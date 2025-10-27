# flexia_extended_exercises.py
import cv2
import socket
import struct
import pickle
import mediapipe as mp

mp_hands = mp.solutions.hands
hands = mp_hands.Hands()
mp_draw = mp.solutions.drawing_utils

# Configurar socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("localhost", 9999))
server_socket.listen(1)
print("Esperando conexión desde Java en localhost:9999...")

conn, addr = server_socket.accept()
print("Conectado a:", addr)

cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Procesamiento con MediaPipe (detección de manos)
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = hands.process(frame_rgb)
    if results.multi_hand_landmarks:
        for hand_landmarks in results.multi_hand_landmarks:
            mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)

    # Comprimir el frame a JPEG antes de enviarlo
    ret, buffer = cv2.imencode('.jpg', frame)
    data = buffer.tobytes()

    # Enviar tamaño + datos JPEG
    conn.sendall(struct.pack(">L", len(data)) + data)

cap.release()
conn.close()
server_socket.close()
