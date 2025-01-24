package com.example.module_compose.widget

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.module_compose.R
import com.example.module_compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlin.math.roundToInt

/**
 * Created by zpf on 2022/6/16.
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeContent(currentPage: PagerState, modifier: Modifier = Modifier) {
    Column(modifier) {
        HorizontalPager(
            count = 4,
            modifier,
            currentPage,
            userScrollEnabled = true,
        ) { page ->
            when (page) {
                0 -> {
                    GoalPage()
                }
                1 -> {
                    WorkoutPage()
                }
                2 -> {
                    MinePage()
                }
                3 -> {
                    DevicePage()
                }
            }
        }
    }
}

@Composable
fun DevicePage() {
    Column(Modifier.fillMaxSize()) {
        // set up all transformation states
        var scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            rotation += rotationChange
            offset += offsetChange
        }
        Box(
            Modifier
                // apply other transformations like rotation and zoom
                // on the pizza slice emoji
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                // add transformable to listen to multitouch transformation events
                // after offset
                .transformable(state = state)
                .background(Color.LightGray)
        )
    }
}

@Composable
fun MinePage(){
    Column() {
        Box(modifier = Modifier.fillMaxSize().align(Alignment.Start)) {
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            val context = LocalContext.current
            Box(
                Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .background(Color.LightGray)
                    .size(50.dp)
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .clickable(
                        onClick = {
                            Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show()
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    ComposeTheme {
//        GoalPage()
        MinePage()
//        PageItem(string = "内容")
    }
}