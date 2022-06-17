package com.example.module_compose.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.module_compose.R

/**
 * Created by zpf on 2022/6/15.
 */

@Composable
fun BottomTabBar(
    selected: Int,
    modifier: Modifier = Modifier,
    onBottomTabClick: ((index: Int) -> Unit)? = null
) {

    Row(
        modifier
            .fillMaxWidth()
    ) {
        TabItem(
            "首页",
            if (selected == 0) R.drawable.tabbar_richang_selected else R.drawable.tabbar_richang_normal,
            if (selected == 0) Color.Red else Color.LightGray,
            modifier
                .weight(1f)
                .normalClick { onBottomTabClick?.invoke(0) }
        )
        TabItem(
            "",
            if (selected == 1) R.drawable.tabbar_yundong_selected else R.drawable.tabbar_yundong_normal,
            if (selected == 1) Color.Red else Color.LightGray,
            modifier
                .weight(1f)
                .normalClick { onBottomTabClick?.invoke(1) }
        )
        TabItem(
            "",
            if (selected == 2) R.drawable.tabbar_geren_selected else R.drawable.tabbar_geren_normal,
            if (selected == 2) Color.Red else Color.LightGray,
            modifier
                .weight(1f)
                .normalClick { onBottomTabClick?.invoke(2) }
        )
        TabItem(
            "",
            if (selected == 3) R.drawable.tabbar_shebei_selected else R.drawable.tabbar_shebei_normal,
            if (selected == 3) Color.Red else Color.LightGray,
            modifier
                .weight(1f)
                .normalClick { onBottomTabClick?.invoke(3) }
        )
    }
}

@Composable
private fun TabItem(title: String?, id: Int, tint: Color, modifier: Modifier) {
    Column(
        modifier
            .height(56.dp)
            .padding(vertical = 8.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(id), "chat", Modifier.size(40.dp), tint)
        if (!title.isNullOrBlank()) {
            Text(title, fontSize = 12.sp)
        }
    }
}