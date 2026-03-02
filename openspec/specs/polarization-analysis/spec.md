# polarization-analysis Specification

## Propósito
Analizar la distribución de la intensidad del entrenamiento en periodos de 7 y 28 días para evaluar la adherencia a modelos de entrenamiento efectivos (como el Polarizado 80/20) y prevenir el agotamiento crónico por exceso de tiempo en la "Zona Gris" (Z3).

## Requisitos

### Requisito: Análisis de Distribución de Zonas 80/20
El sistema DEBE agregar el tiempo total en zonas de todas las actividades en ventanas de 7 y 28 días para calcular la ratio de distribución.
- **Modelo Polarizado (80/20)**: ~80% en Z1/Z2 (Baja Intensidad) y ~20% en Z4/Z5 (Alta Intensidad).
- **Modelo de Umbral/Piramidal**: Distribución con mayor peso en Z3/Z4.

#### Escenario: Cálculo de ratio semanal
- **DADO** un historial de actividades en los últimos 7 días.
- **CUANDO** el usuario visualiza el dashboard de analíticas.
- **ENTONCES** debe mostrar una gráfica de barras o pastel indicando la distribución porcentual del tiempo total acumulado.

### Requisito: Detección de "Zona Gris" (Grey Zone Detection)
El sistema DEBE alertar al usuario si el tiempo acumulado en la Zona 3 (Tempo/Umbral) supera un umbral crítico (ej. >40% del tiempo total).
- **Justificación**: La Zona 3 genera una fatiga significativa sin proporcionar los beneficios aeróbicos profundos de la Z2 ni los beneficios de potencia de la Z4/Z5.

#### Escenario: Alerta de fatiga ineficiente
- **CUANDO** el análisis de 28 días muestra un predominio de Z3.
- **ENTONCES** el sistema debe sugerir una reestructuración del entrenamiento: "Estás pasando demasiado tiempo en la 'Zona Gris'. Considera hacer tus sesiones suaves más suaves (Z2) y tus sesiones intensas más intensas (Z4/Z5)."

### Requisito: Visualización de Tendencia de Polarización
El sistema DEBE mostrar la evolución de la ratio de polarización a lo largo del tiempo para verificar si el atleta está mejorando su disciplina de entrenamiento.

#### Escenario: Gráfico de tendencia mensual
- **CUANDO** el usuario consulta su resumen mensual.
- **ENTONCES** debe visualizar cómo ha cambiado su porcentaje de Z1/Z2 vs Z4/Z5 semana tras semana.
