package com.example.matrixrpg.domain.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import com.example.matrixrpg.errorPrint
import kotlin.math.max
import kotlin.math.min

class Player(
    var x: Int,
    var y: Int,
    val name: String,
    val maxHp: Int,
    hp: Int = maxHp,
    var dmg: Int,
    var lvl: Int,
    var xp: Int,
    var gold: Int,
    var ability: String = "None",
    var icon: Int
) {
    var hp by mutableStateOf(hp)
    var isBerserk by mutableStateOf(true) // Режим берсерка
    var berserkDuration by mutableStateOf(0) // Длительность берсерка
    var isPoisoned by mutableStateOf(false) // Яд
    var poisonDuration by mutableStateOf(0) // Длительность яда
    var isShielded by mutableStateOf(false) // Щит
    var shieldAmount by mutableStateOf(0) // Количество поглощаемого урона
    var isStunned by mutableStateOf(false) // Оглушение
    var isCriticalStrikeActive by mutableStateOf(false) // Критический удар
    var criticalStrikeDuration by mutableStateOf(0) // Длительность критического удара
    var howMuchDamahe by mutableStateOf(0) // Сколько урона нанесено

    // Метод для атаки врага
    fun attack(enemy: Enemy) {
        var damage = dmg
        if (isBerserk) {
            damage = (damage * 1.4).toInt() // Увеличение урона на 40%
            Log.d("needu" , damage.toString())
        }
        if (isCriticalStrikeActive) {
            val chance = (0..100).random()
            if (chance <= 20) {
                damage *= 2 // Двойной урон
            }
        }
        howMuchDamahe = damage
        enemy.takeDamage(damage)
    }

    // Метод для использования способности
    fun useAbility(enemy: Enemy) {
        Log.d("needu" , ability)
        when (ability) {
            "Poison" -> poison(enemy)
            "Berserk" -> {
                berserkMode()
                Log.d("needu", "berserkchoosen")
            }
            "Heal" -> heal()
            "Shield" -> shield()
            "Stun" -> stun(enemy)
            "CriticalStrike" -> criticalStrike()
        }
    }

    // Яд
    fun poison(enemy: Enemy) {
        enemy.isPoisoned = true
        enemy.poisonDuration = 3
        enemy.poisonDamage = 5
    }

    // Режим берсерка
    fun berserkMode() {
        if (!isBerserk) { // Активируем берсерк только если он не активен
            Log.d("needu" , "Berserk is active")
            isBerserk = true
            berserkDuration = 2
        }
    }

    // Лечение
    fun heal() {
        val healAmount = hp * 0.3
        hp = min(hp + healAmount.toInt(), maxHp) // Используем maxHp вместо 100
    }

    // Щит
    fun shield() {
        isShielded = true
        shieldAmount = 20
    }

    // Оглушение
    fun stun(enemy: Enemy) {
        enemy.isStunned = true
    }

    // Критический удар
    fun criticalStrike() {
        isCriticalStrikeActive = true
        criticalStrikeDuration = 3
    }

    // Обновление состояний
    fun updateAbilities() {
        if (berserkDuration > 0) {
            berserkDuration--
        } else {
            isBerserk = false
        }
        if (isCriticalStrikeActive && criticalStrikeDuration > 0) {
            criticalStrikeDuration--
        } else {
            isCriticalStrikeActive = false
        }
    }

    // Метод для получения урона
    fun takeDamage(damage: Int) {
        if (isShielded) {
            val remainingShield = max(0, shieldAmount - damage)
            val remainingDamage = max(0, damage - shieldAmount)
            shieldAmount = remainingShield
            hp -= remainingDamage
            if (shieldAmount == 0) {
                isShielded = false
            }
        } else {
            hp -= damage
        }
        if (hp < 0) hp = 0
    }

    fun isAlive(): Boolean {
        return hp > 0
    }
}