package com.example.module_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.module_compose.ui.theme.ComposeTheme
import com.example.module_compose.widget.BottomTabBar
import com.example.module_compose.widget.HomeContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTheme() {
                Column {

                    val currentPage = rememberPagerState(0)
                    val scope = rememberCoroutineScope() // 创建 CoroutineScope

                    HomeContent(currentPage, Modifier.weight(1f))

                    BottomTabBar(
                        currentPage.currentPage,
                        Modifier.background(ComposeTheme.colors.navigation)
                    ) {
                        // 点击页签后，在协程里翻页
                        scope.launch {
                            currentPage.animateScrollToPage(it)
                        }
                    }

                }
            }
        }
    }


}


@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTheme() {
        Column {

            val currentPage = rememberPagerState(0)

            HomeContent(currentPage, Modifier.weight(1f))

            BottomTabBar(currentPage.currentPage, Modifier.background(ComposeTheme.colors.navigation))
        }
    }
}