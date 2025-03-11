package com.example.matrixrpg.presentation.createPlayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrixrpg.R
import com.example.matrixrpg.ui.theme.background

@Composable
fun CreatePlayerScreen(
    createPlayer: () -> Unit
) {

    val pages = listOf(
        "square",
        "triangle",
        "circle"
    )
    
    val pagerState = rememberPagerState { pages.size }

    var playerName by remember {
        mutableStateOf("Player")
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Icon(
                    painter = painterResource(R.drawable.playercreateback),
                    null,
                    tint = Color.Unspecified
                )
                Box(Modifier.padding(top = 60.dp)) {
                    Box(Modifier.padding(start = 10.dp, top = 60.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.leftslidebutton),
                            null,
                            tint = Color.Unspecified,
                            modifier = Modifier.clickable {

                            }
                        )
                    }
                    Box(Modifier.padding(start = 30.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.characterbackground),
                            null,
                            tint = Color.Unspecified
                        )
                    }
                    Box(Modifier.padding(start = 78.dp, top = 50.dp)) {
                        HorizontalPager(
                            state = pagerState
                        ) { page ->
                            when(pagerState.currentPage){
                                0 -> {
                                    Icon(
                                        painter = painterResource(R.drawable.charactersquare),
                                        null,
                                        tint = Color.Unspecified
                                    )
                                }
                                1 -> {
                                    Icon(
                                        painter = painterResource(R.drawable.charactertriangle),
                                        null,
                                        tint = Color.Unspecified
                                    )
                                }
                                2 -> {
                                    Icon(
                                        painter = painterResource(R.drawable.charactercircle),
                                        null,
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        }

                    }
                    Box(Modifier.padding(start = 190.dp, top = 60.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.rightslidebutton),
                            null,
                            tint = Color.Unspecified,
                            modifier = Modifier.clickable {

                            }
                        )
                    }
                    Box(Modifier.padding(start = 210.dp, top = 20.dp)) {
                        Column {
                            Text("Раса: Квадрат", color = Color.White, fontSize = 12.sp)
                            Text("Способность: Берсерк", color = Color.White, fontSize = 12.sp)
                            Text(
                                "Описание: Какое-то длинное никому не нужное описание",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.padding(end = 25.dp)
                            )
                        }

                    }
                    Box (Modifier.padding(top = 160.dp, start = 80.dp)){
                        TextField(
                            value = playerName,
                            onValueChange = {
                                playerName = it
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color(0xFFE9EAD3),
                                focusedIndicatorColor = Color(0xFFE9EAD3)
                            ),
                            modifier = Modifier.size(
                                150.dp,50.dp
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Icon(
                painter = painterResource(R.drawable.createplayerbutton),
                null,
                tint = Color.Unspecified,
                modifier = Modifier.clickable {

                }
            )
        }
    }
}

@Preview
@Composable
private fun CreatePlayerPreview() {
    CreatePlayerScreen(
        createPlayer = {

        }
    )
}