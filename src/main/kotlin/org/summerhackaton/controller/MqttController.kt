package org.summerhackaton.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.summerhackaton.dto.MqttMessage
import org.summerhackaton.service.MqttService

@RestController
class MqttController(
    private val mqttService: MqttService,
    private val mqttOutboundGateway: MqttService.MqttOutboundGateway,
    private val mqttInboundGateway: MqttService.MqttInboundGateway
) {

    companion object {
        private val clientId = "mqtt-ljhy"
    }

    @PostMapping("/mqtt/publish")
    fun publish(
        @RequestBody request: MqttMessage
    ){
        val message: String = request.message

//        mqttService.save(MqttTest(
//            clientId = clientId,
//            message = message,
//        ))
//        mqttInboundGateway.sendToMqtt(message)
        mqttOutboundGateway.sendToMqtt(message)
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
        mqttInboundGateway.sendToMqtt(message)
//        mqttOutboundGateway.sendToMqtt(message)
    }

}
