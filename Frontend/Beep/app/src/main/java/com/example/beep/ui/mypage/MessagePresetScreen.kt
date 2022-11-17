package com.example.beep.ui.mypage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.beep.R
import com.example.beep.ui.base.ErrorScreen
import com.example.beep.ui.base.LoadingScreen
import com.example.beep.ui.home.PresetViewModel
import com.example.beep.ui.mypage.introduce.UiState
import com.example.beep.ui.theme.PINK500

@Composable
fun MessagePresetScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel,
    presetViewModel: PresetViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        presetViewModel.getPresetByToken(1)
    }

    when (val currentUiState = presetViewModel.messagePreset) {
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Success -> {
            MessagePresetSuccessScreen(
                navController,
                modifier, presetList = currentUiState.data, presetViewModel
            )
        }
        is UiState.Error -> {
            ErrorScreen()
        }
    }
}

@Composable
fun MessagePresetSuccessScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    presetList: Array<String?>,
    viewModel: PresetViewModel
) {
    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }
    var clickNum = remember { mutableStateOf(0) }     //클릭된 수
    var content = remember { mutableStateOf("${presetList[clickNum.value] ?: ""}") }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 0.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.backbutton_gray),
                        contentDescription = "뒤로가기"
                    )
                }
                Text(
                    modifier = modifier
                        .padding(20.dp, 0.dp, 0.dp, 0.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "메세지 단축키 설정"
                )
            }

//        //수정, 입력 창
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "단축키 ${clickNum.value}번 설정")
                    },
                    text = {
                        TextField(
                            value = content.value,
                            onValueChange = { content.value = it },
                            singleLine = true
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false;
                                //api 요청
                                viewModel.updatePreset(clickNum.value, 1, content.value);
                            }) {
                            Text("설정")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("취소")
                        }
                    }
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (num in 0..9) {
                    Row(
                        modifier = modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                openDialog.value = true;
                                clickNum.value = num;
                                content.value = "${presetList[num] ?: ""}"
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PINK500,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(100),
                            modifier = Modifier
                                .wrapContentSize()
                                .width(50.dp)
                                .height(50.dp),
                        ) {
                            Text(text = "$num", color = Color.White, fontSize = 20.sp)
                        }

                        TextButton(modifier = modifier
                            .width(200.dp)
                            .height(50.dp), onClick = {
                            openDialog.value = true; clickNum.value = num; content.value =
                            "${presetList[num] ?: ""}"
                        }) {
                            Text(
                                text = "${presetList[num] ?: "미등록"}",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}