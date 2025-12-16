package com.example.orderqueue

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OrderQueueScreen(
    viewModel: OrderQueueViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Order Queue Outpost",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (state.isProducerActive) "System Active" else "Press Play to start processing orders",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Статистика: X/25 и Проценты
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Queue: ${state.queueSize}/${state.capacity}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(state.fillPercentage * 100).toInt()}%",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Наш кастомный прогресс-бар
        OrderQueueProgressBar(
            percentage = state.fillPercentage,
            modifier = Modifier.height(40.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка управления
        Button(
            onClick = { viewModel.toggleProcessing() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.isConsumerRunning) Color.Red else Color(0xFF4CAF50) // Зеленый
            ),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = if (state.isConsumerRunning) "PAUSE" else "START")
        }
    }
}
