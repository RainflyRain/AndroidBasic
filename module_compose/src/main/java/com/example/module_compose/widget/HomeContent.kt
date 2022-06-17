package com.example.module_compose.widget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.module_compose.R
import com.example.module_compose.style.UnitedSansCondBold
import com.example.module_compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

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
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> {
                    GoalPage()
                }
                1 -> {
                    PageItem(page.toString())
                }
                2 -> {
                    PageItem(page.toString())
                }
                3 -> {
                    PageItem(page.toString())
                }
            }
        }
    }
}

@Composable
private fun GoalPage() {
    Column(
        Modifier
            .fillMaxSize()
    ) {

        TopNavigationBar("今天", rightPainter = painterResource(R.drawable.icon_topbar_calendar_goal))

        Column(Modifier.verticalScroll(ScrollState(0))) {
            GoalHeaderView()
            Text(
                "今日数据",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
            )
            for (i in 1..10) {
                GoalNormalChatView()
            }
        }

    }
}

@Composable
private fun GoalNormalChatView() {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ComposeTheme.colors.gradientBg0,
                        ComposeTheme.colors.gradientBg1
                    )
                )
            )
    ) {

        val (helpTitle, helpIcon, currentNum, aimSum, aimChart) = createRefs()

        val margin = 16.dp

        Text("能量消耗", Modifier.constrainAs(helpTitle) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(parent.top, margin = margin)
        }, fontSize = 14.sp, color = Color.White)

        Image(
            painterResource(R.drawable.icon_goal_help),
            contentDescription = "帮助",
            Modifier
                .size(30.dp)
                .padding(8.dp)
                .constrainAs(helpIcon) {
                    top.linkTo(helpTitle.top)
                    start.linkTo(helpTitle.end)
                    bottom.linkTo(helpTitle.bottom)
                })

        Text("0", Modifier.constrainAs(currentNum) {
            top.linkTo(parent.top, margin = margin)
            end.linkTo(parent.end, margin = margin)
        }, fontSize = 34.sp, color = Color.White)

        Text(
            text = "目标 420 kcal",
            Modifier.constrainAs(aimSum) {
                top.linkTo(currentNum.bottom)
                end.linkTo(parent.end, margin = margin)
            },
            fontSize = 12.sp,
            color = Color.White
        )


        Text(
            "",
            Modifier
                .width(240.dp)
                .padding(1.dp)
                .constrainAs(aimChart) {
                    top.linkTo(helpTitle.bottom)
                    start.linkTo(parent.start, margin = margin)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .drawWithContent {

                    val path = Path()
                    path.moveTo(size.width / 10, size.height * 3 / 10)
                    path.lineTo(size.width * 2 / 10, size.height * 7 / 10)
                    path.lineTo(size.width * 3 / 10, size.height * 2 / 10)
                    path.lineTo(size.width * 4 / 10, size.height * 5 / 10)
                    path.lineTo(size.width * 5 / 10, size.height * 8 / 10)
                    path.lineTo(size.width * 6 / 10, size.height * 1 / 10)
                    path.lineTo(size.width * 7 / 10, size.height * 4 / 10)
                    path.lineTo(size.width * 8 / 10, size.height * 9 / 10)
                    path.lineTo(size.width * 9 / 10, size.height * 2 / 10)
                    path.lineTo(size.width, size.height)

                    drawPath(path = path, Color.Yellow, style = Stroke(2.dp.toPx()))
                },
            color = Color.White
        )

    }
}

@Composable
private fun GoalHeaderView() {
    val caloriesColor = ComposeTheme.colors.dailyCalories
    val exerciseTime = ComposeTheme.colors.dailyExerciseTime

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
    ) {

        val (circleCalorie, numCalorie, sumCalorie, unitCalorie) = createRefs()

        Surface(
            Modifier
                .constrainAs(circleCalorie) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(numCalorie.start, margin = 10.dp)
                }
                .size(60.dp)
                .drawWithContent() {
                    drawCircle(
                        caloriesColor,
                        size.height / 2,
                        Offset(size.width / 2, size.height / 2),
                        0.3f,
                        style = Stroke(4.dp.toPx())
                    )

                    drawCircle(
                        exerciseTime,
                        size.height / 3,
                        Offset(size.width / 2, size.height / 2),
                        0.3f,
                        style = Stroke(4.dp.toPx())
                    )
                }) {

        }

        Text(
            "0",
            color = Color.White,
            fontSize = 92.sp,
            fontFamily = UnitedSansCondBold,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(numCalorie) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )


        Text(
            "/420",
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = UnitedSansCondBold,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(sumCalorie) {
                start.linkTo(numCalorie.end, margin = 10.dp)
                bottom.linkTo(unitCalorie.top)
            }
        )
        Text(
            "Kcal",
            color = caloriesColor,
            fontSize = 18.sp,
            fontFamily = UnitedSansCondBold,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(unitCalorie) {
                start.linkTo(numCalorie.end, margin = 10.dp)
                bottom.linkTo(circleCalorie.bottom)
            }
        )
    }
}

@Composable
private fun PageItem(string: String) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TopNavigationBar(
            "标题",
            rightPainter = painterResource(R.drawable.icon_topbar_more)
        ) {
            Log.i("TAG", "PageItem: $it")
        }

        Text(
            text = string,
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            textAlign = TextAlign.Center
        )

    }

}

@Preview
@Composable
fun HomeContentPreview() {
    ComposeTheme {
//        PageItem(string = "内容")
        GoalPage()
    }
}