package org.summerhackaton.dto

import org.summerhackaton.model.Mqtt

data class Machines (
    val machines: List<MachineDto>
) {
    companion object {
        fun of(mqtts: List<Mqtt>): Machines {
            return Machines(
                machines = mqtts.map(MachineDto::of)
            )
        }
    }
}
