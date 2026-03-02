# metabolic-analysis Specification

## Propósito
Estimar el gasto energético metabólico (Grasas vs. Carbohidratos) y la pérdida de líquidos durante una actividad basándose en la intensidad del esfuerzo (pulso/potencia) y las condiciones ambientales. Esto ayuda al atleta a optimizar su nutrición post-entrenamiento y su estrategia de hidratación.

## Requisitos

### Requisito: Estimación de Oxidación de Sustratos
El sistema DEBE estimar la contribución relativa de Grasas y Carbohidratos al gasto calórico total basándose en el porcentaje de la frecuencia cardíaca máxima (%HRmax) o el porcentaje del FTP (%FTP).
- **Z1/Z2 (Baja Intensidad)**: Predominio de oxidación de grasas (>60%).
- **Z3/Z4 (Umbral)**: Aumento progresivo de oxidación de glucógeno (carbohidratos).
- **Z5 (Alta Intensidad)**: Oxidación casi exclusiva de glucógeno (>90%).

#### Escenario: Cálculo de consumo de glucógeno
- **DADO** una actividad finalizada con datos de pulso y duración.
- **CUANDO** el sistema analiza la intensidad acumulada.
- **ENTONCES** debe mostrar una estimación de gramos de carbohidratos (glucógeno) consumidos (ej. "Consumo est. 120g de CH").

### Requisito: Análisis de Tasa de Sudoración e Hidratación
El sistema DEBE calcular una recomendación de rehidratación basándose en la duración de la actividad, la intensidad (IF) y la temperatura ambiente (`averageTemp`).
- **Factor de Ajuste por Calor**: Incrementar la recomendación si la temperatura supera los 25°C.
- **Factor de Ajuste por Intensidad**: Incrementar la recomendación si el Intensity Factor (IF) es > 0.85.

#### Escenario: Recomendación de hidratación post-actividad
- **CUANDO** el usuario visualiza los detalles de una actividad realizada con calor (>28°C).
- **ENTONCES** el sistema debe mostrar un aviso: "Condiciones de calor detectadas. Recomendación de rehidratación: ~1.2L de agua con electrolitos".

### Requisito: Tiempo de Recuperación Metabólica
El sistema DEBE sugerir un tiempo de espera antes de la siguiente sesión de alta intensidad basándose en el vaciado estimado de glucógeno.

#### Escenario: Sugerencia de recuperación
- **CUANDO** una actividad agota >70% de las reservas estimadas de glucógeno.
- **ENTONCES** el sistema debe sugerir una ventana de recuperación de 24-48h para la reposición total.
