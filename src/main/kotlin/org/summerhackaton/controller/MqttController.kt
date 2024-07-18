package org.summerhackaton.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.summerhackaton.dto.MqttMessage
import org.summerhackaton.model.Mqtt
import org.summerhackaton.service.MqttService

@RestController
class MqttController(
    private val mqttOutboundGateway: MqttService.MqttOutboundGateway,
    private val tempMqttInboundGateway: MqttService.TempMqttInboundGateway,
    private val turbidityMqttInboundGateway: MqttService.TurbidityMqttInboundGateway,
    val mqttService: MqttService
) {

    companion object {
        private val clientId = "mqtt-ljhy"
    }

    @PostMapping("/mqtt/publish")
    fun publish(
        @RequestBody request: Mqtt
    ){
//        mqttService.save(MqttTest(
//            clientId = clientId,
//            message = message,
//        ))
//        mqttInboundGateway.sendToMqtt(message)

        mqttService.save(request)
        mqttOutboundGateway.sendToMqtt(request.id.toString())
    }

    @PostMapping("/mqtt/unpublish")
    fun unPublish(
        @RequestBody request: MqttMessage
    ){
        val message: String = request.message

//        mqttService.save(MqttTest(
//            clientId = clientId,
//            message = message,
//        ))
        mqttOutboundGateway.sendToMqtt(message)
        tempMqttInboundGateway.sendToMqtt(message)
        turbidityMqttInboundGateway.sendToMqtt(message)
    }

}
