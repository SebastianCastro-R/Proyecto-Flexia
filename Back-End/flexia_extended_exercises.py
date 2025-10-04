import cv2
import mediapipe as mp
import pygame
import numpy as np
import math
import time
import json
import os

# Inicializar mediapipe
mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
hands = mp_hands.Hands(min_detection_confidence=0.7, min_tracking_confidence=0.7)

# Inicializar pygame
pygame.init()
screen = pygame.display.set_mode((1000, 720))  # más ancho para más información
pygame.display.set_caption("Flexia - Rehabilitación Túnel Carpiano Extendida")

# Cargar imagen de feedback
try:
    imagen_feedback = pygame.image.load("check.webp")
    imagen_feedback = pygame.transform.scale(imagen_feedback, (120, 120))
except:
    imagen_feedback = None

# Captura de video
cap = cv2.VideoCapture(0)

# Configuración de colores
COLOR_BLANCO = (255, 255, 255)
COLOR_VERDE = (0, 255, 0)
COLOR_ROJO = (255, 0, 0)
COLOR_AMARILLO = (255, 255, 0)
COLOR_AZUL = (100, 150, 255)
COLOR_GRIS = (150, 150, 150)

# Lista completa de ejercicios
EJERCICIOS = {
    1: {
        "nombre": "Mano Abierta",
        "descripcion": "Extiende todos los dedos completamente",
        "funcion": "mano_abierta",
        "categoria": "Extensión"
    },
    2: {
        "nombre": "Puño Cerrado", 
        "descripcion": "Cierra la mano en un puño",
        "funcion": "puno_cerrado",
        "categoria": "Flexión"
    },
    3: {
        "nombre": "Garra",
        "descripcion": "Flexiona solo las puntas de los dedos",
        "funcion": "garra",
        "categoria": "Flexión Parcial"
    },
    4: {
        "nombre": "Dedos Separados",
        "descripcion": "Separa todos los dedos lo más posible",
        "funcion": "dedos_separados",
        "categoria": "Abducción"
    },
    5: {
        "nombre": "Pulgar a Índice",
        "descripcion": "Toca el pulgar con el dedo índice",
        "funcion": "pulgar_indice",
        "categoria": "Oposición"
    },
    6: {
        "nombre": "Pulgar a Meñique",
        "descripcion": "Toca el pulgar con el dedo meñique",
        "funcion": "pulgar_menique",
        "categoria": "Oposición"
    },
    7: {
        "nombre": "OK Sign",
        "descripcion": "Forma un círculo con pulgar e índice",
        "funcion": "ok_sign",
        "categoria": "Precisión"
    },
    8: {
        "nombre": "Extensión Lateral",
        "descripcion": "Extiende la mano hacia un lado",
        "funcion": "extension_lateral",
        "categoria": "Desviación"
    },
    9: {
        "nombre": "Flexión de Muñeca",
        "descripcion": "Flexiona la muñeca hacia abajo",
        "funcion": "flexion_muneca",
        "categoria": "Muñeca"
    },
    10: {
        "nombre": "Extensión de Muñeca",
        "descripcion": "Extiende la muñeca hacia arriba",
        "funcion": "extension_muneca",
        "categoria": "Muñeca"
    },
    11: {
        "nombre": "Dedos en Pinza",
        "descripcion": "Junta todos los dedos en una punta",
        "funcion": "dedos_pinza",
        "categoria": "Precisión"
    },
    12: {
        "nombre": "Paz y Amor",
        "descripcion": "Muestra solo índice y medio extendidos",
        "funcion": "paz_amor",
        "categoria": "Selectivo"
    }
}

# Variables del sistema
ejercicio_actual = 1
contador_repeticiones = 0
tiempo_inicio_ejercicio = time.time()
estado_anterior = False
session_data = {"ejercicios": {}, "tiempo_total": 0}

def calcular_distancia(punto1, punto2):
    """Calcula la distancia euclidiana entre dos puntos"""
    return math.sqrt((punto1.x - punto2.x)**2 + (punto1.y - punto2.y)**2)

def calcular_angulo(p1, p2, p3):
    """Calcula el ángulo formado por tres puntos"""
    v1 = (p1.x - p2.x, p1.y - p2.y)
    v2 = (p3.x - p2.x, p3.y - p2.y)
    
    dot_product = v1[0] * v2[0] + v1[1] * v2[1]
    mag_v1 = math.sqrt(v1[0]**2 + v1[1]**2)
    mag_v2 = math.sqrt(v2[0]**2 + v2[1]**2)
    
    if mag_v1 == 0 or mag_v2 == 0:
        return 0
    
    cos_angle = dot_product / (mag_v1 * mag_v2)
    cos_angle = max(-1, min(1, cos_angle))  # Clamp to [-1, 1]
    
    return math.degrees(math.acos(cos_angle))

def mano_abierta(landmarks, hand_info):
    """Detecta si la mano está completamente abierta"""
    dedos = []
    is_right_hand = hand_info.classification[0].label == "Right"
    
    # Pulgar
    if is_right_hand:
        dedos.append(1 if landmarks[4].x > landmarks[3].x else 0)
    else:
        dedos.append(1 if landmarks[4].x < landmarks[3].x else 0)
    
    # Otros dedos
    for tip in [8, 12, 16, 20]:
        dedos.append(1 if landmarks[tip].y < landmarks[tip - 2].y else 0)
    
    return sum(dedos) >= 4

def puno_cerrado(landmarks, hand_info):
    """Detecta si la mano está cerrada en puño"""
    dedos_cerrados = 0
    
    # Verificar dedos principales
    for tip in [8, 12, 16, 20]:
        distancia_base = calcular_distancia(landmarks[tip], landmarks[tip - 3])
        if distancia_base < 0.06:
            dedos_cerrados += 1
    
    # Pulgar
    distancia_pulgar = calcular_distancia(landmarks[4], landmarks[2])
    if distancia_pulgar < 0.06:
        dedos_cerrados += 1
    
    return dedos_cerrados >= 4

def garra(landmarks, hand_info):
    """Detecta posición de garra (puntas flexionadas)"""
    dedos_garra = 0
    
    for tip in [8, 12, 16, 20]:
        punta = landmarks[tip]
        media = landmarks[tip - 1]
        base = landmarks[tip - 2]
        
        if punta.y > media.y and media.y < base.y:
            dedos_garra += 1
    
    return dedos_garra >= 3

def dedos_separados(landmarks, hand_info):
    """Detecta si los dedos están separados"""
    separaciones = 0
    dedos_tips = [8, 12, 16, 20]
    
    for i in range(len(dedos_tips) - 1):
        distancia = calcular_distancia(landmarks[dedos_tips[i]], landmarks[dedos_tips[i + 1]])
        if distancia > 0.08:
            separaciones += 1
    
    # Separación pulgar-índice
    distancia_pulgar_indice = calcular_distancia(landmarks[4], landmarks[8])
    if distancia_pulgar_indice > 0.1:
        separaciones += 1
    
    return separaciones >= 3

def pulgar_indice(landmarks, hand_info):
    """Detecta si el pulgar toca el índice"""
    distancia = calcular_distancia(landmarks[4], landmarks[8])
    return distancia < 0.05

def pulgar_menique(landmarks, hand_info):
    """Detecta si el pulgar toca el meñique"""
    distancia = calcular_distancia(landmarks[4], landmarks[20])
    return distancia < 0.06

def ok_sign(landmarks, hand_info):
    """Detecta el signo OK (círculo con pulgar e índice)"""
    # Distancia entre pulgar e índice debe ser pequeña
    distancia_pulgar_indice = calcular_distancia(landmarks[4], landmarks[8])
    
    # Los otros dedos deben estar extendidos
    otros_extendidos = 0
    for tip in [12, 16, 20]:
        if landmarks[tip].y < landmarks[tip - 2].y:
            otros_extendidos += 1
    
    return distancia_pulgar_indice < 0.05 and otros_extendidos >= 2

def extension_lateral(landmarks, hand_info):
    """Detecta extensión lateral de la mano"""
    is_right_hand = hand_info.classification[0].label == "Right"
    
    # Verificar si la mano está extendida lateralmente
    muneca = landmarks[0]
    medio = landmarks[12]
    
    # Calcular inclinación
    if is_right_hand:
        lateral = medio.x > muneca.x + 0.1
    else:
        lateral = medio.x < muneca.x - 0.1
    
    # Verificar que los dedos están extendidos
    dedos_extendidos = sum([1 for tip in [8, 12, 16, 20] if landmarks[tip].y < landmarks[tip - 2].y])
    
    return lateral and dedos_extendidos >= 3

def flexion_muneca(landmarks, hand_info):
    """Detecta flexión de muñeca hacia abajo"""
    muneca = landmarks[0]
    medio_base = landmarks[9]
    medio_tip = landmarks[12]
    
    # La muñeca flexionada hace que los dedos apunten hacia abajo
    return medio_tip.y > muneca.y + 0.05

def extension_muneca(landmarks, hand_info):
    """Detecta extensión de muñeca hacia arriba"""
    muneca = landmarks[0]
    medio_tip = landmarks[12]
    
    # La muñeca extendida hace que los dedos apunten hacia arriba
    return medio_tip.y < muneca.y - 0.05

def dedos_pinza(landmarks, hand_info):
    """Detecta todos los dedos juntos en punta"""
    # Todas las puntas de los dedos deben estar cerca entre sí
    tips = [4, 8, 12, 16, 20]  # incluye pulgar
    distancias_pequeñas = 0
    
    for i in range(len(tips)):
        for j in range(i + 1, len(tips)):
            distancia = calcular_distancia(landmarks[tips[i]], landmarks[tips[j]])
            if distancia < 0.08:
                distancias_pequeñas += 1
    
    return distancias_pequeñas >= 6

def paz_amor(landmarks, hand_info):
    """Detecta signo de paz (solo índice y medio extendidos)"""
    # Índice y medio extendidos
    indice_extendido = landmarks[8].y < landmarks[6].y
    medio_extendido = landmarks[12].y < landmarks[10].y
    
    # Anular y meñique cerrados
    anular_cerrado = landmarks[16].y > landmarks[14].y
    menique_cerrado = landmarks[20].y > landmarks[18].y
    
    return indice_extendido and medio_extendido and anular_cerrado and menique_cerrado

def evaluar_ejercicio_actual(landmarks, hand_info):
    """Evalúa el ejercicio actual"""
    funcion_nombre = EJERCICIOS[ejercicio_actual]["funcion"]
    
    funciones = {
        "mano_abierta": mano_abierta,
        "puno_cerrado": puno_cerrado,
        "garra": garra,
        "dedos_separados": dedos_separados,
        "pulgar_indice": pulgar_indice,
        "pulgar_menique": pulgar_menique,
        "ok_sign": ok_sign,
        "extension_lateral": extension_lateral,
        "flexion_muneca": flexion_muneca,
        "extension_muneca": extension_muneca,
        "dedos_pinza": dedos_pinza,
        "paz_amor": paz_amor
    }
    
    if funcion_nombre in funciones:
        return funciones[funcion_nombre](landmarks, hand_info)
    
    return False

def actualizar_progreso(correcto):
    """Actualiza el progreso del ejercicio"""
    global contador_repeticiones, estado_anterior, session_data
    
    # Detectar transición de incorrecto a correcto (nueva repetición)
    if correcto and not estado_anterior:
        contador_repeticiones += 1
        
        # Guardar en sesión
        ej_name = EJERCICIOS[ejercicio_actual]["nombre"]
        if ej_name not in session_data["ejercicios"]:
            session_data["ejercicios"][ej_name] = 0
        session_data["ejercicios"][ej_name] += 1
    
    estado_anterior = correcto

def dibujar_interfaz_extendida(screen, correcto):
    """Dibuja la interfaz de usuario extendida"""
    # Fuentes
    font_titulo = pygame.font.Font(None, 28)
    font_desc = pygame.font.Font(None, 22)
    font_info = pygame.font.Font(None, 18)
    font_small = pygame.font.Font(None, 16)
    
    # Panel lateral derecho para información
    pygame.draw.rect(screen, (30, 30, 30), (650, 0, 350, 720))
    
    # Información del ejercicio actual
    y_pos = 20
    
    # Título del ejercicio
    titulo = font_titulo.render(f"Ejercicio {ejercicio_actual}/12", True, COLOR_BLANCO)
    screen.blit(titulo, (660, y_pos))
    y_pos += 35
    
    # Nombre del ejercicio
    nombre = font_desc.render(EJERCICIOS[ejercicio_actual]["nombre"], True, COLOR_AZUL)
    screen.blit(nombre, (660, y_pos))
    y_pos += 25
    
    # Categoría
    categoria = font_info.render(f"Categoría: {EJERCICIOS[ejercicio_actual]['categoria']}", True, COLOR_GRIS)
    screen.blit(categoria, (660, y_pos))
    y_pos += 25
    
    # Descripción
    desc_lines = []
    desc_text = EJERCICIOS[ejercicio_actual]["descripcion"]
    words = desc_text.split()
    current_line = []
    
    for word in words:
        test_line = " ".join(current_line + [word])
        if font_info.size(test_line)[0] < 300:
            current_line.append(word)
        else:
            if current_line:
                desc_lines.append(" ".join(current_line))
                current_line = [word]
    
    if current_line:
        desc_lines.append(" ".join(current_line))
    
    for line in desc_lines:
        desc = font_info.render(line, True, COLOR_BLANCO)
        screen.blit(desc, (660, y_pos))
        y_pos += 20
    
    y_pos += 15
    
    # Estado del ejercicio
    if correcto:
        if imagen_feedback:
            screen.blit(imagen_feedback, (750, y_pos))
        estado = font_desc.render("¡Correcto!", True, COLOR_VERDE)
        screen.blit(estado, (660, y_pos + 130))
    else:
        estado = font_desc.render("Intenta el ejercicio...", True, COLOR_AMARILLO)
        screen.blit(estado, (660, y_pos))
    
    y_pos += 160
    
    # Contador de repeticiones
    rep_text = font_info.render(f"Repeticiones: {contador_repeticiones}", True, COLOR_BLANCO)
    screen.blit(rep_text, (660, y_pos))
    y_pos += 25
    
    # Tiempo de sesión
    tiempo_sesion = int(time.time() - tiempo_inicio_ejercicio)
    tiempo_text = font_info.render(f"Tiempo: {tiempo_sesion//60:02d}:{tiempo_sesion%60:02d}", True, COLOR_BLANCO)
    screen.blit(tiempo_text, (660, y_pos))
    y_pos += 40
    
    # Controles
    controles_titulo = font_info.render("Controles:", True, COLOR_BLANCO)
    screen.blit(controles_titulo, (660, y_pos))
    y_pos += 25
    
    controles = [
        "← → : Cambiar ejercicio",
        "1-9, 0: Ejercicio directo",
        "R: Reiniciar contador",
        "S: Guardar progreso",
        "ESC/Q: Salir"
    ]
    
    for control in controles:
        texto = font_small.render(control, True, COLOR_GRIS)
        screen.blit(texto, (660, y_pos))
        y_pos += 18
    
    y_pos += 20
    
    # Lista de ejercicios
    ejercicios_titulo = font_info.render("Ejercicios disponibles:", True, COLOR_BLANCO)
    screen.blit(ejercicios_titulo, (660, y_pos))
    y_pos += 25
    
    # Mostrar ejercicios en dos columnas si hay muchos
    ejercicios_por_mostrar = 6  # Mostrar solo los primeros 6 para ahorrar espacio
    for i in range(1, ejercicios_por_mostrar + 1):
        color = COLOR_VERDE if i == ejercicio_actual else COLOR_GRIS
        texto = font_small.render(f"{i}. {EJERCICIOS[i]['nombre'][:15]}", True, color)
        screen.blit(texto, (660, y_pos))
        y_pos += 18
    
    if len(EJERCICIOS) > ejercicios_por_mostrar:
        mas_texto = font_small.render(f"... y {len(EJERCICIOS) - ejercicios_por_mostrar} más", True, COLOR_GRIS)
        screen.blit(mas_texto, (660, y_pos))

def guardar_progreso():
    """Guarda el progreso de la sesión"""
    session_data["tiempo_total"] = int(time.time() - tiempo_inicio_ejercicio)
    
    # Crear directorio si no existe
    os.makedirs("progreso", exist_ok=True)
    
    # Nombre del archivo con timestamp
    timestamp = time.strftime("%Y%m%d_%H%M%S")
    filename = f"progreso/sesion_{timestamp}.json"
    
    try:
        with open(filename, 'w') as f:
            json.dump(session_data, f, indent=4)
        return filename
    except Exception as e:
        print(f"Error guardando progreso: {e}")
        return None

# Loop principal del programa
clock = pygame.time.Clock()
running = True
mensaje_guardado = ""
tiempo_mensaje = 0

print("\n=== Flexia - Rehabilitación Túnel Carpiano Extendida ===")
print("Ejercicios disponibles:")
for num, ej in EJERCICIOS.items():
    print(f"{num:2d}. {ej['nombre']} - {ej['categoria']}")
print("\nControles:")
print("  ← →    : Navegar entre ejercicios")
print("  1-9, 0 : Ir directamente al ejercicio")
print("  R      : Reiniciar contador")
print("  S      : Guardar progreso")
print("  ESC/Q  : Salir")
print("\nComenzando...\n")

while running:
    ret, frame = cap.read()
    if not ret:
        break

    frame = cv2.flip(frame, 1)  # efecto espejo
    rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = hands.process(rgb)

    correcto = False

    if results.multi_hand_landmarks:
        for i, hand_landmarks in enumerate(results.multi_hand_landmarks):
            mp_drawing.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)
            
            hand_info = results.multi_handedness[i]
            
            if evaluar_ejercicio_actual(hand_landmarks.landmark, hand_info):
                correcto = True

    # Actualizar progreso
    actualizar_progreso(correcto)

    # Convertir frame de OpenCV a Pygame
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    frame_rgb = np.transpose(frame_rgb, (1, 0, 2))
    frame_surface = pygame.surfarray.make_surface(frame_rgb)
    frame_surface = pygame.transform.scale(frame_surface, (640, 480))

    # Dibujar en Pygame
    screen.fill((0, 0, 0))
    screen.blit(frame_surface, (5, 120))  # posicionar cámara

    # Dibujar interfaz extendida
    dibujar_interfaz_extendida(screen, correcto)

    # Mostrar mensaje de guardado temporal
    if mensaje_guardado and time.time() - tiempo_mensaje < 3:
        font = pygame.font.Font(None, 24)
        msg_surface = font.render(mensaje_guardado, True, COLOR_VERDE)
        screen.blit(msg_surface, (20, 50))
    elif time.time() - tiempo_mensaje >= 3:
        mensaje_guardado = ""

    # Título principal
    font_titulo = pygame.font.Font(None, 36)
    titulo_principal = font_titulo.render("FLEXIA - Rehabilitación Túnel Carpiano", True, COLOR_BLANCO)
    screen.blit(titulo_principal, (20, 10))

    pygame.display.update()

    # Manejo de eventos
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_ESCAPE or event.key == pygame.K_q:
                running = False
            elif event.key == pygame.K_LEFT:
                ejercicio_actual = max(1, ejercicio_actual - 1)
                contador_repeticiones = 0
            elif event.key == pygame.K_RIGHT:
                ejercicio_actual = min(len(EJERCICIOS), ejercicio_actual + 1)
                contador_repeticiones = 0
            elif event.key == pygame.K_r:
                contador_repeticiones = 0
                print(f"Contador reiniciado para {EJERCICIOS[ejercicio_actual]['nombre']}")
            elif event.key == pygame.K_s:
                filename = guardar_progreso()
                if filename:
                    mensaje_guardado = f"Progreso guardado: {filename}"
                    tiempo_mensaje = time.time()
                    print(f"Progreso guardado en: {filename}")
                else:
                    mensaje_guardado = "Error al guardar progreso"
                    tiempo_mensaje = time.time()
            elif pygame.K_1 <= event.key <= pygame.K_9:
                nuevo_ejercicio = event.key - pygame.K_0
                if nuevo_ejercicio in EJERCICIOS:
                    ejercicio_actual = nuevo_ejercicio
                    contador_repeticiones = 0
            elif event.key == pygame.K_0:  # 0 para ejercicio 10
                if 10 in EJERCICIOS:
                    ejercicio_actual = 10
                    contador_repeticiones = 0

    if cv2.waitKey(1) & 0xFF == 27:  # ESC adicional
        break
    
    clock.tick(30)  # 30 FPS

# Guardar progreso final y limpiar
filename_final = guardar_progreso()
if filename_final:
    print(f"\nSesión completada. Progreso final guardado en: {filename_final}")
    print(f"Tiempo total de sesión: {session_data['tiempo_total']//60}:{session_data['tiempo_total']%60:02d}")
    if session_data['ejercicios']:
        print("Repeticiones realizadas:")
        for ejercicio, reps in session_data['ejercicios'].items():
            print(f"  {ejercicio}: {reps} repeticiones")

cap.release()
cv2.destroyAllWindows()
pygame.quit()
print("\n¡Gracias por usar Flexia! 💪")
