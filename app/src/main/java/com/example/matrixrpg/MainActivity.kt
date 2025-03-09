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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.matrixrpg.domain.models.Enemy
import com.example.matrixrpg.domain.models.Map
import com.example.matrixrpg.domain.models.Player
import com.example.matrixrpg.presentation.map.WorldMap
import com.example.matrixrpg.ui.theme.MatrixRPGTheme
import com.example.matrixrpg.ui.theme.background

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatrixRPGTheme {
                //matrix
                var worldMap by remember {
                    mutableStateOf(
                        mutableListOf(
                            mutableListOf(0,0,0,0,0),
                            mutableListOf(0,0,0,0,0),
                            mutableListOf(0,0,0,0,0),
                            mutableListOf(0,0,0,0,0),
                            mutableListOf(0,0,0,0,0),
                        )
                    )
                }

                //livings values
                val playerValue by remember {
                    mutableStateOf(
                        1
                    )
                }
                val enemyValue by remember {
                    mutableStateOf(
                        2
                    )
                }
                //player
                var player by remember {
                    mutableStateOf(
                        Player(
                            x = 0,
                            y = 0,
                            name = "Player",
                            10,
                            dmg = 10,
                            lvl = 10,
                            xp = 10,
                            gold = 10
                        )
                    )
                }
                //enemy
                var enemy by remember {
                    mutableStateOf(
                        Enemy(
                            x = 2,
                            y = 2,
                            name = "Chert",
                            10,
                            10
                        )
                    )
                }
                var enemy1 by remember {
                    mutableStateOf(
                        Enemy(
                            x = 3,
                            y = 2,
                            name = "Chert",
                            10,
                            10
                        )
                    )
                }
                var enemy2 by remember {
                    mutableStateOf(
                        Enemy(
                            x = 2,
                            y = 4,
                            name = "Chert",
                            10,
                            10
                        )
                    )
                }

                // updateMap
                fun updateWorldMap(x: Int, y: Int, value: Int) {
                    val newMap = worldMap.mapIndexed { rowIndex, row ->
                        if (rowIndex == x) row.toMutableList().also { it[y] = value }
                        else row.toMutableList()
                    }.toMutableList()
                    worldMap = newMap
                }
                fun updateLivings(){
                    updateWorldMap(enemy.x, enemy.y, 2)
                    updateWorldMap(enemy1.x, enemy1.y, 2)
                    updateWorldMap(enemy2.x, enemy2.y, 2)
                }
                LaunchedEffect(Unit) {
                    updateWorldMap(player.x, player.y, 1)
                    updateLivings()
                }
                //other values

                val interactionSource = remember { MutableInteractionSource() }

                Dialog(
                    onDismissRequest = {

                    }
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF333536)
                        ),
                        modifier = Modifier
                            .size(350.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Вы наткнулись на врага!", color = Color.White, fontSize = 28.sp, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(40.dp))
                            Text("Готовьтесь к бою!",color = Color.White, fontSize = 22.sp)
                            Spacer(Modifier.height(40.dp))
                            Button(
                                onClick = {

                                },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5C5F5B)
                                )
                            ) {
                                Text("Атака")
                            }
                        }
                    }

                }
                Column(
                    Modifier.fillMaxSize().background(background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column {
                        Box (contentAlignment = Alignment.Center){
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
                                // draw worldMap
                                //every row
                                worldMap.forEachIndexed { rowIndex, row ->
                                    Row {
                                        // every column
                                        row.forEachIndexed { colIndex, cellValue ->
                                            Box(
                                                contentAlignment = Alignment.Center
                                            ) {

                                                Icon(
                                                    painter = painterResource(R.drawable.maprectangle),
                                                    contentDescription = null,
                                                    tint = Color.Unspecified
                                                )
                                                // isPlayerHere
                                                if (playerValue == worldMap[rowIndex][colIndex]) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.playersquare),
                                                        contentDescription = null,
                                                        tint = Color.Unspecified,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                                // isEnemyHere
                                                if (enemyValue == worldMap[rowIndex][colIndex]){
                                                    Icon(
                                                        painter = painterResource(R.drawable.enemy),
                                                        contentDescription = null,
                                                        tint = Color.Unspecified,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                                if (
                                                    enemyValue == worldMap[rowIndex][colIndex] &&
                                                    playerValue  == worldMap[rowIndex][colIndex]){
                                                    
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //buttons
                        Box {
                            Box(modifier = Modifier.padding(top = 60.dp, start = 40.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.buttonleft),
                                    null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .clickable (
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            updateWorldMap(player.x, player.y, 0)
                                            updateLivings()
                                            if (player.y > 0) {
                                                player.y--
                                            } else {

                                            }
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
                                        .clickable  (
                                            interactionSource = interactionSource,
                                            indication = null
                                        ){
                                            updateWorldMap(player.x, player.y, 0)
                                            updateLivings()

                                            if (player.y < 4) {
                                                player.y++
                                            } else {

                                            }
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
                                        .clickable (
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            updateWorldMap(player.x, player.y, 0)
                                            updateLivings()

                                            if (player.x > 0) {
                                                player.x--
                                            } else {

                                            }
                                            updateWorldMap(player.x, player.y, 1)
                                        }
                                )
                            }

                            Box(
                                modifier = Modifier.padding(top = 110.dp, start = 120.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.buttondown),
                                    null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .clickable (
                                            interactionSource = interactionSource,
                                            indication = null
                                        ){
                                            updateWorldMap(player.x, player.y, 0)
                                            updateLivings()

                                            if (player.x < 4) {
                                                player.x++
                                            } else {

                                            }
                                            updateWorldMap(player.x, player.y, 1)

                                        }
                                )
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
