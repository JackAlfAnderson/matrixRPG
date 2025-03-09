package com.example.matrixrpg.domain.models

class Map() {
    var matrix =
        mutableListOf(
            mutableListOf(0, 0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0, 0)
        )

    // Список врагов
    val enemies = mutableListOf<Enemy>()

    // Добавление врага на карту
    fun addEnemy(enemy: Enemy) {
        enemies.add(enemy)
        matrix[enemy.x][enemy.y] = 2 // 2 будет обозначать врага
    }

    // Обновление карты с учетом врагов
    fun updateMap() {
        // Очищаем карту
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] != 1) { // Не трогаем игрока
                    matrix[i][j] = 0
                }
            }
        }

        // Добавляем врагов
        for (enemy in enemies) {
            if (enemy.isAlive()) {
                matrix[enemy.x][enemy.y] = 2
            }
        }
    }

    fun move(row: Int, col: Int, value: Int) {
        try {
            matrix[row][col] = value
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Вы выходите за пределы карты")
        }
    }
}