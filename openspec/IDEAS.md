# Roadmap de Mejora de Condición Física (PeakFlow)

Este documento contiene las ideas exploradas para transformar PeakFlow en una herramienta de entrenamiento prescriptiva. Cada una de estas ideas se convertirá en una especificación formal (`spec.md`) a medida que decidamos implementarlas.

## 1. Eficiencia Aeróbica (Aerobic Decoupling / Pw:Hr)
**Objetivo**: Medir la fatiga cardiovascular y la solidez de la base aeróbica.
- **Concepto**: Comparar la relación Ritmo/Pulso (o Potencia/Pulso) entre la primera y la segunda mitad de una sesión.
- **Datos requeridos**: `heartRateSeries`, `cadenceSeries` (para filtrar paradas), `distanceKm`, `movingTimeSeconds`.
- **Métrica clave**: % de Desacoplamiento. (<5% = Base sólida).
- **Valor**: Indica al usuario cuándo está listo para aumentar la duración de sus entrenamientos.

## 2. Entrenamiento Polarizado (Análisis 80/20)
**Objetivo**: Maximizar ganancias de fitness evitando el agotamiento crónico.
- **Concepto**: Clasificar el tiempo total semanal en tres grandes zonas: Baja Intensidad (Z1-Z2), Umbral (Z3), Alta Intensidad (Z4-Z5).
- **Datos requeridos**: `heartRateSeries` de todas las actividades de la semana, zonas de pulso del usuario.
- **Métrica clave**: Ratio de distribución semanal.
- **Valor**: Evita que el usuario pase demasiado tiempo en la "zona gris" (Z3), que genera mucha fatiga para poca mejora.

## 3. Salud del Equipo (Gear Lifespan)
**Objetivo**: Prevenir lesiones por el uso de material desgastado (zapatillas/bicicletas).
- **Concepto**: Seguimiento del kilometraje acumulado por cada ítem de equipo registrado en Strava.
- **Datos requeridos**: `gearName`, `gearDistance`.
- **Métrica clave**: Kilometraje restante basado en umbrales configurables (ej. 700km para zapatillas).
- **Valor**: Alerta proactiva: "Tus zapatillas están llegando al final de su vida útil, considera cambiarlas para evitar lesiones".

## 4. Récords de Temporada (Season Bests)
**Objetivo**: Detectar y celebrar picos de forma específicos.
- **Concepto**: Análisis de la "Firma de Poder" o "Firma de Ritmo" en intervalos críticos (1min, 5min, 20min).
- **Datos requeridos**: Actividades históricas, `averagePower`, `maxPower`, `averageSpeed`.
- **Métrica clave**: Peak 1/5/20 min.
- **Valor**: Motivación extra al ver récords no solo "de por vida", sino de la temporada actual.

---
*Nota: Estas ideas están en fase de exploración. Para implementar alguna, primero crearemos una propuesta formal con `/opsx:propose <nombre-del-cambio>`.*
