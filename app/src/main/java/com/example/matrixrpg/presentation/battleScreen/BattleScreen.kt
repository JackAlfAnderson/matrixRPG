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
    onAbility: () -> Unit,
    onUseItem: () -> Unit,
    onPlayerDead: () -> Unit
) {
    var isPlayerTurn by remember { mutableStateOf(true) }
    var isActionSelected by remember { mutableStateOf(false) }

    val playerHealthProgress = remember { mutableStateOf((player.hp.toFloat() / player.maxHp.toFloat()) * 100) }
    val animatedPlayerProgress by animateFloatAsState(targetValue = playerHealthProgress.value)

    val enemyHealthProgress = remember { mutableStateOf(enemy.hp / 100f) }
    val animatedEnemyProgress by animateFloatAsState(targetValue = enemyHealthProgress.value)

    var playerAnimationStart by remember { mutableStateOf(false) }
    var enemyAnimationStart by remember { mutableStateOf(false) }

    var playerDamageAmountStart by remember { mutableStateOf(false) }
    var enemyDamageAmountStart by remember { mutableStateOf(false) }

    var isPlayerDamageAnim by remember { mutableStateOf(false) }
    val playerAnimationOfDamageAmount by animateDpAsState(
        if (isPlayerDamageAnim) 80.dp else 40.dp,
        tween(200)
    )
    var isEnemyDamageAnim by remember { mutableStateOf(false) }
    val enemyAnimationOfDamageAmount by animateDpAsState(
        if (isEnemyDamageAnim) 80.dp else 40.dp,
        tween(200)
    )

    var isPlayerAttacking by remember { mutableStateOf(false) }
    val playerPaddingAnimation by animateDpAsState(
        if (isPlayerAttacking) 100.dp else 180.dp,
        tween(200)
    )

    var isEnemyAttacking by remember { mutableStateOf(false) }
    val enemyPaddingAnimation by animateDpAsState(
        if (isEnemyAttacking) 100.dp else 180.dp,
        tween(200)
    )
    LaunchedEffect(playerDamageAmountStart) {
        isPlayerDamageAnim = true
        delay(200)
        isPlayerDamageAnim = false
    }
    LaunchedEffect(enemyDamageAmountStart) {
        isEnemyDamageAnim = true
        delay(200)
        isEnemyDamageAnim = false
    }

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
        playerHealthProgress.value = player.hp.toFloat() / player.maxHp.toFloat()
    }

    // Обновление прогресса HP врага
    LaunchedEffect(enemy.hp) {
        enemyHealthProgress.value = enemy.hp.toFloat() / enemy.maxHp.toFloat()
    }
    LaunchedEffect(isPlayerTurn) {
        if (!isPlayerTurn) {
            delay(1000) // Задержка перед ходом врага
            enemy.updatePoison() // Применяем яд
            player.updateAbilities() // Обновляем состояния игрока

            if (player.hp > 0) {
                enemy.attack(player)
                enemyDamageAmountStart = !enemyDamageAmountStart
                enemyAnimationStart = !enemyAnimationStart
            }
            if (player.hp <= 0) {
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
            Box(Modifier.padding(bottom = playerAnimationOfDamageAmount)) {
                if (isPlayerDamageAnim){
                    Text(player.howMuchDamahe.toString()+"!", )
                }
            }
            Box(Modifier.padding(bottom = enemyAnimationOfDamageAmount)) {
                if (isEnemyDamageAnim){
                    Text(enemy.dmg.toString()+"!", )
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
                when (player.icon){
                    R.drawable.charactersquare -> {
                        Icon(
                            painter = painterResource(R.drawable.playerbattleicon),
                            null,
                            tint = Color.Unspecified
                        )
                    }
                    R.drawable.charactertriangle -> {
                        Icon(
                            painter = painterResource(R.drawable.trianglebattlescreen),
                            null,
                            tint = Color.Unspecified
                        )
                    }
                    R.drawable.charactercircle -> {
                        Icon(
                            painter = painterResource(R.drawable.circlebattlescreen),
                            null,
                            tint = Color.Unspecified
                        )
                    }
                    else -> {
                        Icon(
                            painter = painterResource(R.drawable.playerbattleicon),
                            null,
                            tint = Color.Unspecified
                        )
                    }

                }

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
                                progress = {animatedPlayerProgress},
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
                                playerDamageAmountStart = !playerDamageAmountStart
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
                                onAbility()
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
                    Text("ABILITY", color = if (isPlayerTurn && !isActionSelected) Color(0xFFE9EAD3) else Color.Gray)
                }
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
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
            0, 0, "", 0, 0, 0, 0, 0, gold = 0,icon = R.drawable.charactersquare
        ),
        Enemy(0, 0, "", 0, 0, 0),
        {},
        {},
        {},
        {},
    )
}