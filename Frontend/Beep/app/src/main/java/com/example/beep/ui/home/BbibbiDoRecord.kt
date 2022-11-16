package com.example.beep.ui.home

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beep.ui.message.startPlaying
import com.example.beep.ui.message.startRecording
import com.example.beep.ui.message.stopPlaying
import com.example.beep.ui.message.stopRecording
import com.example.beep.util.VoicePlayer
import com.example.beep.util.VoiceRecorder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BbibbiDoRecord(
    toSendMsg: () -> Unit,
    toAskRecord: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val voicePermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )
    val context = LocalContext.current
    var filepath = context.cacheDir.absolutePath + "/temp.3gp"
    val currentState = homeViewModel.recordMessageState

    DisposableEffect(key1 = Unit) {
        homeViewModel.playGreeting()
        Log.d("DisposableEffect", "Disposable Effect Called!!")
        VoiceRecorder.nullInstance()
        VoiceRecorder.getInstance(context)
        VoicePlayer.nullInstance()
        VoicePlayer.getInstance()
        File(filepath)

        onDispose {
            Log.d("DisposableEffect", "onDispose Called!!")
            VoiceRecorder.nullInstance()
            VoicePlayer.nullInstance()
        }
    }
    Column(
        modifier = modifier
            .width(320.dp)
            .wrapContentWidth(Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DoRecordScreen(
            currentState = currentState,
            messageTime = homeViewModel.time,
            duration = homeViewModel.fileLength
        )
        Spacer(modifier = modifier.height(35.dp))
        Row(modifier = Modifier.height(60.dp), verticalAlignment = Alignment.Bottom) {
            CancelBtn(onClick = {
                when (currentState) {
                    RecordMessageState.Before, RecordMessageState.Greeting -> {
                        toAskRecord()
                    }
                    RecordMessageState.Recording -> {
                        stopRecording(context)
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Finished
                    }
                    RecordMessageState.Finished -> {
                        resetRecording(context)
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Before
                    }
                    RecordMessageState.Playing -> {
                        stopPlaying()
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Finished
                    }
                    else -> {}
                }
            })
            LeftBtn(onClick = {
                when (currentState) {
                    RecordMessageState.Before -> {}
                    RecordMessageState.Recording -> {}
                    RecordMessageState.Finished -> {}
                    RecordMessageState.Playing -> {}
                    else -> {}
                }
            })
            RightBtn(onClick = {
                when (currentState) {
                    RecordMessageState.Before -> {}
                    RecordMessageState.Recording -> {}
                    RecordMessageState.Finished -> {
                        startPlayingVoiceMessage(
                            filepath = filepath,
                            changeCurrentState = {
                                homeViewModel.recordMessageState = RecordMessageState.Finished
                            },
                            onPrepared = { duration: Int -> homeViewModel.fileLength = duration })
                        homeViewModel.startTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Playing
                    }
                    RecordMessageState.Playing -> {}
                    else -> {}
                }
            })
            ConfirmBtn(onClick = {
                when (currentState) {
                    RecordMessageState.Greeting -> {
                        homeViewModel.stopGreeting()
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Before
                    }
                    RecordMessageState.Before -> {
                        if (!voicePermissionState.status.isGranted) {
                            voicePermissionState.launchPermissionRequest()
                        }
                        if (voicePermissionState.status.isGranted) {
                            if (File(filepath).exists()) {
                                File(filepath).delete()
                            }
                            homeViewModel.fileLength = 31
                            startRecording(context, filepath)
                            homeViewModel.startTimer()
                            homeViewModel.recordMessageState = RecordMessageState.Recording
                        }
                    }
                    RecordMessageState.Recording -> {
                        stopRecording(context)
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Finished
                    }
                    RecordMessageState.Finished -> {
                        toSendMsg()
                        homeViewModel.recordMessageState = RecordMessageState.Before
                    }
                    RecordMessageState.Playing -> {
                        stopPlaying()
                        homeViewModel.stopTimer()
                        homeViewModel.recordMessageState = RecordMessageState.Finished
                    }
                    else -> {}
                }
            })
        }
    }
}

fun startPlayingVoiceMessage(
    filepath: String,
    changeCurrentState: () -> Unit,
    onPrepared: (Int) -> Unit
) {
    VoicePlayer.nullInstance()
    VoicePlayer.getInstance().apply {
        setDataSource(filepath)
        prepare()
        setOnPreparedListener {
            onPrepared(it.duration/1000)
        }
        setOnCompletionListener {
            stopPlaying()
            changeCurrentState()
        }
    }.start()
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ConfirmBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(67.dp),
        shape = RoundedCornerShape(65.dp, 20.dp, 50.dp, 0.dp),
        onClick = onClick
    ) {
    }
}

@Composable
fun RightBtn(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Button(onClick = onClick) {

    }
}

@Composable
fun LeftBtn(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Button(onClick = onClick) {

    }
}

@Composable
fun CancelBtn(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Button(onClick = onClick) {

    }
}

@Composable
fun DoRecordScreen(
    modifier: Modifier = Modifier,
    currentState: RecordMessageState,
    messageTime: Int,
    duration: Int
) {

    when (currentState) {
        RecordMessageState.Greeting -> {
            Text(text = "인사말 재생중...")
        }
        RecordMessageState.Before -> {
            Text(text = "/ : 취소 | ● : 녹음 시작", fontSize = 19.sp)
        }
        RecordMessageState.Recording -> {
            Text(text = "녹음중 $messageTime/00:30", fontSize = 19.sp)
        }
        RecordMessageState.Finished -> {
            Text(text = "/ : 다시 녹음 | > : 재생 | ● : 전송", fontSize = 19.sp)
        }
        RecordMessageState.Playing -> {
            Text(text = "재생중 $messageTime/$duration", fontSize = 19.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun resetRecording(context: Context) {
    VoiceRecorder.nullInstance()
    VoiceRecorder.getInstance(context)
}