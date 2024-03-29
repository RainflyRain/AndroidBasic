package com.example.module_compose.widget

import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

/**
 * Created by zpf on 2022/6/15.
 */

@Composable
fun WaveTextView(
    modifier: Modifier,
    text: String,
    textSize: TextUnit,
    waveColor: Color,
    downTextColor: Color = Color.White,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.2f, 0.2f, 0.7f, 0.9f)
        ),
        repeatMode = RepeatMode.Restart
    )
) {

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current.density
        val circleSizeDp = minOf(maxWidth, maxHeight)
        val circleSizePx = circleSizeDp.value * density
        val waveWidth = circleSizePx / 1.2f
        val waveHeight = circleSizePx / 10f

        val textPaint by remember {
            mutableStateOf(Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                isDither = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.CENTER
            })
        }

        val wavePath by remember {
            mutableStateOf(Path())
        }

        val circlePath by remember {
            mutableStateOf(Path().apply {
                addArc(
                    Rect(0f, 0f, circleSizePx, circleSizePx),
                    0f, 360f
                )
            })
        }

        val animateValue by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = waveWidth,
            animationSpec = animationSpec,
        )

        Canvas(
            modifier = modifier.requiredSize(size = circleSizeDp)
        ) {
            textPaint.textSize = textSize.toPx()
            textPaint.color = waveColor.toArgb()
            val fontMetrics = textPaint.fontMetrics
            val x = size.width / 2f
            val y = size.height / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f
            drawContext.canvas.nativeCanvas.drawText(text, x, y, textPaint)

            wavePath.reset()
            wavePath.moveTo(-waveWidth + animateValue, circleSizePx / 2.3f)
            var i = -waveWidth
            while (i < circleSizePx + waveWidth) {
                wavePath.relativeQuadraticBezierTo(waveWidth / 4f, -waveHeight, waveWidth / 2f, 0f)
                wavePath.relativeQuadraticBezierTo(waveWidth / 4f, waveHeight, waveWidth / 2f, 0f)
                i += waveWidth
            }
            wavePath.lineTo(circleSizePx, circleSizePx)
            wavePath.lineTo(0f, circleSizePx)
            wavePath.close()
            val resultPath = Path.combine(
                path1 = circlePath,
                path2 = wavePath,
                operation = PathOperation.Intersect
            )
            drawPath(resultPath, waveColor)

            clipPath(path = resultPath, clipOp = ClipOp.Intersect) {
                textPaint.textSize = textSize.toPx()
                textPaint.color = downTextColor.toArgb()
                val fontMetrics = textPaint.fontMetrics
                val x = size.width / 2f
                val y = size.height / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f
                drawContext.canvas.nativeCanvas.drawText(text, x, y, textPaint)
            }
        }
    }

}