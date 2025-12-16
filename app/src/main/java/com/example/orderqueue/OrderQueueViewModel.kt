package com.example.orderqueue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class OrderQueueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderQueueUiState())
    val uiState: StateFlow<OrderQueueUiState> = _uiState.asStateFlow()

    private var producerJob: Job? = null
    private var consumerJob: Job? = null

    // Обработка нажатия кнопки Start/Pause
    fun toggleProcessing() {
        val currentState = _uiState.value
        if (currentState.isConsumerRunning) {
            pauseConsumer()
        } else {
            startConsumer()
            // Продюсер запускается один раз и работает постоянно
            if (producerJob == null) {
                startProducer()
            }
        }
    }

    private fun startProducer() {
        producerJob = viewModelScope.launch {
            _uiState.update { it.copy(isProducerActive = true) }
            while (isActive) {
                delay(250) // Эмиссия каждые 250мс
                addOrder()
            }
        }
    }

    private fun startConsumer() {
        if (consumerJob?.isActive == true) return
        
        _uiState.update { it.copy(isConsumerRunning = true) }
        consumerJob = viewModelScope.launch {
            while (isActive) {
                // Случайная задержка 100-250мс
                val processingTime = Random.nextLong(100, 251)
                delay(processingTime)
                removeOrder()
            }
        }
    }

    private fun pauseConsumer() {
        consumerJob?.cancel()
        consumerJob = null
        _uiState.update { it.copy(isConsumerRunning = false) }
    }

    private fun addOrder() {
        _uiState.update { state ->
            // Нет ограничения сверху, может быть overflow
            state.copy(queueSize = state.queueSize + 1)
        }
    }

    private fun removeOrder() {
        _uiState.update { state ->
            // Не уходим в минус
            val newSize = (state.queueSize - 1).coerceAtLeast(0)
            state.copy(queueSize = newSize)
        }
    }
}
