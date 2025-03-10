package com.example.matrixrpg.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.matrixrpg.errorPrint

class Player(
    var x: Int,
    var y: Int,
    val name: String,
    hp: Int,
    var dmg: Int,
    var lvl: Int,
    var xp: Int,
    var gold: Int
) {

    var hp by mutableStateOf(hp)

    // Метод для атаки врага
    fun attack(enemy: Enemy) {

        enemy.takeDamage(dmg)
        if (!enemy.isAlive()) {

        }
    }

    // Методы перемещения (остаются без изменений)
    fun moveUp(map: Map) {
        if (x > 0) {
            x--
        } else {
            errorPrint()
        }
    }

    fun moveDown(map: Map) {
        if (x < 4) {
            x++
        } else {
            errorPrint()
        }
    }

    fun moveLeft(map: Map) {
        if (y > 0) {
            y--
        } else {
            errorPrint()
        }
    }

    fun moveRight(map: Map) {
        if (y < 4) {
            y++
        } else {
            errorPrint()
        }
    }

    fun takeDamage(damage: Int) {
        hp -= damage
        if (hp < 0) hp = 0
    }

    fun isAlive(): Boolean {
        return hp > 0
    }
}