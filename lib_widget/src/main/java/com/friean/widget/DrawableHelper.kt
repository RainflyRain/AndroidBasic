package com.friean.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.view.Gravity
import android.view.View
import androidx.core.graphics.plus
import kotlin.math.PI
import kotlin.math.tan

fun createRectDrawable(color:Int, width:Int, height:Int, radii:Float):Drawable{
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(color)
        cornerRadius = radii
        setSize(width, height)
    }
}

//图表横屏CheckBox的图标
fun Context.createCheckBoxDrawable(colorRes:Int):StateListDrawable{
    val size = dp2px(16).toInt()
    val strokeWidth = dp2px(2).toInt()
    return createCheckBoxDrawable(resources.getColor(colorRes), size, strokeWidth)
}
//图表横屏CheckBox的图标
fun createCheckBoxDrawable(color:Int, size:Int, strokeWidth:Int):StateListDrawable{
    return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_pressed), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(strokeWidth, color)
            setColor(color)
            setSize(size, size)
        })
        addState(intArrayOf(android.R.attr.state_checked), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(strokeWidth, color)
            setColor(color)
            setSize(size, size)
        })
        addState(intArrayOf(), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(strokeWidth, color)
            setSize(size, size)
        })
    }
}


//矩形按钮效果的背景
fun View.setBgBtnColorResource(colorRes:Int, radiiInDp:Int){
    val radii = dp2px(radiiInDp)
    val color = context.resources.getColor(colorRes)
    StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_pressed), GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radii
            setColor(color.setOpacity(0.5f))
        })
        addState(intArrayOf(), GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radii
            setColor(color)
        })
        background = this
    }
}

//圆形按钮效果的背景
fun View.setBgBtnOvalColorResource(colorRes:Int, size:Int){
    val sizeInPx = dp2px(size).toInt()
    val color = context.resources.getColor(colorRes)
    StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_pressed), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(sizeInPx, sizeInPx)
            setColor(color.setOpacity(0.5f))
        })
        addState(intArrayOf(), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(sizeInPx, sizeInPx)
            setColor(color)
        })
        background = this
    }
}

//矩形单色背景
fun View.setBgRectangleColorResource(colorRes:Int, radiiInDp:Int){
    val radii = dp2px(radiiInDp)
    val color = context.resources.getColor(colorRes)
    GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = radii
        setColor(color)
        background = this
    }
}

fun Context.createBgRect(colorRes:Int, radii:FloatArray):Drawable{
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = radii
        setColor(resources.getColor(colorRes))
    }
}



//圆形单色背景
fun View.setBgOvalColorResource(colorRes:Int, size:Int){
    val width = dp2px(size).toInt()
    val color = context.resources.getColor(colorRes)
     GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setSize(width, width)
        setColor(color)
        background = this
    }
}

//圆环单色背景
fun View.setBgRingColorResource(colorRes:Int, size:Int, strokeWidth: Int){
    val sizeInPx = dp2px(size).toInt()
    val strokeWidthInPx = dp2px(strokeWidth).toInt()
    val color = context.resources.getColor(colorRes)
    GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setSize(sizeInPx, sizeInPx)
        setStroke(strokeWidthInPx, color)
        background = this
    }
}



fun Int.setOpacity(opacity: Float): Int {
    //产品定义背景色为前景色的20%
    return setAlpha((0xff * opacity).toInt())
}

fun Int.setAlpha(alpha: Int): Int {
    //产品定义背景色为前景色的20%
    return this and 0xffffff or (alpha shl 24)
}


fun createGradientBg(colors:IntArray, radii:Float, topOnly:Boolean=false):GradientDrawable{
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = if (topOnly){
            floatArrayOf(radii, radii, radii, radii, 0f, 0f, 0f, 0f)
        }else{
            floatArrayOf(radii, radii, radii, radii, radii, radii, radii, radii)
        }
        if (colors.isNotEmpty()) {
            if (colors.size == 1) {
                setColor(colors[0])
            } else {
                this.colors = colors
            }
        }
    }
}

/**
 * 底部圆角
 */
fun createGradientBgRadius(colors:IntArray, radii:Float, bottomOnly:Boolean=false):GradientDrawable{
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = if (bottomOnly){
            floatArrayOf(0f, 0f, 0f, 0f,radii, radii, radii, radii)
        }else{
            floatArrayOf(radii, radii, radii, radii, radii, radii, radii, radii)
        }
        if (colors.isNotEmpty()) {
            if (colors.size == 1) {
                setColor(colors[0])
            } else {
                this.colors = colors
            }
        }
    }
}

//设置圆形指示背景
fun View.setBgIndicatorOvalColorResource(colorRes:Int, size:Int){
    val sizeInPx = dp2px(size).toInt()
    val color = context.resources.getColor(colorRes)
    StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_activated), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(sizeInPx, sizeInPx)
        })
        addState(intArrayOf(android.R.attr.state_selected), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(sizeInPx, sizeInPx)
        })
        addState(intArrayOf(), GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color.setOpacity(0.5f))
            setSize(sizeInPx, sizeInPx)
        })
        background = this
    }
}
//设置心率副标题背景
fun View.setBgRect(colorRes:Int, radii:Int, fill:Boolean=false, lineColorRes: Int?=null, dashWidth:Float=0f, dashGap:Float=0f){
    val radiiInPx = dp2px(radii)
    val strokeWidth = dp2px(1).toInt()
    val color = context.resources.getColor(colorRes)
    GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = radiiInPx
        if (fill){
            setColor(color)
        }

        if (lineColorRes != null){
            setStroke(strokeWidth, context.resources.getColor(lineColorRes), dashWidth, dashGap)
        }else{
            setStroke(strokeWidth, color, dashWidth, dashGap)
        }

        background = this
    }
}

fun View.setBgRect(colorRes:Int, radii:FloatArray, fill:Boolean=false, lineColorRes: Int?=null, dashWidth:Float=0f, dashGap:Float=0f){
    val strokeWidth = dp2px(1).toInt()
    val color = context.resources.getColor(colorRes)
    GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = radii
        if (fill){
            setColor(color)
        }

        if (lineColorRes != null){
            setStroke(strokeWidth, context.resources.getColor(lineColorRes), dashWidth, dashGap)
        }else{
            setStroke(strokeWidth, color, dashWidth, dashGap)
        }

        background = this
    }
}

//制式单位的背景
fun View.setBgUnit(colorRes: Int, left:Boolean, checkedColorRes:Int, isStroke: Boolean){
    val radii = dp2px(5)
    val strokeWidth = dp2px(1).toInt()
    val color = context.resources.getColor(colorRes)
    val checkedColor =  context.resources.getColor(checkedColorRes)
    val radiis = if(left){
        floatArrayOf(radii, radii, 0f, 0f, 0f, 0f, radii, radii)
    }else{
        floatArrayOf(0f, 0f, radii, radii, radii, radii, 0f, 0f)
    }

    StateListDrawable().apply {
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = radiis
            setColor(checkedColor)
        }.also {
            addState(intArrayOf(android.R.attr.state_checked), it)
            addState(intArrayOf(android.R.attr.state_activated), it)
            addState(intArrayOf(android.R.attr.state_pressed), it)
        }
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = radiis
            setBackgroundResource(colorRes)
            if(isStroke){
                setStroke(strokeWidth, color)
            }
        }.also {
            addState(intArrayOf(), it)
        }
        background = this
    }
}


fun Context.getAppSelector(selectedColorRes:Int, unselectedColorRes:Int):Drawable{
    return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_checked), getAppCheckedDrawable(selectedColorRes, true))
        addState(intArrayOf(), getAppCheckedDrawable(unselectedColorRes, false))
    }
}

////这里保持View的宽度比高度大12（=20-4-4）则可以保证按钮的开关图是圆形的
fun Context.getAppCheckedDrawable(colorRes: Int, checked:Boolean):Drawable{
    val color = resources.getColor(colorRes)
    return arrayOf(
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(0)
                setSize(dp2px(40).toInt(), dp2px(28).toInt())
            },
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp2px(1000)
                setColor(color.setOpacity(0.5f))
                setSize(dp2px(40).toInt(), dp2px(14).toInt())
            },
            GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
                setSize(dp2px(20).toInt(), dp2px(20).toInt())
            }
    ).let {
        LayerDrawable(it)
    }.apply {
        setLayerInset(1,
                0,
                dp2px(7).toInt(),
                0,
                dp2px(7).toInt())
        setLayerInset(2, //这里保持View的宽度比高度大12（=20-4-4）则可以保证按钮的开关图是圆形的
                if(checked) dp2px(20).toInt() else 0,
                dp2px(4).toInt(),
                if(checked) 0 else dp2px(20).toInt(),
                dp2px(4).toInt())
    }
}


//三角形单色背景
fun View.setBgTriangleColorResource(colorRes:Int){
    background = object :Drawable(){
        private val paint by lazy {
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = context.resources.getColor(colorRes)
                style = Paint.Style.FILL
            }
        }

        private val path by lazy { Path() }
        override fun draw(canvas: Canvas) {
            canvas.drawPath(path, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun onBoundsChange(bounds: Rect?) {
            super.onBoundsChange(bounds)
            path.reset()
            bounds?.let {
                path.moveTo(it.left.toFloat(), it.bottom.toFloat())
                path.lineTo(it.right.toFloat(), it.bottom.toFloat())
                path.lineTo(it.centerX().toFloat(), it.top.toFloat())
                path.close()
            }
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }
    }
}

fun View.createWheelCheckBoxDrawable(
        colorRes:Int, checkedRes:Int, size:Int, strokeWidth:Int):StateListDrawable{
    val sizeInPx = dp2px(size).toInt()
    val strokeWidthInPx = dp2px(strokeWidth).toInt()
    val innerPadding = 3*strokeWidthInPx
    val innerSizeInPx = sizeInPx-2*innerPadding
    return arrayOf(
            GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setStroke(strokeWidthInPx, resources.getColor(checkedRes))
                setSize(sizeInPx, sizeInPx)
            },
            GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(resources.getColor(checkedRes))
                setSize(innerSizeInPx, innerSizeInPx)
            }
    ).let {
        LayerDrawable(it).apply {
            setLayerInset(1, innerPadding, innerPadding, innerPadding, innerPadding)
        }
    }.let {
        StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_pressed), it)
            addState(intArrayOf(android.R.attr.state_activated), it)
            addState(intArrayOf(android.R.attr.state_checked), it)
            addState(intArrayOf(), GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setStroke(strokeWidthInPx, resources.getColor(colorRes))
                setSize(sizeInPx, sizeInPx)
            })
        }
    }
}

//三角形单色背景
fun View.createArrow(colorRes:Int, size:Int, strokeWidth: Int,
                     gravity: Int, ringStrokeWidth:Int?):Drawable{
    return object : TintDrawable(){
        private val sizeInPx = dp2px(size).toInt()
        private val strokeWidthInPx = dp2px(strokeWidth)
        private val ringStrokeWidthInPx:Float? = ringStrokeWidth?.let { dp2px(it) }
        private val rect = RectF()
        private val paint by lazy {
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = context.resources.getColor(colorRes)
                isDither = true
                isAntiAlias = true
                style = Paint.Style.STROKE
            }
        }

        private val path by lazy { Path() }
        override fun getAlpha(): Int {
            return paint.alpha
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }
        override fun onBoundsChange(bounds: Rect?) {
            super.onBoundsChange(bounds)
            path.reset()
            bounds?.let {
                path.moveTo(it.width().toFloat()*0.35f, it.height().toFloat()*0.25f)
                path.lineTo(it.width().toFloat()*0.35f, it.height().toFloat()*0.65f)
                path.lineTo(it.width().toFloat()*0.75f, it.height().toFloat()*0.65f)
                val inset = (ringStrokeWidthInPx?:0f)*0.5f
                rect.set(it.left+inset, it.top+inset, it.right-inset, it.bottom-inset)
            }
        }

        override fun draw(canvas: Canvas) {
            paint.colorFilter = if(colorFilter != null) colorFilter else tintFilter
            val saveCount = canvas.save()
            when(gravity){
                Gravity.START->45f
                Gravity.END->-135f
                Gravity.TOP->135f
                else->-45f
            }.let {
                canvas.rotate(it, bounds.centerX().toFloat(), bounds.centerY().toFloat())
            }
            paint.strokeWidth = strokeWidthInPx
            canvas.drawPath(path, paint)
            canvas.restoreToCount(saveCount)
            if(ringStrokeWidthInPx != null && ringStrokeWidthInPx > 0){
                paint.strokeWidth = ringStrokeWidthInPx
                canvas.drawOval(rect, paint)
            }
        }
        override fun getIntrinsicHeight(): Int = sizeInPx
        override fun getIntrinsicWidth(): Int = sizeInPx
    }
}


fun View.createAddDrawableByRes(colorRes:Int, size:Int, strokeWidth: Int,circleColorRes:Int?=null):Drawable{
    return createAddDrawable(
        resources.getColor(colorRes),
        size,
        strokeWidth,
        if (circleColorRes == null) null else resources.getColor(circleColorRes!!)
    )
}
//加号
fun View.createAddDrawable(color:Int, size:Int, strokeWidth: Int,circleColor:Int?=null):Drawable{
    val sizeInPx = dp2px(size).toInt()
    val strokeWidthInPx = dp2px(strokeWidth).toInt()
    val signSize = if(circleColor == null) sizeInPx else (sizeInPx*0.55f).toInt()
    val insect = ((sizeInPx-strokeWidthInPx)/2f).toInt()
    val paddingInsect = ((sizeInPx-signSize)/2f).toInt()
    return arrayOf(
            GradientDrawable().apply {
                if(circleColor == null){
                    shape = GradientDrawable.RECTANGLE
                    setColor(0)
                }else{
                    shape = GradientDrawable.OVAL
                    setColor(circleColor)
                }
                setSize(sizeInPx, sizeInPx)
            },
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                setSize(signSize, strokeWidthInPx)
            },
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                setSize(strokeWidthInPx, signSize)
            }
    ).let {
        LayerDrawable(it)
    }.apply {
        setLayerInset(1,
                paddingInsect,
                insect,
                paddingInsect,
                insect)
        setLayerInset(2,
                insect,
                paddingInsect,
                insect,
                paddingInsect)
    }
}
//减号
fun View.createSubtractDrawable(colorRes:Int, size:Int, strokeWidth: Int,circleColorRes:Int?=null):Drawable{
    val sizeInPx = dp2px(size).toInt()
    val strokeWidthInPx = dp2px(strokeWidth).toInt()
    val color = resources.getColor(colorRes)
    val circleColor = circleColorRes?.let{resources.getColor(it)}
    val signSize = if(circleColor == null) sizeInPx else (sizeInPx*0.7f).toInt()
    val insect = ((sizeInPx-strokeWidthInPx)/2f).toInt()
    val paddingInsect = ((sizeInPx-signSize)/2f).toInt()
    return arrayOf(
            GradientDrawable().apply {
                if(circleColor == null){
                    shape = GradientDrawable.RECTANGLE
                    setColor(0)
                }else{
                    shape = GradientDrawable.OVAL
                    setColor(circleColor)
                }
                setSize(sizeInPx, sizeInPx)
            },
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                setSize(signSize, strokeWidthInPx)
            }
    ).let {
        LayerDrawable(it)
    }.apply {
        setLayerInset(1,
                paddingInsect,
                insect,
                paddingInsect,
                insect)
    }
}

fun View.createLineProgressDrawable(colorRes:Int,bgColorRes:Int?=null):Drawable{
    val color = resources.getColor(colorRes)
    val bgColor = bgColorRes?.let{resources.getColor(it)}
    return arrayOf(
            GradientDrawable().apply {
                id = android.R.id.background
                shape = GradientDrawable.RECTANGLE
                if(bgColor == null){
                    setStroke(1, color)
                }else{
                    setColor(bgColor)
                }
                cornerRadius = dp2px(5)
            },
            GradientDrawable().apply {
                id = android.R.id.progress
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                cornerRadius = dp2px(5)
                if(bgColor == null){
                    setStroke(1, color)
                }else{
                    setStroke(dp2px(1).toInt(),bgColor)
                }
            }.let {
                ClipDrawable(it, Gravity.LEFT, ClipDrawable.HORIZONTAL)
            }
    ).let {
        LayerDrawable(it)
    }
}

//打勾符号
class TickDrawable(private val lineColor:Int, private val strokeWidthInPx: Float, private val padding:Int=0): TintDrawable() {
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = lineColor
            this.strokeWidth = strokeWidthInPx
            strokeCap = Paint.Cap.SQUARE
            style = Paint.Style.STROKE
        }
    }
    private val path = Path()
    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val saveCount = canvas.save()
        canvas.rotate(-45f, bounds.centerX().toFloat(), bounds.centerY().toFloat())
        canvas.drawPath(path, paint)
        canvas.restoreToCount(saveCount)
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            val rect = Rect(it)
            rect.plus(-padding)
            rect
        }?.let {
            val with = it.width().toFloat()
            val height = it.height().toFloat()
            val left = it.left
            val top = it.top
            path.reset()
            path.moveTo(left+with*0.26f, top+height*0.35f)
            path.lineTo(left+with*0.26f, top+height*0.6f)
            path.lineTo(left+with*0.76f, top+height*0.6f)
        }
    }

    override fun getConstantState(): ConstantState? {
        return object:ConstantState(){
            override fun newDrawable(): Drawable {
                return TickDrawable(lineColor, strokeWidthInPx)
            }

            override fun getChangingConfigurations(): Int{
                return 0
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}

abstract class TintDrawable:Drawable(){
    private var tintMode:PorterDuff.Mode? = null
    protected var tint:ColorStateList? = null
    private var mColorFilter: ColorFilter? = null
    protected var tintFilter: ColorFilter? = null
    private set
    override fun isStateful(): Boolean = true
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    override fun getColorFilter(): ColorFilter? = mColorFilter
    override fun onStateChange(state: IntArray?): Boolean {
        return  if(tintMode == null || tint == null){
            false
        }else{
            tintFilter = updateTintFilter(tint, tintMode)
            invalidateSelf()
            true
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if(mColorFilter != colorFilter) {
            mColorFilter = colorFilter
            invalidateSelf()
        }
    }

    override fun setTintList(tint: ColorStateList?) {
        if(this.tint != tint){
            this.tint = tint
            tintFilter = updateTintFilter(tint, tintMode)
            invalidateSelf()
        }
    }

   override fun setTintMode(tintMode: PorterDuff.Mode?) {
        if(this.tintMode != tintMode) {
            this.tintMode = tintMode
            tintFilter = updateTintFilter(tint, tintMode)
            invalidateSelf()
        }
    }

    fun updateTintFilter(tint: ColorStateList?,
                         tintMode: PorterDuff.Mode?): PorterDuffColorFilter? {
        return if (tint == null || tintMode == null) {
            null
        }else {
            // setMode, setColor of PorterDuffColorFilter are not public method in SDK v7.
            // Therefore we create a new one all the time here. Don't expect this is called often.
            PorterDuffColorFilter(tint.getColorForState(state, Color.TRANSPARENT), tintMode)
        }
    }
}

class RunDrawable(private val state:Int,
                  private val color:Int,
                  private val bgColor:Int): TintDrawable(){
    companion object{
        const val PAUSE = 0
        const val START = 1
        const val STOP = 2
    }
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }
    }
    private val path = Path()
    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val width = bounds.width()
        val height = bounds.height()
        paint.color = bgColor
        canvas.drawCircle(width*0.5f,
                height*0.5f,
                width*0.5f, paint)
        paint.color = color
        canvas.drawPath(path, paint)
        if(state == PAUSE){
            val saveCount = canvas.save()
            canvas.translate(width*0.2f, 0f)
            canvas.drawPath(path, paint)
            canvas.restoreToCount(saveCount)
        }
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            val width = it.width()
            val height = it.height()
            path.reset()
            when(state){
                START ->{
                    val triangleHeight = 2*(0.3f* tan(30f* PI/180f)).toFloat()
                    path.moveTo(width*0.4f, height*(1-triangleHeight)/2)
                    path.lineTo(width*0.4f, height*(1+triangleHeight)/2)
                    path.lineTo(width*0.7f, height*0.5f)
                }
                STOP ->{
                    path.moveTo(width*0.35f, height*0.35f)
                    path.lineTo(width*0.35f, height*0.65f)
                    path.lineTo(width*0.65f, height*0.65f)
                    path.lineTo(width*0.65f, height*0.35f)
                }
                else->{
                    path.moveTo(width*0.35f, height*0.35f)
                    path.lineTo(width*0.35f, height*0.65f)
                    path.lineTo(width*0.45f, height*0.65f)
                    path.lineTo(width*0.45f, height*0.35f)
                }
            }
            path.close()
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    override fun setColorFilter(colorFilter: ColorFilter?) {
       paint.colorFilter = colorFilter
    }
}

class LinkDotsDrawable(private val colors:List<Int>, private val dotSize:Float): TintDrawable(){
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val width = bounds.width()
        val centerY = bounds.centerY().toFloat()
        val radius = dotSize/2
        when(colors.size){
            0->{
                //
            }
            1->{
                paint.color = colors[0]
                canvas.drawCircle(bounds.centerX().toFloat(), centerY, radius, paint)
            }
            else->{
                val dotMargin = (width - dotSize*colors.size)/ (colors.size-1)
                for (i in colors.indices){
                    paint.color = colors[i]
                    canvas.drawCircle(
                        i*(dotSize+dotMargin) + radius, centerY, radius, paint)
                }
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha  = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

}

class ShareMoreDrawable(
        private val color:Int,
        private val bgColor:Int): TintDrawable(){
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha  = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val width = bounds.width()
        val height = bounds.height()
        paint.color = bgColor
        canvas.drawCircle(width*0.5f,
                height*0.5f,
                width*0.5f, paint)
        paint.color = color
        var radius = width*0.068f
        canvas.drawCircle(width*0.5f,
                height*0.5f,
                radius, paint)

        canvas.drawCircle(width*(0.5f-0.227f),
                height*0.5f,
                radius, paint)

        canvas.drawCircle(width*(0.5f+0.227f),
                height*0.5f,
                radius, paint)
    }
}

class PlanDayMoreDrawable(private val lineColor:Int,
                          private val lineWidthInPx:Float,
                          private val intervalInPx:Float): TintDrawable(){
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            color = lineColor
            strokeWidth = lineWidthInPx
        }
    }

    private val rect = RectF()

    override fun setAlpha(alpha: Int) {
        paint.alpha  = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()
        val count = 4
        val itemHeight = (height-(count-1)*intervalInPx)/count
        for (i in 0 until count){
            val top = i*(itemHeight+intervalInPx)
            rect.set(0f, top, width, top+itemHeight)
            rect.inset(lineWidthInPx/2, lineWidthInPx/2)//预留空间给线宽
            canvas.drawRect(rect, paint)
        }
    }
}

fun Context.createStateListDrawable(states:List<IntArray>, drawables:List<Int>, size: Int? = null):StateListDrawable{
    return object :StateListDrawable() {
        init {
            val res = resources
            for (i in states.indices){
                addState(states[i], res.getDrawable(drawables[i]))
            }
        }

        override fun getIntrinsicHeight(): Int {
            return size?:super.getIntrinsicHeight()
        }

        override fun getIntrinsicWidth(): Int {
            return size?:super.getIntrinsicWidth()
        }
    }
}

class PlanProgramProgressDrawable(
    private val typeColor:Int,
    private val progressColor:Int,
    private val cornerRadius:Float,
    private val progressHeight:Float,
    private val progressTopMargin:Float
): TintDrawable(){
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
            color = typeColor
        }
    }

    private val rect = RectF()

    override fun setAlpha(alpha: Int) {
        paint.alpha  = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun draw(canvas: Canvas) {
        paint.colorFilter = if(colorFilter != null)  colorFilter else tintFilter
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        var saveCount = canvas.save()
        rect.set(0f, 0f, width, height-progressTopMargin-progressHeight)
        canvas.clipRect(rect)
        rect.bottom = height
        paint.color = typeColor
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        canvas.restoreToCount(saveCount)

        saveCount = canvas.save()
        rect.top = height-progressHeight
        canvas.clipRect(rect)
        rect.top = 0f
        paint.color = progressColor
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        canvas.restoreToCount(saveCount)
    }
}