package org.summerhackaton.config

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.summerhackaton.service.MqttListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.summerhackaton.service.MqttService
import java.time.LocalDateTime

@Configuration
class MqttConfig(
//    val mqttService: MqttService
) {

    companion object {
        private val brokerUrl = "tcp://broker.mqtt-dashboard.com"
//        private val brokerUrl = "tcp://localhost:1883"
        private val outTopic = "bssm/redtide"
        private val inTopic = "bssm/greentide"
        private val pubClientId = "mqtt-ljhy-pub"
        private val subClientId = "mqtt-ljhy-sub"
    }

    @Bean
    fun mqttPahoClientFactory()
        = DefaultMqttPahoClientFactory().apply {
            connectionOptions = MqttConnectOptions().apply {
                serverURIs = arrayOf(brokerUrl)
            }
        }

    @Bean
    fun mqttOutboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    fun mqttOutBound(): MessageHandler
            = MqttPahoMessageHandler(pubClientId, mqttPahoClientFactory()).apply {
                setAsync(true)
                setDefaultTopic(outTopic)
            }

    @Bean
    fun mqttInboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun inbound(): MqttPahoMessageDrivenChannelAdapter {
        val adapter = MqttPahoMessageDrivenChannelAdapter(
            brokerUrl,
            subClientId,
            mqttPahoClientFactory(),
            inTopic,
        )

        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = mqttInboundChannel()
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    suspend fun mqttInbound(): MessageHandler {
        return MessageHandler { msg: Message<*> ->
            println("Payload: ${msg}")
        }
    }
}
