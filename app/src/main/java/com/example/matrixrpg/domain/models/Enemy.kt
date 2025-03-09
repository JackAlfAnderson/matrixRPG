package com.example.matrixrpg.domain.models

class Enemy(
    var x: Int,
    var y: Int,
    val name: String,
    var hp: Int,
    val dmg: Int
) {
    fun isAlive(): Boolean {
        return hp > 0
    }

    fun takeDamage(damage: Int) {
        hp -= damage
        if (hp < 0) hp = 0
    }
}