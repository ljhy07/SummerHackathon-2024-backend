package org.summerhackaton.service

import org.springframework.integration.annotation.MessagingGateway
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.summerhackaton.model.Mqtt
import java.util.*

@Service
class MqttService(
    val mqttRepository: MqttRepository
) {
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface MqttOutboundGateway{
        fun sendToMqtt(@Payload data: String)
    }

    @MessagingGateway(defaultRequestChannel = "mqttInboundChannel")
    interface MqttInboundGateway{
        fun sendToMqtt(@Payload data: String)
    }

    fun save(mqtt: Mqtt): Mqtt
            = mqttRepository.save(mqtt)

    fun findAll(): List<Mqtt>
            = mqttRepository.findAll()

    fun findById(id: Long): Optional<Mqtt>
        = mqttRepository.findById(id)
}
