package com.example.orderqueue

data class OrderQueueUiState(
    val queueSize: Int = 0,
    val capacity: Int = 25,
    val isConsumerRunning: Boolean = false,
    val isProducerActive: Boolean = false // Флаг, запущен ли вообще продюсер (он не останавливается)
) {
    // Вспомогательное свойство для процента заполнения
    val fillPercentage: Float
        get() = if (capacity > 0) queueSize.toFloat() / capacity else 0f
}
