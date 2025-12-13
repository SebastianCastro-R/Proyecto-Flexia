import cv2
import socket
import struct
import mediapipe as mp
import time
import sys, io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def mano_abierta(hand_landmarks):
    dedos = [8, 12, 16, 20]
    nudillos = [6, 10, 14, 18]
    abiertos = sum(hand_landmarks.landmark[d].y < hand_landmarks.landmark[n].y for d, n in zip(dedos, nudillos))

    pulgar_abierto = (
        hand_landmarks.landmark[4].x < hand_landmarks.landmark[3].x
        if hand_landmarks.landmark[17].x > hand_landmarks.landmark[5].x
        else hand_landmarks.landmark[4].x > hand_landmarks.landmark[3].x
    )
    return abiertos == 4 and pulgar_abierto

mp_hands = mp.solutions.hands
hands = mp_hands.Hands()
mp_draw = mp.solutions.drawing_utils

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("localhost", 9999))
server_socket.listen(1)
print("Esperando conexión desde Java en localhost:9999...")

conn, addr = server_socket.accept()
print("Conectado a:", addr)

cap = cv2.VideoCapture(0)
ultimo_estado = False
ultimo_envio = 0
contador_estado_actual = 0  # Para evitar cambios bruscos
# Variables para el nuevo sistema
tiempo_inicio_ejercicio = None
tiempo_mantencion_requerido = 3.0  # Segundos requeridos
ultimo_ok_enviado = 0  # Timestamp del último OK enviado
tiempo_entre_oks = 1.0  # Esperar 1 segundo antes de enviar otro OK

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
                if mano_abierta(hand_landmarks):
                    estado_actual = True

        if estado_actual:
            contador_estado_actual = min(contador_estado_actual + 1, 5)
        else:
            contador_estado_actual = max(contador_estado_actual - 1, 0)

        estado_suavizado = contador_estado_actual >= 3

        # === NUEVA LÓGICA ===
        tiempo_actual = time.time()
        
        if estado_suavizado:
            # Iniciar temporizador de mantención
            if tiempo_inicio_ejercicio is None:
                tiempo_inicio_ejercicio = tiempo_actual
                print("⏱️ Iniciando conteo de 3s...")
            
            # Verificar si ha mantenido la posición el tiempo suficiente
            tiempo_transcurrido = tiempo_actual - tiempo_inicio_ejercicio
            
            if tiempo_transcurrido >= tiempo_mantencion_requerido:
                # Verificar si ya pasó suficiente tiempo desde el último OK
                if (tiempo_actual - ultimo_ok_enviado) >= tiempo_entre_oks:
                    # Enviar STATUS:OK (una repetición completada)
                    try:
                        mensaje = b"STATUS:OK"
                        conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                        ultimo_ok_enviado = tiempo_actual
                        
                        print("✅ Repetición completada!")
                        print(f"⏱️ Tiempo mantenido: {tiempo_transcurrido:.1f}s")
                        
                        # Reiniciar temporizador para la siguiente repetición
                        tiempo_inicio_ejercicio = tiempo_actual
                    except (BrokenPipeError, ConnectionResetError):
                        print("Conexión con Java perdida (status).")
                        break
        else:
            # Resetear temporizador si pierde la posición
            if tiempo_inicio_ejercicio is not None:
                print("🔄 Perdió la posición. Reiniciando conteo...")
                tiempo_inicio_ejercicio = None
            
            # Solo enviar RESET si antes estaba en estado correcto
            if ultimo_estado:
                try:
                    mensaje = b"STATUS:RESET"
                    conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                    print("🔄 Ejercicio no completado (mano cerrada)")
                except (BrokenPipeError, ConnectionResetError):
                    print("Conexión con Java perdida (status).")
                    break

        ultimo_estado = estado_suavizado

        # Enviar frame
        if tiempo_actual - ultimo_envio > 0.1:
            ret, buffer = cv2.imencode('.jpg', frame)
            data = buffer.tobytes()
            try:
                conn.sendall(struct.pack(">L", len(data)) + data)
            except (BrokenPipeError, ConnectionResetError):
                print("❌ Conexión con Java perdida (frame).")
                break
            ultimo_envio = tiempo_actual

except Exception as e:
    print("⚠️ Error general:", e)
finally:
    cap.release()
    conn.close()
    server_socket.close()
    print("🔚 Conexión cerrada correctamente.")