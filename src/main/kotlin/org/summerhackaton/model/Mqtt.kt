package org.summerhackaton.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "Mqtt")
data class Mqtt (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 1,

    @Column(columnDefinition = "TEXT", nullable = false)
    val location: String,

    @Column(nullable = false)
    var temp: Float,

    @Column(nullable = false)
    var turbidity: Float,

    // 1 = 작동, 2 = 중지, 3 = 미사용
    @Column(nullable = false)
    var isUse: Long = 3,

    // 0: 안전 ~ 3: 위험
    @Column(nullable = false)
    var risk: Long = 0,

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
