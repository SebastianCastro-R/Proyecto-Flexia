import cv2
import socket
import struct
import mediapipe as mp
import time
import sys, io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def mano_abierta(hand_landmarks):
    """Ejercicio 1: Detectar mano completamente abierta"""
    dedos = [8, 12, 16, 20]  # puntas de los dedos
    nudillos = [6, 10, 14, 18]  # nudillos base
    
    # Verificar que los 4 dedos estén extendidos
    dedos_extendidos = sum(hand_landmarks.landmark[d].y < hand_landmarks.landmark[n].y 
                          for d, n in zip(dedos, nudillos))
    
    # Verificar pulgar extendido
    pulgar_abierto = (
        hand_landmarks.landmark[4].x < hand_landmarks.landmark[3].x
        if hand_landmarks.landmark[17].x > hand_landmarks.landmark[5].x  # mano derecha
        else hand_landmarks.landmark[4].x > hand_landmarks.landmark[3].x  # mano izquierda
    )
    
    return dedos_extendidos == 4 and pulgar_abierto

# Configuración de MediaPipe
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(
    static_image_mode=False,
    max_num_hands=1,  # Solo una mano para este ejercicio
    min_detection_confidence=0.7,
    min_tracking_confidence=0.5
)
mp_draw = mp.solutions.drawing_utils

# Configuración del servidor
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("localhost", 9999))
server_socket.listen(1)
print("EJERCICIO 1 - Esperando conexión desde Java en localhost:9999...")
print("Instrucción: Mantén la mano completamente ABIERTA")

conn, addr = server_socket.accept()
print("Conectado a:", addr)

cap = cv2.VideoCapture(0)
ultimo_estado = False
ultimo_envio = 0
contador_estado_actual = 0
frames_requeridos = 5  # Frames consecutivos requeridos para confirmar

try:
    while True:
        ret, frame = cap.read()
        if not ret:
            continue

        # Voltear la cámara horizontalmente (efecto espejo)
        frame = cv2.flip(frame, 1)
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(frame_rgb)

        estado_actual = False
        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                # Dibujar landmarks
                mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)
                
                if mano_abierta(hand_landmarks):
                    estado_actual = True
                    
                    # Dibujar texto de confirmación
                    cv2.putText(frame, "MANO ABIERTA DETECTADA", (50, 50),
                               cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

        # Suavizar la detección
        if estado_actual:
            contador_estado_actual = min(contador_estado_actual + 1, frames_requeridos)
        else:
            contador_estado_actual = max(contador_estado_actual - 1, 0)

        # Estado suavizado
        estado_suavizado = contador_estado_actual >= frames_requeridos

        # Enviar estado solo cuando cambia
        if estado_suavizado and not ultimo_estado:
            try:
                mensaje = b"STATUS:OK"
                conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                print("✅ EJERCICIO 1 COMPLETADO - Mano abierta detectada")
            except (BrokenPipeError, ConnectionResetError):
                print("Conexión con Java perdida (status).")
                break
        elif not estado_suavizado and ultimo_estado:
            try:
                mensaje = b"STATUS:RESET"
                conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                print("🔄 Ejercicio 1 no completado")
            except (BrokenPipeError, ConnectionResetError):
                print("Conexión con Java perdida (status).")
                break

        ultimo_estado = estado_suavizado


        # Enviar frame comprimido (limitado a 10 FPS)
        if time.time() - ultimo_envio > 0.1:
            ret, buffer = cv2.imencode('.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 80])
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
    print("🔚 Conexión cerrada correctamente.")