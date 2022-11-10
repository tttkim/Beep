package com.example.beep.ui.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.runBlocking


@Composable
fun AddCancelBtn(
    changeToAddAddress: () -> Unit,
) {
    Button(
        onClick = changeToAddAddress,
        modifier = Modifier
            .height(40.dp)
            .border(
                width = 1.dp,
                color = Color(android.graphics.Color.parseColor("#7AA8FF")),
                shape = RoundedCornerShape(10.dp)
            ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp, 0.dp),
    ) {
        Text(
            text = "취소",
            fontFamily = galmurinineFont,
            fontSize = 16.sp,
            color = Color(android.graphics.Color.parseColor("#7AA8FF"))
        )
    }
}

@Composable
fun AddToBookBtn() {
    Button(
        onClick = { /*showMessage*/ },
        modifier = Modifier
            .height(40.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(
                android.graphics.Color.parseColor(
                    "#7AA8FF"
                )
            )
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp, 0.dp),

        ) {
        Text(
            text = "주소록",
            fontFamily = galmurinineFont,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun AddSubmitBtn(
    name: String,
    phone: String,
    viewModel: AddressPostSelfViewModel = viewModel(),
    changeToAddAddress: () -> Unit,
) {
    Button(
        onClick = {
            Log.d("PHONE", phone)
            Log.d("NAME", name)
            viewModel.postAddress(phone, name)
            changeToAddAddress()
        },
        modifier = Modifier
            .height(40.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(
                android.graphics.Color.parseColor(
                    "#7AA8FF"
                )
            )
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp, 0.dp),
    ) {
        Text(
            text = "등록",
            fontFamily = galmurinineFont,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun PatchSubmitBtn(
    apiPhone: String,
    name: String,
    phone: String,
    changeToPatchAddress: () -> Unit,
    viewModel: AddressPatchViewModel = viewModel()
) {
    val openDialog = remember { mutableStateOf(false)  }
//    var dialogTxt = ""
    val dialogTxt = remember { mutableStateOf("")  }
//    var goPatch = false

    Button(
        onClick = {
            if (name.isEmpty()) {
                openDialog.value = true
                dialogTxt.value = "이름을 입력해주세요"
            } else if (phone.isEmpty()) {
                openDialog.value = true
                dialogTxt.value = "핸드폰 번호를 입력해주세요"
            } else if (phone.length != 11) {
                openDialog.value = true
                dialogTxt.value = "유효한 번호를 입력해주세요"
            } else {
                patchDo(
                    apiPhone = apiPhone,
                    name = name,
                    phone = phone,
                    changeToPatchAddress = changeToPatchAddress,
                    patchUsingViewModel = { apiPhone: String, phone: String, name: String ->
                        viewModel.patchAddress(
                            apiPhone,
                            phone,
                            name
                        )
                    },
                )
            }
        },
        modifier = Modifier
            .height(40.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(
                android.graphics.Color.parseColor(
                    "#7AA8FF"
                )
            )
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp, 0.dp),
    ) {
        Text(
            text = "수정",
            fontFamily = galmurinineFont,
            fontSize = 16.sp,
            color = Color.White
        )
    }
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
//            text = { dialogTxt.value },
            text = { Text(text = "입력을 확인해주세요")},
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("OK")
                }
            }
        )
    }
}

fun patchDo(
    apiPhone: String,
    name: String,
    phone: String,
    changeToPatchAddress: () -> Unit,
    patchUsingViewModel: (String, String, String) -> Unit,
) {
    runBlocking { patchUsingViewModel(apiPhone, phone, name) }
    changeToPatchAddress()
}

//@Composable
//fun showDialogue(
//    openDialog: Boolean,
//    dialogTxt: String,
//    closeDialog: () -> Unit
//) {
//    if (openDialog) {
//
//        AlertDialog(
//            onDismissRequest = {
//                closeDialog
//            },
//            text = { dialogTxt },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        closeDialog
//                    }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}



