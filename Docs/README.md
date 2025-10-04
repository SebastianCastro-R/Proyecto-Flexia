# 🖐️ Flexia - Sistema de Rehabilitación para Túnel Carpiano

**Flexia** es un sistema de rehabilitación interactivo para el síndrome del túnel carpiano que utiliza visión por computadora para detectar y guiar ejercicios de mano y muñeca en tiempo real.

## 🌟 Características

- **12 ejercicios especializados** para rehabilitación del túnel carpiano
- **Detección automática** de ejercicios usando MediaPipe
- **Contador de repeticiones** automático
- **Sistema de progreso** con guardado de sesiones
- **Interfaz intuitiva** con feedback visual en tiempo real
- **Categorización** de ejercicios por tipo terapéutico

## 🏥 Ejercicios Incluidos

### Ejercicios Básicos (1-5)
1. **Mano Abierta** - Extensión completa de dedos
2. **Puño Cerrado** - Fortalecimiento de flexores
3. **Garra** - Flexión parcial selectiva
4. **Dedos Separados** - Abducción y movilidad lateral
5. **Pulgar a Índice** - Oposición básica

### Ejercicios Avanzados (6-12)
6. **Pulgar a Meñique** - Oposición completa
7. **OK Sign** - Precisión y coordinación
8. **Extensión Lateral** - Desviación de muñeca
9. **Flexión de Muñeca** - Estiramiento de extensores
10. **Extensión de Muñeca** - Estiramiento de flexores
11. **Dedos en Pinza** - Coordinación motora fina
12. **Paz y Amor** - Control selectivo de dedos

## 🛠️ Instalación

### Requisitos Previos
- Python 3.7 o superior
- Cámara web funcional
- Windows (testado en Windows, puede funcionar en otros SO)

### Dependencias
Instala las siguientes bibliotecas:

```bash
pip install opencv-python
pip install mediapipe
pip install pygame
pip install numpy
```

### Archivos del Proyecto
```
Proyecto Flexia/
├── flexia_extended_exercises.py    # Programa principal con 12 ejercicios
├── final_count_multi_exercises.py  # Versión básica con 5 ejercicios
├── final_count.py                  # Versión simple (1 ejercicio)
├── guia_ejercicios.md             # Guía detallada de ejercicios
├── README.md                      # Este archivo
├── check.webp                     # Imagen de feedback (opcional)
└── progreso/                      # Carpeta para archivos de progreso (se crea automáticamente)
```

## 🚀 Uso

### Iniciar el Programa
```bash
python flexia_extended_exercises.py
```

### Controles
- **← →** : Navegar entre ejercicios
- **1-9, 0** : Ir directamente a un ejercicio específico
- **R** : Reiniciar contador de repeticiones
- **S** : Guardar progreso de la sesión
- **ESC/Q** : Salir del programa

### Configuración Inicial
1. Asegúrate de tener buena iluminación
2. Posiciona tu mano visible en el encuadre de la cámara
3. Mantén una distancia de 50-80 cm de la cámara
4. El programa detectará tu mano automáticamente

## 📊 Sistema de Progreso

### Guardado Automático
- El programa guarda automáticamente tu progreso al salir
- Los archivos se guardan en la carpeta `progreso/` con formato JSON
- Incluye timestamp, ejercicios realizados, y tiempo de sesión

### Formato de Datos
```json
{
    "ejercicios": {
        "Mano Abierta": 15,
        "Puño Cerrado": 12,
        "Flexión de Muñeca": 8
    },
    "tiempo_total": 450
}
```

## 👩‍⚕️ Recomendaciones Médicas

### Frecuencia Recomendada
- **Principiantes**: 2-3 sesiones/día, 5-10 repeticiones
- **Intermedio**: 3-4 sesiones/día, 10-15 repeticiones  
- **Avanzado**: 4-5 sesiones/día, 15-20 repeticiones

### Progresión Sugerida
1. **Semana 1-2**: Ejercicios básicos (1-5)
2. **Semana 3-4**: Agregar ejercicios de muñeca (6-10)
3. **Semana 5+**: Rutina completa (1-12)

### ⚠️ Precauciones
- No usar si hay dolor severo
- Consultar con profesional de salud antes de comenzar
- Parar si aumentan entumecimiento o hormigueo
- Comenzar gradualmente y aumentar intensidad progresivamente

## 🔧 Personalización

### Ajustar Sensibilidad
En el archivo `flexia_extended_exercises.py`, puedes modificar:
- `min_detection_confidence`: Confianza mínima para detección (0.7 por defecto)
- `min_tracking_confidence`: Confianza mínima para seguimiento (0.7 por defecto)
- Umbrales de distancia en las funciones de ejercicios

### Agregar Nuevos Ejercicios
1. Define la función de detección del ejercicio
2. Agrega el ejercicio al diccionario `EJERCICIOS`
3. Incluye la función en el diccionario `funciones` de `evaluar_ejercicio_actual()`

## 🐛 Resolución de Problemas

### Problemas Comunes

**La cámara no se detecta**
- Verifica que la cámara esté conectada y funcionando
- Cierra otras aplicaciones que puedan estar usando la cámara
- Cambia el índice de cámara en `cv2.VideoCapture(0)` a 1, 2, etc.

**Los ejercicios no se detectan correctamente**
- Mejora la iluminación del ambiente
- Asegúrate de que la mano esté completamente visible
- Ajusta la distancia a la cámara (50-80cm recomendado)
- Verifica que no hay objetos obstruyendo la vista de la mano

**Rendimiento lento**
- Cierra otras aplicaciones pesadas
- Reduce la resolución de la cámara si es necesario
- Asegúrate de tener las últimas versiones de las bibliotecas

### Logs y Depuración
El programa muestra información en la consola:
- Estado de inicio con lista de ejercicios
- Cambios de ejercicio y reseteo de contadores
- Guardado de progreso
- Resumen final de la sesión

## 📖 Documentación Adicional

- **guia_ejercicios.md**: Información detallada sobre cada ejercicio y sus beneficios terapéuticos
- Comentarios en el código fuente para desarrolladores
- Referencias médicas sobre síndrome del túnel carpiano

## 🤝 Contribuciones

Este proyecto fue desarrollado como herramienta de apoyo para rehabilitación. Las contribuciones son bienvenidas:

1. Fork del proyecto
2. Crear rama para nueva característica
3. Commit de los cambios
4. Push a la rama
5. Crear Pull Request

## 📄 Licencia y Disclaimer

**Disclaimer Médico**: Este software es una herramienta de apoyo y NO reemplaza el consejo médico profesional. Siempre consulta con un profesional de la salud calificado para diagnóstico y tratamiento del síndrome del túnel carpiano.

---

## 🚀 Versiones Disponibles

### flexia_extended_exercises.py ⭐ (Recomendado)
- **12 ejercicios completos**
- Interfaz avanzada con categorización
- Sistema de progreso completo
- Controles expandidos

### final_count_multi_exercises.py
- **5 ejercicios básicos**
- Interfaz simplificada
- Ideal para principiantes

### final_count.py
- **1 ejercicio (mano abierta)**
- Versión más simple
- Para pruebas iniciales

---

*¡Gracias por usar Flexia! 💪 Tu recuperación es importante para nosotros.*
