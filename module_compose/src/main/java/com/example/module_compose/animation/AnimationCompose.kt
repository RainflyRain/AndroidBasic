package com.example.module_compose.animation

import androidx.compose.animation.*
import androidx.compose.ui.Alignment

/**
 * Created by zpf on 2022/6/14.
 */
fun elementEnter():EnterTransition{
    return slideInVertically {
        -40
    } + expandVertically(
        expandFrom = Alignment.Top
    ) + fadeIn(
        initialAlpha = 0.3f
    )
}

fun elementExit():ExitTransition{
    return slideOutVertically() + shrinkVertically() + fadeOut()
}