package org.summerhackaton.service

import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import org.summerhackaton.dto.MqttMessage
import java.time.LocalDateTime

class MqttListener() : MessageHandler {
    override fun handleMessage(msg: Message<*>) {
        println("[${LocalDateTime.now()}] $msg]")
    }
}
