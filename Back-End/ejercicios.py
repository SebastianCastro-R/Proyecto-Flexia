import cv2
import socket
import struct
import mediapipe as mp
import time
import sys
import io
import math
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# ===============================
# FUNCIONES DE DETECCIÓN POR EJERCICIO
# ===============================

def calcular_distancia(punto1, punto2):
    """Calcula la distancia euclidiana entre dos landmarks"""
    return ((punto1.x - punto2.x)**2 + (punto1.y - punto2.y)**2)**0.5

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
    cos_angle = max(-1, min(1, cos_angle))
    
    return math.degrees(math.acos(cos_angle))

def determinar_mano(landmarks):
    """Determina si es mano izquierda o derecha"""
    return "right" if landmarks[5].x < landmarks[17].x else "left"

# --- EJERCICIO 1: MANO ABIERTA ---
def mano_abierta(landmarks):
    """Detectar mano completamente abierta"""
    dedos = []
    hand_info = determinar_mano(landmarks)
    is_right_hand = hand_info == "right"
    
    # Pulgar
    if is_right_hand:
        dedos.append(1 if landmarks[4].x > landmarks[3].x else 0)
    else:
        dedos.append(1 if landmarks[4].x < landmarks[3].x else 0)
    
    # Otros dedos
    for tip in [8, 12, 16, 20]:
        dedos.append(1 if landmarks[tip].y < landmarks[tip - 2].y else 0)
    
    return sum(dedos) >= 4

# --- EJERCICIO 2: PUÑO CERRADO ---
def puno_cerrado(landmarks):
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

# --- EJERCICIO 3: GARRA ---
def garra(landmarks):
    """Detecta posición de garra (puntas flexionadas)"""
    dedos_garra = 0
    
    for tip in [8, 12, 16, 20]:
        punta = landmarks[tip]
        media = landmarks[tip - 1]
        base = landmarks[tip - 2]
        
        if punta.y > media.y and media.y < base.y:
            dedos_garra += 1
    
    return dedos_garra >= 3

# --- EJERCICIO 4: DEDOS SEPARADOS ---
def dedos_separados(landmarks):
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

# --- EJERCICIO 5: PULGAR A ÍNDICE ---
def pulgar_indice(landmarks):
    """Detecta si el pulgar toca el índice"""
    distancia = calcular_distancia(landmarks[4], landmarks[8])
    return distancia < 0.05

# --- EJERCICIO 6: PULGAR A MEÑIQUE ---
def pulgar_menique(landmarks):
    """Detecta si el pulgar toca el meñique"""
    distancia = calcular_distancia(landmarks[4], landmarks[20])
    return distancia < 0.06

# --- EJERCICIO 7: OK SIGN ---
def ok_sign(landmarks):
    """Detecta el signo OK (círculo con pulgar e índice)"""
    # Distancia entre pulgar e índice debe ser pequeña
    distancia_pulgar_indice = calcular_distancia(landmarks[4], landmarks[8])
    
    # Los otros dedos deben estar extendidos
    otros_extendidos = 0
    for tip in [12, 16, 20]:
        if landmarks[tip].y < landmarks[tip - 2].y:
            otros_extendidos += 1
    
    return distancia_pulgar_indice < 0.05 and otros_extendidos >= 2

# --- EJERCICIO 8: EXTENSIÓN LATERAL ---
def extension_lateral(landmarks):
    """Detecta extensión lateral de la mano"""
    hand_info = determinar_mano(landmarks)
    is_right_hand = hand_info == "right"
    
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

# --- EJERCICIO 9: FLEXIÓN DE MUÑECA ---
def flexion_muneca(landmarks):
    """Detecta flexión de muñeca hacia abajo"""
    muneca = landmarks[0]
    medio_tip = landmarks[12]
    
    # La muñeca flexionada hace que los dedos apunten hacia abajo
    return medio_tip.y > muneca.y + 0.05

# --- EJERCICIO 10: EXTENSIÓN DE MUÑECA ---
def extension_muneca(landmarks):
    """Detecta extensión de muñeca hacia arriba"""
    muneca = landmarks[0]
    medio_tip = landmarks[12]
    
    # La muñeca extendida hace que los dedos apunten hacia arriba
    return medio_tip.y < muneca.y - 0.05

# --- EJERCICIO 11: DEDOS EN PINZA ---
def dedos_pinza(landmarks):
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

# --- EJERCICIO 12: PAZ Y AMOR ---
def paz_amor(landmarks):
    """Detecta signo de paz (solo índice y medio extendidos)"""
    # Índice y medio extendidos
    indice_extendido = landmarks[8].y < landmarks[6].y
    medio_extendido = landmarks[12].y < landmarks[10].y
    
    # Anular y meñique cerrados
    anular_cerrado = landmarks[16].y > landmarks[14].y
    menique_cerrado = landmarks[20].y > landmarks[18].y
    
    return indice_extendido and medio_extendido and anular_cerrado and menique_cerrado

# ===============================
# CONFIGURACIÓN PRINCIPAL
# ===============================

class EjercicioManager:
    def __init__(self):
        self.mp_hands = mp.solutions.hands
        self.hands = self.mp_hands.Hands(
            static_image_mode=False,
            max_num_hands=1,
            min_detection_confidence=0.7,
            min_tracking_confidence=0.5
        )
        self.mp_draw = mp.solutions.drawing_utils
        
        # Mapeo completo de ejercicios (todos los 12)
        self.ejercicios = {
            "ejercicio1": {
                "funcion": mano_abierta,
                "nombre": "Mano Abierta",
                "descripcion": "Extiende todos los dedos completamente",
                "categoria": "Extensión",
                "instruccion": "Mantén la mano completamente ABIERTA"
            },
            "ejercicio2": {
                "funcion": puno_cerrado,
                "nombre": "Puño Cerrado", 
                "descripcion": "Cierra la mano en un puño",
                "categoria": "Flexión",
                "instruccion": "Cierra completamente la mano (PUÑO)"
            },
            "ejercicio3": {
                "funcion": garra,
                "nombre": "Garra",
                "descripcion": "Flexiona solo las puntas de los dedos",
                "categoria": "Flexión Parcial",
                "instruccion": "Flexiona solo las PUNTAS de los dedos"
            },
            "ejercicio4": {
                "funcion": dedos_separados,
                "nombre": "Dedos Separados",
                "descripcion": "Separa todos los dedos lo más posible",
                "categoria": "Abducción",
                "instruccion": "Separa todos los dedos al MÁXIMO"
            },
            "ejercicio5": {
                "funcion": pulgar_indice,
                "nombre": "Pulgar a Índice",
                "descripcion": "Toca el pulgar con el dedo índice",
                "categoria": "Oposición",
                "instruccion": "Junta PULGAR con ÍNDICE"
            },
            "ejercicio6": {
                "funcion": pulgar_menique,
                "nombre": "Pulgar a Meñique",
                "descripcion": "Toca el pulgar con el dedo meñique",
                "categoria": "Oposición",
                "instruccion": "Junta PULGAR con MEÑIQUE"
            },
            "ejercicio7": {
                "funcion": ok_sign,
                "nombre": "OK Sign",
                "descripcion": "Forma un círculo con pulgar e índice",
                "categoria": "Precisión",
                "instruccion": "Forma un CÍRCULO con pulgar e índice"
            },
            "ejercicio8": {
                "funcion": extension_lateral,
                "nombre": "Extensión Lateral",
                "descripcion": "Extiende la mano hacia un lado",
                "categoria": "Desviación",
                "instruccion": "Extiende la mano hacia un LADO"
            },
            "ejercicio9": {
                "funcion": flexion_muneca,
                "nombre": "Flexión de Muñeca",
                "descripcion": "Flexiona la muñeca hacia abajo",
                "categoria": "Muñeca",
                "instruccion": "Flexiona la MUÑECA hacia ABAJO"
            },
            "ejercicio10": {
                "funcion": extension_muneca,
                "nombre": "Extensión de Muñeca",
                "descripcion": "Extiende la muñeca hacia arriba",
                "categoria": "Muñeca",
                "instruccion": "Extiende la MUÑECA hacia ARRIBA"
            },
            "ejercicio11": {
                "funcion": dedos_pinza,
                "nombre": "Dedos en Pinza",
                "descripcion": "Junta todos los dedos en una punta",
                "categoria": "Precisión",
                "instruccion": "Junta todos los dedos en PUNTA"
            },
            "ejercicio12": {
                "funcion": paz_amor,
                "nombre": "Paz y Amor",
                "descripcion": "Muestra solo índice y medio extendidos",
                "categoria": "Selectivo",
                "instruccion": "Solo ÍNDICE y MEDIO extendidos"
            }
        }
        
        self.ejercicio_actual = None
        self.conn = None
        
    def iniciar_servidor(self):
        """Inicia el servidor socket"""
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.bind(("localhost", 9999))
        server_socket.listen(1)
        print("🔄 Servidor de ejercicios iniciado - Esperando conexión Java...")
        print("📋 Ejercicios disponibles:")
        for key, ej in self.ejercicios.items():
            print(f"   {key}: {ej['nombre']} - {ej['categoria']}")
        
        self.conn, addr = server_socket.accept()
        print(f"✅ Conectado a Java: {addr}")
        
        # Esperar el ejercicio seleccionado desde Java
        ejercicio_seleccionado = self.recibir_ejercicio()
        return ejercicio_seleccionado
    
    def recibir_ejercicio(self):
        """Recibe el nombre del ejercicio desde Java"""
        try:
            # Java envía: "EJERCICIO:ejercicio2"
            data = self.conn.recv(1024).decode('utf-8').strip()
            if data.startswith("EJERCICIO:"):
                ejercicio = data.split(":")[1]
                print(f"🎯 Ejercicio seleccionado desde Java: {ejercicio}")
                return ejercicio
            else:
                print("⚠️ Formato de ejercicio incorrecto, usando ejercicio1 por defecto")
                return "ejercicio1"
        except Exception as e:
            print(f"⚠️ Error recibiendo ejercicio: {e}, usando ejercicio1 por defecto")
            return "ejercicio1"
    
    def ejecutar_ejercicio(self, ejercicio_nombre):
        """Ejecuta el ejercicio seleccionado"""
        if ejercicio_nombre not in self.ejercicios:
            print(f"❌ Ejercicio {ejercicio_nombre} no encontrado, usando ejercicio1")
            ejercicio_nombre = "ejercicio1"
        
        ejercicio_info = self.ejercicios[ejercicio_nombre]
        self.ejercicio_actual = ejercicio_info
        
        print(f"▶️ Iniciando: {ejercicio_info['nombre']}")
        print(f"📝 Instrucción: {ejercicio_info['instruccion']}")
        print(f"📋 Categoría: {ejercicio_info['categoria']}")
        print(f"📖 Descripción: {ejercicio_info['descripcion']}")
        
        cap = cv2.VideoCapture(0)
        ultimo_estado = False
        ultimo_envio = 0
        contador_estado_actual = 0
        frames_requeridos = 5
        
        # Variables para el nuevo sistema
        tiempo_inicio_ejercicio = None  # Tiempo cuando empezó a mantener la posición
        tiempo_mantencion_requerido = 3.0  # Segundos que debe mantener la posición
        ultimo_ok_enviado = 0  # Timestamp del último OK enviado
        tiempo_entre_oks = 1.0  # Esperar 1 segundo antes de enviar otro OK
        
        try:
            while True:
                ret, frame = cap.read()
                if not ret:
                    continue

                frame = cv2.flip(frame, 1)
                frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                results = self.hands.process(frame_rgb)

                estado_actual = False
                if results.multi_hand_landmarks:
                    for hand_landmarks in results.multi_hand_landmarks:
                        self.mp_draw.draw_landmarks(frame, hand_landmarks, self.mp_hands.HAND_CONNECTIONS)
                        
                        if ejercicio_info['funcion'](hand_landmarks.landmark):
                            estado_actual = True

                # Suavizar detección
                if estado_actual:
                    contador_estado_actual = min(contador_estado_actual + 1, frames_requeridos)
                else:
                    contador_estado_actual = max(contador_estado_actual - 1, 0)

                estado_suavizado = contador_estado_actual >= frames_requeridos

                # === NUEVA LÓGICA: Enviar OK por cada repetición ===
                tiempo_actual = time.time()
                
                if estado_suavizado:
                    # Iniciar temporizador de mantención
                    if tiempo_inicio_ejercicio is None:
                        tiempo_inicio_ejercicio = tiempo_actual
                        print(f"⏱️ Iniciando conteo de {tiempo_mantencion_requerido}s...")
                    
                    # Verificar si ha mantenido la posición el tiempo suficiente
                    tiempo_transcurrido = tiempo_actual - tiempo_inicio_ejercicio
                    
                    if tiempo_transcurrido >= tiempo_mantencion_requerido:
                        # Verificar si ya pasó suficiente tiempo desde el último OK
                        if (tiempo_actual - ultimo_ok_enviado) >= tiempo_entre_oks:
                            # Enviar STATUS:OK (una repetición completada)
                            self.enviar_status("OK")
                            ultimo_ok_enviado = tiempo_actual
                            
                            print(f"✅ {ejercicio_info['nombre']} - Repetición completada!")
                            print(f"⏱️ Tiempo mantenido: {tiempo_transcurrido:.1f}s")
                            
                            # Reiniciar temporizador para la siguiente repetición
                            tiempo_inicio_ejercicio = tiempo_actual
                else:
                    # Resetear temporizador si pierde la posición
                    if tiempo_inicio_ejercicio is not None:
                        print(f"🔄 Perdió la posición. Reiniciando conteo...")
                        tiempo_inicio_ejercicio = None
                    
                    # Solo enviar RESET si antes estaba en estado correcto
                    if ultimo_estado:
                        self.enviar_status("RESET")
                        print(f"🔄 {ejercicio_info['nombre']} - Perdió posición")

                ultimo_estado = estado_suavizado

                # Enviar frame
                if tiempo_actual - ultimo_envio > 0.1:
                    ret, buffer = cv2.imencode('.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 80])
                    if ret:
                        try:
                            data = buffer.tobytes()
                            self.conn.sendall(struct.pack(">L", len(data)) + data)
                        except:
                            print("❌ Conexión perdida")
                            break
                        ultimo_envio = tiempo_actual

        except Exception as e:
            print(f"⚠️ Error en ejercicio: {e}")
        finally:
            cap.release()
            cv2.destroyAllWindows()    
    def enviar_status(self, status):
        """Envía STATUS al cliente Java"""
        try:
            mensaje = f"STATUS:{status}".encode('utf-8')
            self.conn.sendall(struct.pack(">L", len(mensaje)) + mensaje)
        except:
            print("❌ Error enviando status")
    
    def cerrar(self):
        """Cierra conexiones"""
        try:
            if self.conn:
                self.conn.close()
        except:
            pass

# ===============================
# EJECUCIÓN PRINCIPAL
# ===============================
if __name__ == "__main__":
    manager = EjercicioManager()
    
    try:
        # 1. Iniciar servidor y recibir ejercicio desde Java
        ejercicio = manager.iniciar_servidor()
        
        # 2. Ejecutar el ejercicio seleccionado
        manager.ejecutar_ejercicio(ejercicio)
        
    except Exception as e:
        print(f"❌ Error general: {e}")
    finally:
        manager.cerrar()
        print("🔚 Servidor cerrado correctamente")