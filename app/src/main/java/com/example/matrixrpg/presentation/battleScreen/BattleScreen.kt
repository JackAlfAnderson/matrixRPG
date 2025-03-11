package com.example.matrixrpg.presentation.battleScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrixrpg.R
import com.example.matrixrpg.domain.models.Enemy
import com.example.matrixrpg.domain.models.Player
import kotlinx.coroutines.delay

@Composable
fun BattleScreen(
    player: Player,
    enemy: Enemy,
    onAttack: () -> Unit,
    onDefend: () -> Unit,
    onUseItem: () -> Unit,
    onPlayerDead: () -> Unit
) {
    var isPlayerTurn by remember { mutableStateOf(true) }
    var isActionSelected by remember { mutableStateOf(false) }

    val playerHealthProgress = remember { mutableStateOf(player.hp / 100f) }
    val animatedPlayerProgress by animateFloatAsState(targetValue = playerHealthProgress.value)

    val enemyHealthProgress = remember { mutableStateOf(enemy.hp / 100f) }
    val animatedEnemyProgress by animateFloatAsState(targetValue = enemyHealthProgress.value)

    var playerAnimationStart by remember { mutableStateOf(false) }
    var enemyAnimationStart by remember { mutableStateOf(false) }

    var isPlayerAttacking by remember { mutableStateOf(false) }
    val playerPaddingAnimation by animateDpAsState(
        if (isPlayerAttacking) 100.dp else 180.dp,
        tween(200)
    )
    val playerDamageQuantityAnimation by animateDpAsState(
        if (isPlayerAttacking) 80.dp else 40.dp,
        tween(200)
    )


    var isEnemyAttacking by remember { mutableStateOf(false) }
    val enemyPaddingAnimation by animateDpAsState(
        if (isEnemyAttacking) 100.dp else 180.dp,
        tween(200)
    )

    val enemyDamageQuantityAnimation by animateDpAsState(
        if (isEnemyAttacking) 80.dp else 40.dp,
        tween(200)
    )

    LaunchedEffect(playerAnimationStart) {
        isPlayerAttacking = true
        delay(200)
        isPlayerAttacking = false
    }
    LaunchedEffect(enemyAnimationStart) {
        isEnemyAttacking = true
        delay(200)
        isEnemyAttacking = false
    }

    LaunchedEffect(player.hp) {
        playerHealthProgress.value = player.hp / 100f
    }

    LaunchedEffect(enemy.hp) {
        enemyHealthProgress.value = enemy.hp / 100f
    }

    LaunchedEffect(isPlayerTurn) {
        if (!isPlayerTurn) {
            delay(1000) // Задержка перед ходом врага
            if(player.hp > 0){
                enemy.attack(player)
                enemyAnimationStart = !enemyAnimationStart
            }
            if (player.hp <= 0) {
                // Игрок умер, перезапуск карты
                onPlayerDead()
            } else {
                isPlayerTurn = true
                isActionSelected = false
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF343637)),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.battleground),
                        null,
                        tint = Color.Unspecified
                    )
                }
            }
            Box {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isPlayerAttacking){
                        Text("${player.dmg}!")
                        Spacer(Modifier.height(playerDamageQuantityAnimation))
                    }
                    if (isEnemyAttacking){
                        Text("${enemy.dmg}!")
                        Spacer(Modifier.height(enemyDamageQuantityAnimation))
                    }

                }
            }
            Box(Modifier.padding(start = enemyPaddingAnimation)) {
                Icon(
                    painter = painterResource(R.drawable.enemybattleicon),
                    null,
                    tint = Color.Unspecified
                )
            }
            Box(Modifier.padding(end = playerPaddingAnimation)) {
                Icon(
                    painter = painterResource(R.drawable.playerbattleicon),
                    null,
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(Modifier.height(20.dp))

        Box(
            Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        ) {
            Box(Modifier.fillMaxWidth()) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.table),
                        null,
                        tint = Color.Unspecified
                    )
                    Column {
                        Text(
                            player.name,
                            color = Color(0xFFF6F9C2),
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("HP", color = Color.White, fontSize = 12.sp)
                            Spacer(Modifier.width(6.dp))
                            LinearProgressIndicator(
                                progress = { animatedPlayerProgress },
                                modifier = Modifier
                                    .width(58.dp)
                                    .height(9.dp),
                                color = Color(0xFFF6F9C2),
                                trackColor = Color(0xFFADAFA1)
                            )
                        }
                    }
                }
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.table),
                        null,
                        tint = Color.Unspecified
                    )
                    Column {
                        Text(
                            enemy.name,
                            color = Color(0xFFF6F9C2),
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("HP", color = Color.White, fontSize = 12.sp)
                            Spacer(Modifier.width(6.dp))
                            LinearProgressIndicator(
                                progress = { animatedEnemyProgress },
                                modifier = Modifier
                                    .width(58.dp)
                                    .height(9.dp),
                                color = Color(0xFFF6F9C2),
                                trackColor = Color(0xFFADAFA1)
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(40.dp))
        Box(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .clickable(
                            enabled = isPlayerTurn && !isActionSelected,
                            onClick = {
                                playerAnimationStart = !playerAnimationStart
                                onAttack()
                                isActionSelected = true
                                isPlayerTurn = false
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.table),
                        null,
                        tint = Color.Unspecified
                    )
                    Text("ATTACK", color = if (isPlayerTurn && !isActionSelected) Color(0xFFE9EAD3) else Color.Gray)
                }
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .clickable(
                            enabled = isPlayerTurn && !isActionSelected,
                            onClick = {
                                onDefend()
                                isActionSelected = true
                                isPlayerTurn = false
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.table),
                        null,
                        tint = Color.Unspecified
                    )
                    Text("DEFEND", color = if (isPlayerTurn && !isActionSelected) Color(0xFFE9EAD3) else Color.Gray)
                }
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Box(
                    modifier = Modifier
                        .clickable(
                            enabled = isPlayerTurn && !isActionSelected,
                            onClick = {
                                onUseItem()
                                isActionSelected = true
                                isPlayerTurn = false
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.table),
                        null,
                        tint = Color.Unspecified
                    )
                    Text("ITEMS", color = if (isPlayerTurn && !isActionSelected) Color(0xFFE9EAD3) else Color.Gray)
                }
            }
        }
    }
}

@Preview
@Composable
private fun BattleScreenPreview() {
    BattleScreen(
        Player(
            0, 0, "", 0, 0, 0, 0, 0
        ),
        Enemy(0, 0, "", 0, 0),
        {},
        {},
        {},
        {},
    )
}