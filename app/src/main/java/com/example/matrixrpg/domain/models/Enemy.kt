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
    fun attack(player: Player) {
        player.takeDamage(dmg)
        if (!player.isAlive()) {

        }
    }

    var hp by mutableStateOf(hp)

    fun isAlive(): Boolean {
        return hp > 0
    }

    fun takeDamage(damage: Int) {
        hp -= damage
        if (hp < 0) hp = 0
    }
}