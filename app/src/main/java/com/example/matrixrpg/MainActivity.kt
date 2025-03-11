package com.example.matrixrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matrixrpg.domain.models.Enemy
import com.example.matrixrpg.domain.models.Map
import com.example.matrixrpg.domain.models.Player
import com.example.matrixrpg.presentation.battleScreen.BattleScreen
import com.example.matrixrpg.presentation.map.WorldMap
import com.example.matrixrpg.ui.theme.MatrixRPGTheme
import com.example.matrixrpg.ui.theme.background

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatrixRPGTheme {

                val navController = rememberNavController()

                // Состояния
                var worldMap by remember {
                    mutableStateOf(
                        mutableListOf(
                            mutableListOf(0, 0, 0, 0, 0),
                            mutableListOf(0, 0, 0, 0, 0),
                            mutableListOf(0, 0, 0, 0, 0),
                            mutableListOf(0, 0, 0, 0, 0),
                            mutableListOf(0, 0, 0, 0, 0)
                        )
                    )
                }
                var isDialogShow by remember { mutableStateOf(false) }
                var isVictoryDialogVisible by remember { mutableStateOf(false) }
                var isWastedDialogVisible by remember { mutableStateOf(false) }

                val playerValue by remember { mutableStateOf(1) }
                val enemyValue by remember { mutableStateOf(2) }
                var currentEnemy by remember { mutableStateOf(mutableListOf<Enemy>()) }

                var abilities = listOf(
                    "Poison",
                    "Berserk",
                    "Heal",
                    "Shield",
                    "Stun",
                    "CriticalStrike"
                )

                var player by remember {
                    mutableStateOf(
                        Player(
                            x = 0,
                            y = 0,
                            name = "Player",
                            100,
                            dmg = 20,
                            lvl = 10,
                            xp = 10,
                            gold = 10,
                            ability = abilities[0]
                        )
                    )
                }
                // Функция для использования способности
                fun useAbility() {
                    player.useAbility(currentEnemy[0])
                }

                var listOfEnemies by remember {
                    mutableStateOf(
                        mutableListOf(
                            Enemy(x = 2, y = 2, name = "Chert", 100, 10),
                            Enemy(x = 3, y = 2, name = "Chert", 100, 10),
                            Enemy(x = 2, y = 4, name = "Chert", 100, 10)
                        )
                    )
                }

                // Функции для обновления карты
                fun updateWorldMap(x: Int, y: Int, value: Int) {
                    val newMap = worldMap.mapIndexed { rowIndex, row ->
                        if (rowIndex == x) row.toMutableList().also { it[y] = value }
                        else row.toMutableList()
                    }.toMutableList()
                    worldMap = newMap
                }

                fun updateLivings() {
                    listOfEnemies.forEach {
                        updateWorldMap(it.x, it.y, 2)
                    }
                }

                // Функция для начала битвы
                fun startBattle() {
                    navController.navigate("battleScreen")
                }

                // Функция для атаки
                fun attack() {
                    currentEnemy[0].let { enemy ->
                        player.attack(enemy)
                        if (!enemy.isAlive()) {
                            listOfEnemies = listOfEnemies.filter { it != enemy }.toMutableList()
                            isVictoryDialogVisible = true
                        }
                    }
                }

                //Функция при смерти игрока
                fun playerDead() {
                    updateWorldMap(player.x, player.y, 0)
                    player.hp = 100
                    player.x = 0
                    player.y = 0
                    //сделать логику создания нового персонажа при каждом новом заходе и при каждом перезапуске
                    updateWorldMap(player.x, player.y, 1)
                    listOfEnemies = mutableListOf(
                        Enemy(x = 2, y = 2, name = "Chert", 100, 10),
                        Enemy(x = 4, y = 2, name = "Chert", 100, 10),
                        Enemy(x = 4, y = 4, name = "Chert", 100, 10)
                    )
                    updateLivings()
                    isWastedDialogVisible = true
                }

                // Функция для возврата на карту
                fun backToMap() {
                    listOfEnemies.removeIf { enemy ->
                        enemy.x == currentEnemy[0].x && enemy.y == currentEnemy[0].y
                    }
                    currentEnemy.clear()
                    isDialogShow = false
                    player.hp = 100
                    updateLivings()
                    navController.navigate("worldMap")

                }

                LaunchedEffect(Unit) {
                    updateWorldMap(player.x, player.y, 1)
                    updateLivings()
                }

                val interactionSource = remember { MutableInteractionSource() }

                Column(Modifier.fillMaxSize().background(background)) {
                    NavHost(
                        navController,
                        startDestination = "worldMap"
                    ) {
                        composable(route = "worldMap") {
                            if (isDialogShow) {
                                Dialog(onDismissRequest = {}) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF333536)),
                                        modifier = Modifier.size(350.dp),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "Вы наткнулись на врага!",
                                                color = Color.White,
                                                fontSize = 28.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(Modifier.height(40.dp))
                                            Text("Готовьтесь к бою!", color = Color.White, fontSize = 22.sp)
                                            Spacer(Modifier.height(40.dp))
                                            Button(
                                                onClick = {
                                                    navController.navigate("battleScreen")
                                                    isDialogShow = false
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 60.dp)
                                                    .height(50.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5F5B))
                                            ) {
                                                Text("Атака")
                                            }
                                        }
                                    }
                                }
                            }
                            Column(
                                Modifier.fillMaxSize().background(background),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(R.drawable.worldmap),
                                            null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(325.dp, 320.dp).padding(top = 3.dp, end = 2.dp)
                                        )
                                        Column(
                                            Modifier.background(Color(0xFF1B1A1D)),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            worldMap.forEachIndexed { rowIndex, row ->
                                                Row {
                                                    row.forEachIndexed { colIndex, cellValue ->
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(
                                                                painter = painterResource(R.drawable.maprectangle),
                                                                contentDescription = null,
                                                                tint = Color.Unspecified
                                                            )
                                                            if (playerValue == worldMap[rowIndex][colIndex]) {
                                                                Icon(
                                                                    painter = painterResource(R.drawable.playersquare),
                                                                    contentDescription = null,
                                                                    tint = Color.Unspecified,
                                                                    modifier = Modifier.size(24.dp)
                                                                )
                                                                val isEnemyOnPlayerCell = remember {
                                                                    derivedStateOf {
                                                                        listOfEnemies.any { enemy ->
                                                                            enemy.x == colIndex && enemy.y == rowIndex
                                                                        }
                                                                    }
                                                                }
                                                                LaunchedEffect(isEnemyOnPlayerCell.value) {
                                                                    if (isEnemyOnPlayerCell.value) {
                                                                        currentEnemy.clear()
                                                                        currentEnemy.add(listOfEnemies.first { it.x == colIndex && it.y == rowIndex })
                                                                        isDialogShow = true
                                                                    }
                                                                }
                                                            }
                                                            listOfEnemies.forEach { enemy ->
                                                                if (enemy.x == colIndex && enemy.y == rowIndex) {
                                                                    Icon(
                                                                        painter = painterResource(R.drawable.enemy),
                                                                        contentDescription = null,
                                                                        tint = Color.Unspecified,
                                                                        modifier = Modifier.size(24.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Box {
                                        Box(modifier = Modifier.padding(top = 60.dp, start = 40.dp)) {
                                            Icon(
                                                painter = painterResource(R.drawable.buttonleft),
                                                null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                    ) {
                                                        updateWorldMap(player.x, player.y, 0)
                                                        updateLivings()
                                                        if (player.y > 0) player.y--
                                                        updateWorldMap(player.x, player.y, 1)
                                                    }
                                            )
                                        }
                                        Box(modifier = Modifier.padding(top = 60.dp, start = 200.dp)) {
                                            Icon(
                                                painter = painterResource(R.drawable.buttonright),
                                                null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                    ) {
                                                        updateWorldMap(player.x, player.y, 0)
                                                        updateLivings()
                                                        if (player.y < 4) player.y++
                                                        updateWorldMap(player.x, player.y, 1)
                                                    }
                                            )
                                        }
                                        Box(modifier = Modifier.padding(top = 10.dp, start = 120.dp)) {
                                            Icon(
                                                painter = painterResource(R.drawable.buttonup),
                                                null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                    ) {
                                                        updateWorldMap(player.x, player.y, 0)
                                                        updateLivings()
                                                        if (player.x > 0) player.x--
                                                        updateWorldMap(player.x, player.y, 1)
                                                    }
                                            )
                                        }
                                        Box(modifier = Modifier.padding(top = 110.dp, start = 120.dp)) {
                                            Icon(
                                                painter = painterResource(R.drawable.buttondown),
                                                null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                    ) {
                                                        updateWorldMap(player.x, player.y, 0)
                                                        updateLivings()
                                                        if (player.x < 4) player.x++
                                                        updateWorldMap(player.x, player.y, 1)
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        composable(route = "battleScreen") {
                            if (currentEnemy.isNotEmpty()) {
                                BattleScreen(
                                    player = player,
                                    enemy = currentEnemy[0],
                                    onAttack = { attack() },
                                    onAbility = { useAbility() },
                                    onUseItem = {},
                                    onPlayerDead = { playerDead() }
                                )
                            }

                            if (isVictoryDialogVisible) {
                                Dialog(onDismissRequest = {}) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF333536)),
                                        modifier = Modifier.size(350.dp),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "Поздравляем!",
                                                color = Color.White,
                                                fontSize = 28.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(Modifier.height(40.dp))
                                            Text("Вы победили!", color = Color.White, fontSize = 22.sp)
                                            Spacer(Modifier.height(40.dp))
                                            Button(
                                                onClick = {
                                                    isVictoryDialogVisible = false
                                                    backToMap()
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 60.dp)
                                                    .height(50.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5F5B))
                                            ) {
                                                Text("Вернуться на карту")
                                            }
                                        }
                                    }
                                }
                            }
                            if (isWastedDialogVisible) {
                                Dialog(onDismissRequest = {}) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF333536)),
                                        modifier = Modifier.size(350.dp),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "Сочувствуем",
                                                color = Color.White,
                                                fontSize = 28.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(Modifier.height(40.dp))
                                            Text("Все ваши жизни были ПОТРАЧЕНЫ", color = Color.White, fontSize = 22.sp)
                                            Spacer(Modifier.height(40.dp))
                                            Button(
                                                onClick = {
                                                    isWastedDialogVisible = false
                                                    navController.navigate("worldMap")
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 60.dp)
                                                    .height(50.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5F5B))
                                            ) {
                                                Text("Начать сначала")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun printMatrix(matrix: List<MutableList<Int>>) {
    for (row in matrix) {
        println(row.joinToString(" "))
    }
}

fun errorPrint() {

}
