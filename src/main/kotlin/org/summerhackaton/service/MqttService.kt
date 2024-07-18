package org.summerhackaton.service

import jakarta.transaction.Transactional
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.summerhackaton.dto.MqttMessage
import org.summerhackaton.dto.MqttRequest
import org.summerhackaton.model.Mqtt
import java.time.LocalDateTime
import java.util.*

@Service
class MqttService(
    val mqttRepository: MqttRepository,
) {
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface MqttOutboundGateway{
        fun sendToMqtt(@Payload data: String)
    }

    @MessagingGateway(defaultRequestChannel = "tempMqttInboundChannel")
    interface TempMqttInboundGateway{
        fun sendToMqtt(@Payload data: String)
    }

    @MessagingGateway(defaultRequestChannel = "turbidityMqttInboundChannel")
    interface TurbidityMqttInboundGateway{
        fun sendToMqtt(@Payload data: String)
    }

    fun save(mqtt: Mqtt): Mqtt
        = mqttRepository.save(mqtt)

    fun findAll(): List<Mqtt>
        = mqttRepository.findAll()

    fun findById(id: Long): Optional<Mqtt>
        = mqttRepository.findById(id)

    @Transactional
    fun updateDataByIdToTemp(temp: Float) {
        val optionalMqttData: Optional<Mqtt> = mqttRepository.findById(1)
        if (optionalMqttData.isPresent) {
            val mqttData = optionalMqttData.get()
            temp?.let { mqttData.temp = it }
            mqttData.updatedAt = LocalDateTime.now()
            mqttRepository.save(mqttData)
        } else {
            println("No data found with ID: 1")
        }
    }

    @Transactional
    fun updateDataByIdToTurbidity(turbidity: Float) {
        val optionalMqttData: Optional<Mqtt> = mqttRepository.findById(1)
        if (optionalMqttData.isPresent) {
            val mqttData = optionalMqttData.get()
            turbidity?.let { mqttData.turbidity = it }
            mqttData.updatedAt = LocalDateTime.now()
            mqttRepository.save(mqttData)
        } else {
            println("No data found with ID: 1")
        }
    }
}
