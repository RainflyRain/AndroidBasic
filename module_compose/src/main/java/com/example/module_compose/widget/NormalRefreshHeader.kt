package com.example.module_compose.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.module_compose.ui.theme.ComposeTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.zj.refreshlayout.RefreshHeaderState
import com.zj.refreshlayout.SwipeRefreshState
import com.zj.refreshlayout.header.ArrowDrawable
import com.zj.refreshlayout.header.ProgressDrawable

/**
 * Created by zpf on 2022/6/17.
 */

@Composable
fun NormalRefreshHeader(state: SwipeRefreshState) {
    val text = when (state.headerState) {
        RefreshHeaderState.PullDownToRefresh -> "下拉刷新"
        RefreshHeaderState.Refreshing -> "正在刷新..."
        else -> "释放刷新"
    }
    val angle = remember {
        Animatable(0f)
    }
    LaunchedEffect(state.headerState) {
        if (state.headerState == RefreshHeaderState.ReleaseToRefresh) {
            angle.animateTo(180f)
        } else {
            angle.animateTo(0f)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (state.headerState == RefreshHeaderState.Refreshing) {
                Image(
                    painter = rememberDrawablePainter(drawable = ProgressDrawable().apply {
                        setColor(0xff666666.toInt())
                    }),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                )
            } else {
                Image(
                    painter = rememberDrawablePainter(drawable = ArrowDrawable()),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(angle.value)
                )
            }
            Text(
                text = text,
                color = ComposeTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .wrapContentSize()
                    .clipToBounds()
                    .padding(16.dp, 0.dp)
            )
        }
    }
}