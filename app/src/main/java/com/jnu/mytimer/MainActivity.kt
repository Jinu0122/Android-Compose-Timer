package com.jnu.mytimer

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jnu.mytimer.ui.theme.MyTimerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.Files.size

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimerScreen()
                }
            }
        }
    }
}

@Composable
fun TimerScreen(){
    // 데이터
    var timerCount by remember { mutableStateOf(0)}
    var isActive by remember { mutableStateOf(false)}

    val snackbarHostState =  remember { SnackbarHostState()}
    val corotineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        while (true){

            delay(1000L) // 1초
            if (isActive && timerCount > 0 ) {
                timerCount--
            }
            if (isActive && timerCount == 0 ) {
                isActive = false
            }

        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally // 센터로 오게하기
    )
    {
        Text(timerCount.toString(),
            fontSize = 120.sp,
            modifier = Modifier.padding(100.dp)
        )

        // 타이머 토글 버튼
        Button(onClick = {
            //TODO:: 타이머 토글

            //현재 스낵바가 존재하면 리턴하기
           if (snackbarHostState.currentSnackbarData != null){
                return@Button
            }

            if (timerCount == 0){

                corotineScope.launch {
                    snackbarHostState
                        .showSnackbar("시간을 먼저 설정해 주세요",
                        actionLabel = "닫기", SnackbarDuration.Short).let {
                            when(it) {
                                SnackbarResult.Dismissed -> Log.d(TAG, "TimerScreen: 스낵바 닫힘")
                                SnackbarResult.ActionPerformed-> Log.d(TAG, "TimerScreen: 스낵바 닫기버튼 클릭")
                            }
                     }
                    return@launch
                }
                return@Button
            }

            isActive = !isActive
        }) {
            Text(if (isActive) "종료" else "시작",
                fontSize = 30.sp,
                modifier = Modifier.padding(10.dp)
                )
        }

        
        AnimatedVisibility(visible = !isActive) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("타이머 설정",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(10.dp)
                )

                Row() {

                    Button(
                        modifier = Modifier.size(100.dp),
                        onClick = {
                            //TODO:: 시간증가
                            timerCount += 1
                        }) {
                        Text("+",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(30.dp))

                    Button(
                        modifier = Modifier.size(100.dp),
                        onClick = {
                            //TODO:: 시간감소
                            if (timerCount > 0 ) timerCount -= 1
                        }) {
                        Text("-",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))

        // 스낵바가 보여지는 부분 
        SnackbarHost(hostState = snackbarHostState)
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTimerTheme {
        Greeting("Android")
    }
}