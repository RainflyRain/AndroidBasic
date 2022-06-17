package com.example.module_compose.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.module_compose.R
import com.example.module_compose.ui.theme.ComposeTheme

/**
 * Created by zpf on 2022/6/16.
 */
@Composable
fun TopNavigationBar(
    title: String,
    leftTxt: String? = null,
    leftPainter: Painter? = null,
    rightTxt: String? = null,
    rightPainter: Painter? = null,
    modifier: Modifier = Modifier,
    onTopEvent: ((TopNavigationEvent) -> Unit)? = null
) {
    Box(
        modifier
    ) {
        Row(
            modifier = Modifier
                .background(ComposeTheme.colors.navigation)
                .fillMaxWidth()
                .height(48.dp)
        ) {

            TextButtonWidget(leftTxt, leftPainter, Modifier.align(Alignment.CenterVertically)) {
                onTopEvent?.invoke(TopNavigationEvent.TOP_LEFT_EVENT)
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButtonWidget(rightTxt, rightPainter, Modifier.align(Alignment.CenterVertically)) {
                onTopEvent?.invoke(TopNavigationEvent.TOP_RIGHT_EVENT)
            }
        }
        Text(
            title,
            color = Color.White,
            modifier = Modifier
                .clickable {
                    onTopEvent?.invoke(TopNavigationEvent.TOP_CENTER_EVENT)
                }
                .align(Alignment.Center),
            fontSize = 18.sp
        )
    }
}

@Composable
fun TextButtonWidget(
    txt: String? = null,
    painter: Painter? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(modifier) {
        if (txt != null) {
            Text(
                txt,
                Modifier
                    .clickable { onClick?.invoke() },
                color = Color.White,
                fontSize = 16.sp
            )
        } else if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .clickable { onClick?.invoke() }
                    .size(36.dp)
                    .padding(8.dp)
            )
        }
    }
}

enum class TopNavigationEvent {
    TOP_LEFT_EVENT, TOP_RIGHT_EVENT, TOP_CENTER_EVENT
}


@Preview(showBackground = true)
@Composable
fun TopNavigationBarPreview() {
    ComposeTheme {

        Column {
            TopNavigationBar(
                "标题",
                "返回",
                leftPainter = painterResource(R.drawable.icon_topbar_more),
                rightPainter = painterResource(R.drawable.icon_topbar_more)
            ) {
                Log.i("TopNavigationBarPreview", "TopNavigationBarPreview: $it")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            TextButtonWidget(
                txt = "更多",
                modifier = Modifier.background(ComposeTheme.colors.navigation)
            )
        }
    }

}