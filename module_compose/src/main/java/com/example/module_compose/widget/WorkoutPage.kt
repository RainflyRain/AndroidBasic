package com.example.module_compose.widget

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.module_compose.R
import com.example.module_compose.style.DinBold
import com.example.module_compose.style.DinCondBold
import com.example.module_compose.ui.theme.ComposeTheme
import com.zj.refreshlayout.SwipeRefreshLayout
import kotlinx.coroutines.delay

/**
 * Created by zpf on 2022/6/17.
 */

@Composable
fun WorkoutPage() {
    Column(Modifier.fillMaxSize()) {
        TopNavigationBar("所有运动", rightTxt = "统计")

        TextButton(onClick = { /*TODO*/ }, modifier = Modifier.height(40.dp)) {
            Text(text = "2022年5月", color = Color.White)
        }

        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(2000)
                refreshing = false
            }
        }
        SwipeRefreshLayout(isRefreshing = refreshing, onRefresh = { refreshing = true }, indicator = {
            NormalRefreshHeader(state = it)
        }) {
            Column(modifier = Modifier.verticalScroll(ScrollState(0))) {
                repeat(20){
                    WorkoutItem()
                    Divider(
                        Modifier.padding(end = 8.dp),
                        color = ComposeTheme.colors.line,
                        thickness = 1.dp,
                        startIndent = 100.dp
                    )
                }
            }
        }


    }
}

@Composable
private fun WorkoutItem() {
    ConstraintLayout(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = MaterialTheme.colors.primary
            )
    ) {
        val (workoutMap, workoutDistance, workoutDistanceUnit, workoutTime, workoutSpeed, workoutDate, workoutLocation, workoutIcon) = createRefs()
        Image(
            painterResource(R.drawable.icon_workout_amp), null,
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(workoutMap) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                })

        Text(
            text = "0.89",
            fontSize = 30.sp,
            color = ComposeTheme.colors.dailyCalories,
            modifier = Modifier.constrainAs(workoutDistance) {
                top.linkTo(parent.top)
                start.linkTo(workoutMap.end, margin = 8.dp)
                bottom.linkTo(workoutTime.top)
            },
            fontFamily = DinCondBold,
            fontWeight = FontWeight.Medium
        )

        Text(
            "km",
            color = ComposeTheme.colors.textSecondary,
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(workoutDistanceUnit) {
                start.linkTo(workoutDistance.end)
                bottom.linkTo(workoutDistance.bottom)
            },
            fontFamily = DinBold,
            fontWeight = FontWeight.Medium)

        Text(
            "02:55",
            color = ComposeTheme.colors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(workoutTime) {
                top.linkTo(workoutDistance.bottom)
                start.linkTo(workoutDistance.start)
                bottom.linkTo(workoutDate.top)
            },
            fontFamily = DinBold,
            fontWeight = FontWeight.Medium)

        Text(
            "151'57/km",
            color = ComposeTheme.colors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(workoutSpeed) {
                top.linkTo(workoutTime.top)
                start.linkTo(workoutTime.end, margin = 8.dp)
                bottom.linkTo(workoutTime.bottom)
            },
            fontFamily = DinBold,
            fontWeight = FontWeight.Medium)

        Text(
            "2019年01月10日",
            color = ComposeTheme.colors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(workoutDate) {
                top.linkTo(workoutTime.bottom)
                start.linkTo(workoutTime.start)
                bottom.linkTo(parent.bottom)
            },
            fontFamily = DinBold,
            fontWeight = FontWeight.Medium)

        Text(
            "跑步",
            color = ComposeTheme.colors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(workoutLocation) {
                top.linkTo(workoutDate.top)
                start.linkTo(workoutDate.end, margin = 8.dp)
                bottom.linkTo(workoutDate.bottom)
            },
            fontFamily = DinBold,
            fontWeight = FontWeight.Medium)

        Icon(painterResource(R.drawable.icon_workout_outdoor_run), null, Modifier.constrainAs(workoutIcon){
            top.linkTo(parent.top)
            end.linkTo(parent.end, margin = 16.dp)
            bottom.linkTo(parent.bottom)
        }, tint = ComposeTheme.colors.dailyCalories)
    }
}