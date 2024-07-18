package org.summerhackaton.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

@Configuration
class MqttConfig(
    val mqttService: MqttService
) {

    companion object {
        private val brokerUrl = "tcp://broker.mqtt-dashboard.com"
//        private val brokerUrl = "tcp://localhost:1883"
        private val outTopic = "bssm/redtide"
        private val tempInTopic = "bssm/greentide"
        private val turbidityInTopic = "bssm/camera"
        private val pubClientId = "mqtt-ljhy-pub"
        private val tempSubClientId = "mqtt-ljhy-sub-temp"
        private val turbiditySubClientId = "mqtt-ljhy-sub-turb"
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
    fun tempMqttInboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun tempInbound(): MqttPahoMessageDrivenChannelAdapter {
        val adapter = MqttPahoMessageDrivenChannelAdapter(
            brokerUrl,
            tempSubClientId,
            mqttPahoClientFactory(),
            tempInTopic,
        )
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = tempMqttInboundChannel()

        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "tempMqttInboundChannel")
    suspend fun tempMqttInbound(): MessageHandler {
        return MessageHandler { msg: Message<*> ->
            try {
                mqttService.updateDataByIdToTemp((msg.toString()).toFloat())
                println("Temp Data updated for ID: 1 with Payload: ${msg.payload}")
            } catch (e: Exception) {
                println("Error processing payload: ${msg.payload}")
                e.printStackTrace()
            }
        }
//        return MqttListener()
    }

    @Bean
    fun turbidityMqttInboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun turbidityInbound(): MqttPahoMessageDrivenChannelAdapter {
        val adapter = MqttPahoMessageDrivenChannelAdapter(
            brokerUrl,
            turbiditySubClientId,
            mqttPahoClientFactory(),
            turbidityInTopic,
        )
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = turbidityMqttInboundChannel()

        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "turbidityMqttInboundChannel")
    suspend fun turbidityMqttInbound(): MessageHandler {
        return MessageHandler { msg: Message<*> ->
            try {
                mqttService.updateDataByIdToTurbidity((msg.toString()).toFloat())
                println("Turbidity Data updated for ID: 1 with Payload: ${msg.payload}")
            } catch (e: Exception) {
                println("Error processing payload: ${msg.payload}")
                e.printStackTrace()
            }
        }
//        return MqttListener()
    }
}
