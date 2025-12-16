package com.example.orderqueue

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun OrderQueueProgressBar(
    percentage: Float, // 1.0 = 100%, 1.2 = 120%
    modifier: Modifier = Modifier
) {
    // Анимация для состояния перегрузки (сдвиг паттерна)
    val infiniteTransition = rememberInfiniteTransition(label = "overflow")
    val animatedShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f, // Сдвиг на 20 пикселей
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "stripeShift"
    )

    Canvas(modifier = modifier.fillMaxWidth().height(30.dp)) {
        val width = size.width
        val height = size.height
        
        // Рисуем фон (серый трек)
        drawRect(
            color = Color.LightGray.copy(alpha = 0.3f),
            size = size
        )

        // Определяем цвет заполнения
        val barColor = when {
            percentage > 1.0f -> Color(0xFF800080) // Фиолетовый для перегрузки (Overload)
            percentage > 0.66f -> Color.Red
            percentage > 0.33f -> Color(0xFFFFC107) // Amber/Yellow
            else -> Color.Green
        }

        // Ширина заполненной части (ограничиваем шириной экрана для визуализации, 
        // но если >100%, рисуем на всю ширину)
        val fillWidth = (width * percentage).coerceAtMost(width)

        drawRect(
            color = barColor,
            size = Size(fillWidth, height)
        )

        // Если перегрузка (>100%), рисуем анимированные полоски поверх
        if (percentage > 1.0f) {
            val stripeWidth = 10f
            val gap = 10f
            // Рисуем наклонные линии
            // Используем clipRect, чтобы не рисовать за пределами бара (хотя тут бар на всю ширину)
            var x = -20f + animatedShift // Начало со сдвигом для анимации
            while (x < width) {
                drawLine(
                    color = Color.White.copy(alpha = 0.3f),
                    start = Offset(x, 0f),
                    end = Offset(x + stripeWidth, height),
                    strokeWidth = 5f
                )
                x += stripeWidth + gap
            }
        }
        
        // Рамка
        drawRect(
            color = Color.Gray,
            style = Stroke(width = 2f),
            size = size
        )
    }
}
