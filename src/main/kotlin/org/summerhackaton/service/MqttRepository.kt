package org.summerhackaton.service

import org.springframework.data.jpa.repository.JpaRepository
import org.summerhackaton.model.Mqtt

interface MqttRepository : JpaRepository<Mqtt, Long> {}
