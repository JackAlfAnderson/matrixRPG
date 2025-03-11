package com.example.matrixrpg.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
class Enemy(
    var x: Int,
    var y: Int,
    val name: String,
    hp: Int,
    val dmg: Int
) {
    var hp by mutableStateOf(hp)
    var isPoisoned by mutableStateOf(false) // Яд
    var poisonDuration by mutableStateOf(0) // Длительность яда
    var poisonDamage by mutableStateOf(0) // Урон от яда
    var isStunned by mutableStateOf(false) // Оглушение

    fun attack(player: Player) {
        if (!isStunned) {
            player.takeDamage(dmg)
        } else {
            isStunned = false
        }
    }

    fun takeDamage(damage: Int) {
        hp -= damage
        if (hp < 0) hp = 0
    }

    fun isAlive(): Boolean {
        return hp > 0
    }

    fun updatePoison() {
        if (isPoisoned && poisonDuration > 0) {
            takeDamage(poisonDamage)
            poisonDuration--
        } else {
            isPoisoned = false
        }
    }
}