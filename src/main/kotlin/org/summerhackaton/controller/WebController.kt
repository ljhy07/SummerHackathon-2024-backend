package org.summerhackaton.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.summerhackaton.dto.Machines
import org.summerhackaton.model.Mqtt
import org.summerhackaton.dto.MqttRequest
import org.summerhackaton.service.MqttService
import java.util.*

@RestController
class WebController(
    val mqttService: MqttService
) {

    @GetMapping("/api/machinesdata")
    fun getMachinesData()
        = Machines.of(mqttService.findAll())

    @GetMapping("/api/getdata")
    fun getMachineData(
        @RequestParam id: Long
    ): Optional<Mqtt> {
        println(id)
        return mqttService.findById(id)
    }

}
