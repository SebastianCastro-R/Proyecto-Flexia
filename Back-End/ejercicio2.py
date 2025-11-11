import cv2
import socket
import struct
import mediapipe as mp
import time
import sys, io

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def mano_cerrada(hand_landmarks):
    """Detecta si la mano está completamente cerrada (puño)."""
    dedos = [8, 12, 16, 20]  # puntas de los dedos
    nudillos = [6, 10, 14, 18]  # nudillos base

    # Verificar que los 4 dedos estén flexionados
    dedos_flexionados = sum(
        hand_landmarks.landmark[d].y > hand_landmarks.landmark[n].y
        for d, n in zip(dedos, nudillos)
    )

    # Verificar pulgar flexionado (sobre la palma)
    pulgar_flexionado = (
        hand_landmarks.landmark[4].y > hand_landmarks.landmark[2].y and
        abs(hand_landmarks.landmark[4].x - hand_landmarks.landmark[2].x) < 0.1
    )

    return dedos_flexionados >= 3 and pulgar_flexionado

# Configuración de MediaPipe
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(
    static_image_mode=False,
    max_num_hands=1,
    min_detection_confidence=0.7,
    min_tracking_confidence=0.5
)
mp_draw = mp.solutions.drawing_utils

# Configuración del servidor
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("localhost", 9999))
server_socket.listen(1)
print("🖐 EJERCICIO 2 - Esperando conexión desde Java en localhost:9999...")
print("Instrucción: Cierra la mano completamente (PUÑO) para completar el ejercicio.")

conn, addr = server_socket.accept()
print("🔗 Conectado a:", addr)

cap = cv2.VideoCapture(0)
ultimo_estado = False
ultimo_envio = 0
contador_estado_actual = 0
frames_requeridos = 5  # frames consecutivos requeridos para confirmar el gesto

try:
    while True:
        ret, frame = cap.read()
        if not ret:
            continue

        frame = cv2.flip(frame, 1)
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(frame_rgb)

        estado_actual = False

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)
                if mano_cerrada(hand_landmarks):
                    estado_actual = True

        # Filtrar ruido: requiere varios frames consecutivos del mismo estado
        if estado_actual:
            contador_estado_actual = min(contador_estado_actual + 1, frames_requeridos)
        else:
            contador_estado_actual = max(contador_estado_actual - 1, 0)

        estado_suavizado = contador_estado_actual >= frames_requeridos

        # Solo enviar o imprimir cuando se completa el ejercicio
        if estado_suavizado and not ultimo_estado:
            try:
                mensaje = b"STATUS:OK"
                conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                print("✅ EJERCICIO 2 COMPLETADO - Puño detectado")
            except (BrokenPipeError, ConnectionResetError):
                print("❌ Conexión con Java perdida.")
                break

        # Actualizar estado
        ultimo_estado = estado_suavizado

        # Enviar frame (10 FPS aprox)
        if time.time() - ultimo_envio > 0.1:
            ret, buffer = cv2.imencode('.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 80])
            if ret:
                data = buffer.tobytes()
                try:
                    conn.sendall(struct.pack(">L", len(data)) + data)
                except (BrokenPipeError, ConnectionResetError, OSError):
                    print("❌ Conexión con Java perdida (frame).")
                    break
                ultimo_envio = time.time()

except Exception as e:
    print("⚠️ Error general:", e)
finally:
    try:
        conn.close()
    except:
        pass
    try:
        server_socket.close()
    except:
        pass
    cap.release()
    print("🔚 Conexión cerrada correctamente.")
