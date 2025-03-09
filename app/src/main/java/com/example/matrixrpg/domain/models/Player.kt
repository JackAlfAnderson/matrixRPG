package com.example.matrixrpg.domain.models

import com.example.matrixrpg.errorPrint

class Player(
    var x: Int,
    var y: Int,
    val name: String,
    var hp: Int,
    var dmg: Int,
    var lvl: Int,
    var xp: Int,
    var gold: Int
) {
    // Метод для атаки врага
    fun attack(enemy: Enemy) {
        println("$name атакует ${enemy.name} и наносит $dmg урона!")
        enemy.takeDamage(dmg)
        if (!enemy.isAlive()) {
            println("${enemy.name} повержен!")
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
}