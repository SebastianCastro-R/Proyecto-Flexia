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

try:
    while True:
        ret, frame = cap.read()
        if not ret:
            continue  # no rompas el bucle

        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(frame_rgb)

        estado_actual = False
        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)
                if mano_abierta(hand_landmarks):
                    estado_actual = True

        # Enviar estado de éxito solo cuando cambia
        if estado_actual and not ultimo_estado:
            try:
                mensaje = b"STATUS:OK"
                conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
                print("Ejercicio completado (mano abierta)")
            except (BrokenPipeError, ConnectionResetError):
                print("Conexión con Java perdida (status).")
                break

        ultimo_estado = estado_actual

        # Enviar frame comprimido (limitado a 10 FPS)
        if time.time() - ultimo_envio > 0.1:
            ret, buffer = cv2.imencode('.jpg', frame)
            data = buffer.tobytes()
            try:
                conn.sendall(struct.pack(">L", len(data)) + data)
            except (BrokenPipeError, ConnectionResetError):
                print("❌ Conexión con Java perdida (frame).")
                break
            ultimo_envio = time.time()

except Exception as e:
    print("⚠️ Error general:", e)
finally:
    cap.release()
    conn.close()
    server_socket.close()
    print("🔚 Conexión cerrada correctamente.")
