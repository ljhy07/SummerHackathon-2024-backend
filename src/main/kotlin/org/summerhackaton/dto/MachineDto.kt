package org.summerhackaton.dto

import org.summerhackaton.model.Mqtt

data class MachineDto (
    val id: Long,
    val location: String,
    val isUse: Long,
) {
    companion object {
        fun of(mqtt: Mqtt): MachineDto {
            return MachineDto(
                id = mqtt.id,
                location = mqtt.location,
                isUse = mqtt.isUse,
            )
        }
    }
}
