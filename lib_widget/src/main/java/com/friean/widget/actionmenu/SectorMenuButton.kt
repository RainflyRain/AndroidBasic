package com.friean.widget.actionmenu

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.annotation.IntDef
import androidx.core.graphics.ColorUtils
import com.friean.widget.R

/**
 * 作者：Rance on 2016/11/10 16:41
 * 邮箱：rance935@163.com
 */
class SectorMenuButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {
    lateinit var buttonDatas: ArrayList<ButtonData>
        private set
    private lateinit var buttonRects: MutableMap<ButtonData, RectF>
    private var buttonEventListener: ButtonEventListener? = null

    private var expanded = false

    private var startAngle = 0f
    private var endAngle = 0f
    private var buttonGapPx = 0
    private var mainButtonRotateDegree = 0
    private var rotateAnimDuration = 0
    private var mainButtonSizePx = 0
    private var subButtonSizePx = 0
    private var mainButtonTextSize = 0
    private var subButtonTextSize = 0
    private var mainButtonTextColor = 0
    private var subButtonTextColor = 0
    private var expandAnimDuration = 0
    private var maskBackgroundColor = 0
    private var buttonElevationPx = 0
    private var isSelectionMode = false
    private var rippleEffect = false
    private var rippleColor = Int.MIN_VALUE
    private var blurBackground = false
    private var blurRadius = 0f

    private var mainShadowBitmap: Bitmap? = null
    private var subShadowBitmap: Bitmap? = null
    var shadowMatrix: Matrix? = null

    private var buttonSideMarginPx = 0

    private var paint: Paint? = null
    private var textPaint: Paint? = null

    private var angleCalculator: AngleCalculator? = null
    private var animating = false
    private var maskAttached = false
    private var expandProgress = 0f
    private var rotateProgress = 0f
    private var expandValueAnimator: ValueAnimator? = null
    private var collapseValueAnimator: ValueAnimator? = null
    private var rotateValueAnimator: ValueAnimator? = null
    private var overshootInterpolator: Interpolator? = null
    private var anticipateInterpolator: Interpolator? = null
    private var ripplePath: Path? = null
    private var rippleInfo: RippleInfo? = null
    private var maskView: MaskView? = null
    private var blur: Blur? = null
    private lateinit var blurImageView: ImageView
    private var blurAnimator: ObjectAnimator? = null
    private var blurListener: Animator.AnimatorListener? = null
    private var pressPointF: PointF? = null
    private var rawButtonRect: Rect? = null
    private var rawButtonRectF: RectF? = null
    private var pressTmpColor = 0
    private var pressInButton = false

    private var checker: QuickClickChecker? = null
    private var checkThreshold = 0

    private class RippleInfo {
        var pressX: Float = 0f
        var pressY: Float = 0f
        var rippleRadius: Float = 0f
        var buttonIndex: Int = 0
        var rippleColor: Int = Int.MIN_VALUE
    }

    init {
        init(context, attrs)
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL

        //得到XML自定义属性
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SectorMenuButton)
        startAngle =
            ta.getInteger(R.styleable.SectorMenuButton_aebStartAngleDegree, DEFAULT_START_ANGLE)
                .toFloat()
        endAngle = ta.getInteger(R.styleable.SectorMenuButton_aebEndAngleDegree, DEFAULT_END_ANGLE)
            .toFloat()

        buttonGapPx = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebButtonGapDp,
            dp2px(context, DEFAULT_BUTTON_GAP_DP.toFloat())
        )
        mainButtonSizePx = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebMainButtonSizeDp,
            dp2px(context, DEFAULT_BUTTON_MAIN_SIZE_DP.toFloat())
        )
        subButtonSizePx = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebSubButtonSizeDp,
            dp2px(context, DEFAULT_BUTTON_SUB_SIZE_DP.toFloat())
        )
        buttonElevationPx = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebButtonElevation,
            dp2px(context, DEFAULT_BUTTON_ELEVATION_DP.toFloat())
        )
        buttonSideMarginPx = buttonElevationPx * 2
        mainButtonTextSize = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebMainButtonTextSizeSp,
            sp2px(context, DEFAULT_BUTTON_TEXT_SIZE_SP.toFloat())
        )
        subButtonTextSize = ta.getDimensionPixelSize(
            R.styleable.SectorMenuButton_aebSubButtonTextSizeSp,
            sp2px(context, DEFAULT_BUTTON_TEXT_SIZE_SP.toFloat())
        )
        mainButtonTextColor = ta.getColor(
            R.styleable.SectorMenuButton_aebMainButtonTextColor,
            DEFAULT_BUTTON_TEXT_COLOR
        )
        subButtonTextColor = ta.getColor(
            R.styleable.SectorMenuButton_aebSubButtonTextColor,
            DEFAULT_BUTTON_TEXT_COLOR
        )

        expandAnimDuration = ta.getInteger(
            R.styleable.SectorMenuButton_aebAnimDurationMillis,
            DEFAULT_EXPAND_ANIMATE_DURATION
        )
        rotateAnimDuration = ta.getInteger(
            R.styleable.SectorMenuButton_aebMainButtonRotateAnimDurationMillis,
            DEFAULT_ROTATE_ANIMATE_DURATION
        )
        maskBackgroundColor = ta.getInteger(
            R.styleable.SectorMenuButton_aebMaskBackgroundColor,
            DEFAULT_MASK_BACKGROUND_COLOR
        )
        mainButtonRotateDegree = ta.getInteger(
            R.styleable.SectorMenuButton_aebMainButtonRotateDegree,
            mainButtonRotateDegree
        )
        isSelectionMode = ta.getBoolean(R.styleable.SectorMenuButton_aebIsSelectionMode, false)
        rippleEffect = ta.getBoolean(R.styleable.SectorMenuButton_aebRippleEffect, true)
        rippleColor = ta.getColor(R.styleable.SectorMenuButton_aebRippleColor, rippleColor)
        blurBackground = ta.getBoolean(R.styleable.SectorMenuButton_aebBlurBackground, false)
        blurRadius =
            ta.getFloat(R.styleable.SectorMenuButton_aebBlurRadius, DEFAULT_BLUR_RADIUS.toFloat())
        ta.recycle()

        //模糊处理
        if (blurBackground) {
            blur = Blur()
            blurImageView = ImageView(getContext())
        }

        checkThreshold = if (mainButtonRotateDegree != 0) {
            if (expandAnimDuration > rotateAnimDuration) expandAnimDuration else rotateAnimDuration
        } else {
            expandAnimDuration
        }
        checker = QuickClickChecker(checkThreshold)

        rippleInfo = RippleInfo()
        pressPointF = PointF()
        rawButtonRect = Rect()
        rawButtonRectF = RectF()
        shadowMatrix = Matrix()

        initViewTreeObserver()
        initAnimators()
    }

    /**
     * 注册监听视图树的观察者(observer)
     */
    private fun initViewTreeObserver() {
        val observer = viewTreeObserver
        observer.addOnGlobalLayoutListener {
            getGlobalVisibleRect(rawButtonRect)
            rawButtonRectF!![rawButtonRect!!.left.toFloat(), rawButtonRect!!.top.toFloat(), rawButtonRect!!.right.toFloat()] =
                rawButtonRect!!.bottom.toFloat()
        }
    }

    /**
     * 初始化动画
     */
    private fun initAnimators() {
        overshootInterpolator = OvershootInterpolator()
        anticipateInterpolator = AnticipateInterpolator()

        //打开菜单动画
        expandValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        expandValueAnimator?.setDuration(expandAnimDuration.toLong())
        expandValueAnimator?.setInterpolator(overshootInterpolator)
        expandValueAnimator?.addUpdateListener(this)
        expandValueAnimator?.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationStart(animator: Animator) {
                animating = true
                attachMask()
            }

            override fun onAnimationEnd(animator: Animator) {
                animating = false
                expanded = true
            }
        })

        //关闭菜单动画
        collapseValueAnimator = ValueAnimator.ofFloat(1f, 0f)
        collapseValueAnimator?.setDuration(expandAnimDuration.toLong())
        collapseValueAnimator?.setInterpolator(anticipateInterpolator)
        collapseValueAnimator?.addUpdateListener(this)
        collapseValueAnimator?.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationStart(animator: Animator) {
                animating = true
                hideBlur()
                maskView!!.reset()
            }

            override fun onAnimationEnd(animator: Animator) {
                animating = false
                expanded = false
                if (rotateValueAnimator == null) {
                    detachMask()
                } else {
                    if (expandAnimDuration >= rotateAnimDuration) {
                        detachMask()
                    }
                }
            }
        })

        if (mainButtonRotateDegree == 0) {
            return
        }

        //主菜单旋转动画
        rotateValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        rotateValueAnimator?.setDuration(rotateAnimDuration.toLong())
        rotateValueAnimator?.addUpdateListener(this)
        rotateValueAnimator?.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationEnd(animator: Animator) {
                if (!expanded && expandAnimDuration < rotateAnimDuration) {
                    detachMask()
                }
            }
        })
    }

    //所有按钮的监听事件
    fun setButtonEventListener(listener: ButtonEventListener?) {
        buttonEventListener = listener
    }

    //设置打开菜单的插值器
    fun setExpandAnimatorInterpolator(interpolator: Interpolator?) {
        if (interpolator != null) {
            expandValueAnimator!!.interpolator = interpolator
        }
    }

    //设置关闭菜单的插值器
    fun setCollapseAnimatorInterpolator(interpolator: Interpolator?) {
        if (interpolator != null) {
            collapseValueAnimator!!.interpolator = interpolator
        }
    }

    //按钮初始化
    fun setButtonDatas(buttonDatas: List<ButtonData>?): SectorMenuButton {
        if (buttonDatas == null || buttonDatas.isEmpty()) {
            return this
        }
        this.buttonDatas = ArrayList(buttonDatas)
        if (isSelectionMode) {
            try {
                this.buttonDatas!!.add(0, buttonDatas[0].clone() as ButtonData)
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }
        }

        buttonRects = HashMap(this.buttonDatas!!.size)
        var i = 0
        val size = this.buttonDatas!!.size
        while (i < size) {
            val buttonData = this.buttonDatas!!.get(i)
//            buttonData.setIsMainButton(i == 0)
            val buttonSizePx = if (buttonData.isMainButton) mainButtonSizePx else subButtonSizePx
            val rectF = RectF(
                buttonSideMarginPx.toFloat(),
                buttonSideMarginPx.toFloat(),
                (buttonSizePx + buttonSideMarginPx).toFloat(),
                (buttonSizePx + buttonSideMarginPx).toFloat()
            )
            buttonRects[buttonData] = rectF
            i++
        }
        angleCalculator = AngleCalculator(startAngle, endAngle, this.buttonDatas.size - 1)
        return this
    }

    private val mainButtonData: ButtonData
        get() = buttonDatas!![0]

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = mainButtonSizePx + buttonSideMarginPx * 2
        val desiredHeight = mainButtonSizePx + buttonSideMarginPx * 2

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawButton(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initButtonInfo()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        pressPointF!![event.rawX] = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (checker!!.isQuick) {
                    return false
                }
                pressInButton = true
                val executeActionUp = !animating && buttonDatas != null && !buttonDatas!!.isEmpty()
                if (executeActionUp) {
                    updatePressState(0, true)
                }
                return executeActionUp
            }

            MotionEvent.ACTION_MOVE -> updatePressPosition(0, rawButtonRectF)
            MotionEvent.ACTION_UP -> {
                if (!isPointInRectF(pressPointF, rawButtonRectF)) {
                    return true
                }
                updatePressState(0, false)
                expand()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 更新按钮位置
     * @param buttonIndex
     * @param rectF
     */
    private fun updatePressPosition(buttonIndex: Int, rectF: RectF?) {
        if (buttonIndex < 0) {
            return
        }
        if (isPointInRectF(pressPointF, rectF)) {
            if (!pressInButton) {
                updatePressState(buttonIndex, true)
                pressInButton = true
            }
        } else {
            if (pressInButton) {
                updatePressState(buttonIndex, false)
                pressInButton = false
            }
        }
    }

    private fun isPointInRectF(pointF: PointF?, rectF: RectF?): Boolean {
        return pointF!!.x >= rectF!!.left && pointF.x <= rectF.right && pointF.y >= rectF.top && pointF.y <= rectF.bottom
    }

    //更新按钮的状态
    private fun updatePressState(buttonIndex: Int, down: Boolean) {
        if (buttonIndex < 0) {
            return
        }
        val buttonData = buttonDatas!![buttonIndex]
        if (down) {
            pressTmpColor = buttonData.backgroundColor
            buttonData.backgroundColor = getPressedColor(pressTmpColor)
        } else {
            buttonData.backgroundColor = pressTmpColor
        }
        if (expanded) {
            maskView!!.invalidate()
        } else {
            invalidate()
        }
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        if (valueAnimator === expandValueAnimator || valueAnimator === collapseValueAnimator) {
            expandProgress = valueAnimator.animatedValue as Float
        }
        if (valueAnimator === rotateValueAnimator) {
            rotateProgress = valueAnimator.animatedValue as Float
        }
        if (maskAttached) {
            maskView!!.updateButtons()
            maskView!!.invalidate()
        }
    }

    //打开菜单
    fun expand() {
        if (expandValueAnimator!!.isRunning) {
            expandValueAnimator!!.cancel()
        }
        expandValueAnimator!!.start()
        startRotateAnimator(true)
        if (buttonEventListener != null) {
            buttonEventListener!!.onExpand()
        }
    }

    //关闭菜单
    fun collapse() {
        if (collapseValueAnimator!!.isRunning) {
            collapseValueAnimator!!.cancel()
        }
        collapseValueAnimator!!.start()
        startRotateAnimator(false)
        if (buttonEventListener != null) {
            buttonEventListener!!.onCollapse()
        }
    }

    //主菜单旋转动画
    private fun startRotateAnimator(expand: Boolean) {
        if (rotateValueAnimator != null) {
            if (rotateValueAnimator!!.isRunning) {
                rotateValueAnimator!!.cancel()
            }
            if (expand) {
                rotateValueAnimator!!.interpolator = overshootInterpolator
                rotateValueAnimator!!.setFloatValues(0f, 1f)
            } else {
                rotateValueAnimator!!.interpolator = anticipateInterpolator
                rotateValueAnimator!!.setFloatValues(1f, 0f)
            }
            rotateValueAnimator!!.start()
        }
    }

    //更新底层view
    private fun attachMask() {
        if (maskView == null) {
            maskView = MaskView(context, this)
        }

        if (!maskAttached && !showBlur()) {
            val root = rootView as ViewGroup
            root.addView(maskView)
            maskAttached = true
            maskView!!.reset()
            maskView!!.initButtonRect()
            maskView!!.onClickMainButton()
        }
    }

    /**
     * 模糊处理
     * @return
     */
    private fun showBlur(): Boolean {
        if (!blurBackground) {
            return false
        }

        visibility = INVISIBLE

        val root = rootView as ViewGroup
        root.isDrawingCacheEnabled = true
        val bitmap = root.drawingCache
        checkBlurRadius()

        blur!!.setParams(object : Blur.Callback {
            override fun onBlurred(blurredBitmap: Bitmap?) {
                blurImageView!!.setImageBitmap(blurredBitmap)
                root.isDrawingCacheEnabled = false
                root.addView(
                    blurImageView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )

                blurAnimator = ObjectAnimator.ofFloat(blurImageView, "alpha", 0.0f, 1.0f)
                    .setDuration(expandAnimDuration.toLong())
                if (blurListener != null) {
                    blurAnimator!!.removeListener(blurListener)
                }
                blurAnimator!!.start()

                root.addView(maskView)
                maskAttached = true
                maskView!!.reset()
                maskView!!.initButtonRect()
                maskView!!.onClickMainButton()
            }

        }, context, bitmap, blurRadius)
        blur!!.execute()

        return true
    }

    /**
     * 检查模糊处理的Radius，必须在0~25之间
     */
    private fun checkBlurRadius() {
        if (blurRadius <= 0 || blurRadius > 25) {
            blurRadius = DEFAULT_BLUR_RADIUS.toFloat()
        }
    }

    /**
     * 隐藏模糊处理
     */
    private fun hideBlur() {
        if (!blurBackground) {
            return
        }

        visibility = VISIBLE

        val root = rootView as ViewGroup
        blurAnimator!!.setFloatValues(1.0f, 0.0f)
        if (blurListener == null) {
            blurListener = object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animator: Animator) {
                    root.removeView(blurImageView)
                }
            }
        }
        blurAnimator!!.addListener(blurListener)
        blurAnimator!!.start()
    }

    /**
     * 隐藏底层view
     */
    private fun detachMask() {
        if (maskAttached) {
            val root = rootView as ViewGroup
            root.removeView(maskView)
            maskAttached = false
            for (i in buttonDatas!!.indices) {
                val buttonData = buttonDatas!![i]
                val rectF = buttonRects!![buttonData]
                val size = if (buttonData.isMainButton) mainButtonSizePx else subButtonSizePx
                rectF!![buttonSideMarginPx.toFloat(), buttonSideMarginPx.toFloat(), (buttonSideMarginPx + size).toFloat()] =
                    (buttonSideMarginPx + size).toFloat()
            }
        }
        invalidate()
    }

    /**
     * 重置按钮水波纹效果
     */
    private fun resetRippleInfo() {
        rippleInfo!!.buttonIndex = Int.MIN_VALUE
        rippleInfo!!.pressX = 0f
        rippleInfo!!.pressY = 0f
        rippleInfo!!.rippleRadius = 0f
    }

    /**
     * 绘制主菜单
     * @param canvas
     */
    private fun drawButton(canvas: Canvas) {
        if (buttonDatas == null || buttonDatas!!.isEmpty()) {
            return
        }

        val buttonData = mainButtonData
        drawButton(canvas, paint, buttonData)
    }

    /**
     * 绘制指定按钮
     * @param canvas
     */
    private fun drawButton(canvas: Canvas, paint: Paint?, buttonData: ButtonData) {
        drawShadow(canvas, paint, buttonData)
        drawContent(canvas, paint, buttonData)
        drawRipple(canvas, paint, buttonData)
    }

    /**
     * 绘制阴影效果
     * @param canvas
     * @param paint
     * @param buttonData
     */
    private fun drawShadow(canvas: Canvas, paint: Paint?, buttonData: ButtonData) {
        if (buttonElevationPx <= 0) {
            return
        }

        val left: Float
        val top: Float
        val bitmap: Bitmap?
        if (buttonData.isMainButton) {
            mainShadowBitmap = getButtonShadowBitmap(buttonData)
            bitmap = mainShadowBitmap
        } else {
            subShadowBitmap = getButtonShadowBitmap(buttonData)
            bitmap = subShadowBitmap
        }

        val shadowOffset = buttonElevationPx / 2
        val rectF = buttonRects!![buttonData]
        left = rectF!!.centerX() - bitmap!!.width / 2
        top = rectF.centerY() - bitmap.height / 2 + shadowOffset
        shadowMatrix!!.reset()
        if (!buttonData.isMainButton) {
            shadowMatrix!!.postScale(
                expandProgress,
                expandProgress,
                (bitmap.width / 2).toFloat(),
                (bitmap.height / 2 + shadowOffset).toFloat()
            )
        }
        shadowMatrix!!.postTranslate(left, top)
        if (buttonData.isMainButton) {
            shadowMatrix!!.postRotate(
                -mainButtonRotateDegree * rotateProgress,
                rectF.centerX(),
                rectF.centerY()
            )
        }
        paint!!.alpha = 255
        canvas.drawBitmap(bitmap, shadowMatrix!!, paint)
    }

    /**
     * 绘制菜单按钮的内容（icon 文字）
     * @param canvas
     * @param paint
     * @param buttonData
     */
    private fun drawContent(canvas: Canvas, paint: Paint?, buttonData: ButtonData) {
        paint!!.alpha = 255
        paint.color = buttonData.backgroundColor
        val rectF = buttonRects!![buttonData]
        canvas.drawOval(rectF!!, paint)
        if (buttonData.isIconButton()) {
            val drawable = buttonData.icon
                ?: throw IllegalArgumentException("iconData is true, drawable cannot be null")
            val left = rectF.left.toInt() + dp2px(context, buttonData.iconPaddingDp)
            val right = rectF.right.toInt() - dp2px(context, buttonData.iconPaddingDp)
            val top = rectF.top.toInt() + dp2px(context, buttonData.iconPaddingDp)
            val bottom = rectF.bottom.toInt() - dp2px(context, buttonData.iconPaddingDp)
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        } else {
            requireNotNull(buttonData.texts) { "iconData is false, text cannot be null" }
            val texts = buttonData.texts
            val sizePx = if (buttonData.isMainButton) mainButtonTextSize else subButtonTextSize
            val textColor = if (buttonData.isMainButton) mainButtonTextColor else subButtonTextColor
            textPaint = getTextPaint(sizePx, textColor)
            drawTexts(texts, canvas, rectF.centerX(), rectF.centerY())
        }
    }

    /**
     * 绘制文字
     * @param strings
     * @param canvas
     * @param x
     * @param y
     */
    private fun drawTexts(strings: Array<String?>?, canvas: Canvas, x: Float, y: Float) {
        val fontMetrics = textPaint!!.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        val length = strings!!.size
        val total = (length - 1) * (-top + bottom) + (-fontMetrics.ascent + fontMetrics.descent)
        val offset = total / 2 - bottom
        for (i in 0 until length) {
            val yAxis = -(length - i - 1) * (-top + bottom) + offset
            canvas.drawText(strings[i]!!, x, y + yAxis, textPaint!!)
        }
    }

    /**
     * 绘制水波纹效果
     * @param canvas
     * @param paint
     * @param buttonData
     */
    private fun drawRipple(canvas: Canvas, paint: Paint?, buttonData: ButtonData) {
        val pressIndex = buttonDatas!!.indexOf(buttonData)
        if (!rippleEffect || pressIndex == -1 || pressIndex != rippleInfo!!.buttonIndex) {
            return
        }

        paint!!.color = rippleInfo!!.rippleColor
        paint.alpha = 128
        canvas.save()
        if (ripplePath == null) {
            ripplePath = Path()
        }
        ripplePath!!.reset()
        val rectF = buttonRects!![buttonData]
        val radius = rectF!!.right - rectF.centerX()
        ripplePath!!.addCircle(rectF.centerX(), rectF.centerY(), radius, Path.Direction.CW)
        canvas.clipPath(ripplePath!!)
        canvas.drawCircle(
            rippleInfo!!.pressX,
            rippleInfo!!.pressY,
            rippleInfo!!.rippleRadius,
            paint
        )
        canvas.restore()
    }

    /**
     * 获取按钮阴影效果Bitmap
     * @param buttonData
     * @return
     */
    private fun getButtonShadowBitmap(buttonData: ButtonData): Bitmap? {
        if (buttonData.isMainButton) {
            if (mainShadowBitmap != null) {
                return mainShadowBitmap
            }
        } else {
            if (subShadowBitmap != null) {
                return subShadowBitmap
            }
        }

        val buttonSizePx = if (buttonData.isMainButton) mainButtonSizePx else subButtonSizePx
        val buttonRadius = buttonSizePx / 2
        val bitmapRadius = buttonRadius + buttonElevationPx
        val bitmapSize = bitmapRadius * 2
        val bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(0x0)
        val colors = intArrayOf(
            ColorUtils.setAlphaComponent(BUTTON_SHADOW_COLOR, BUTTON_SHADOW_ALPHA),
            ColorUtils.setAlphaComponent(BUTTON_SHADOW_COLOR, 0)
        )
        val stops =
            floatArrayOf((buttonRadius - buttonElevationPx).toFloat() / bitmapRadius.toFloat(), 1f)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(
            RadialGradient(
                bitmapRadius.toFloat(),
                bitmapRadius.toFloat(),
                bitmapRadius.toFloat(),
                colors,
                stops,
                Shader.TileMode.CLAMP
            )
        )
        val canvas = Canvas(bitmap)
        canvas.drawRect(0f, 0f, bitmapSize.toFloat(), bitmapSize.toFloat(), paint)
        if (buttonData.isMainButton) {
            mainShadowBitmap = bitmap
            return mainShadowBitmap
        } else {
            subShadowBitmap = bitmap
            return subShadowBitmap
        }
    }

    private fun getTextPaint(sizePx: Int, color: Int): Paint {
        if (textPaint == null) {
            textPaint = Paint()
            textPaint!!.isAntiAlias = true
            textPaint!!.textAlign = Paint.Align.CENTER
        }

        textPaint!!.textSize = sizePx.toFloat()
        textPaint!!.color = color
        return textPaint as Paint
    }

    private fun initButtonInfo() {
        val root = rootView as ViewGroup
        getGlobalVisibleRect(rawButtonRect)
        rawButtonRectF!![rawButtonRect!!.left.toFloat(), rawButtonRect!!.top.toFloat(), rawButtonRect!!.right.toFloat()] =
            rawButtonRect!!.bottom.toFloat()
    }

    private fun getLighterColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 1.1f
        return Color.HSVToColor(hsv)
    }

    private fun getDarkerColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.9f
        return Color.HSVToColor(hsv)
    }

    private fun getPressedColor(color: Int): Int {
        return getDarkerColor(color)
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    @SuppressLint("ViewConstructor")
    private class MaskView(context: Context?, private val SectorMenuButton: SectorMenuButton) :
        View(context) {
        private val initialSubButtonRectF: RectF
        private val touchRectF: RectF
        private val touchRippleAnimator: ValueAnimator
        private val paint = Paint()
        private val expandDesCoordinateMap: MutableMap<ButtonData, ExpandMoveCoordinate>

        @get:RippleState
        private var rippleState = 0
        private val rippleRadius = 0f
        private var clickIndex = 0
        private val matrixArray: Array<Matrix?>
        private val checker = QuickClickChecker(SectorMenuButton.checkThreshold)

        @Retention(AnnotationRetention.SOURCE)
        private annotation class RippleState

        private class ExpandMoveCoordinate(var moveX: Float, var moveY: Float)

        init {
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true

            matrixArray = arrayOfNulls(SectorMenuButton.buttonDatas!!.size)
            for (i in matrixArray.indices) {
                matrixArray[i] = Matrix()
            }

            initialSubButtonRectF = RectF()
            touchRectF = RectF()

            expandDesCoordinateMap = HashMap(
                SectorMenuButton.buttonDatas!!.size
            )
            setBackgroundColor(SectorMenuButton.maskBackgroundColor)

            touchRippleAnimator = ValueAnimator.ofFloat(0f, 1f)
            touchRippleAnimator.setDuration((SectorMenuButton.expandAnimDuration.toFloat() * 0.9f).toLong())
            touchRippleAnimator.addUpdateListener { valueAnimator ->
                val animateProgress = valueAnimator.animatedValue as Float
                SectorMenuButton.rippleInfo!!.rippleRadius = rippleRadius * animateProgress
            }
            touchRippleAnimator.addListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animator: Animator) {
                    SectorMenuButton.rippleInfo!!.rippleRadius = 0f
                    rippleState = RIPPLED
                }
            })
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val root = rootView
            setMeasuredDimension(root.width, root.height)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            drawButtons(canvas, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            SectorMenuButton.pressPointF!![event.x] = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (checker.isQuick) {
                        return false
                    }
                    clickIndex = touchedButtonIndex
                    if (SectorMenuButton.expanded) {
                        SectorMenuButton.updatePressState(clickIndex, true)
                    }
                    SectorMenuButton.pressInButton = true
                    return SectorMenuButton.expanded
                }

                MotionEvent.ACTION_MOVE -> SectorMenuButton.updatePressPosition(
                    clickIndex,
                    touchRectF
                )

                MotionEvent.ACTION_UP -> {
                    if (!SectorMenuButton.isPointInRectF(
                            SectorMenuButton.pressPointF,
                            touchRectF
                        )
                    ) {
                        if (clickIndex < 0) {
                            SectorMenuButton.collapse()
                        }
                        return true
                    }
                    SectorMenuButton.updatePressState(clickIndex, false)
                    onButtonPressed()
                }
            }
            return super.onTouchEvent(event)
        }

        fun reset() {
            rippleState = IDLE
        }

        fun onClickMainButton() {
            clickIndex = 0
        }

        private fun onButtonPressed() {
            if (SectorMenuButton.buttonEventListener != null) {
                if (clickIndex > 0) {
                    SectorMenuButton.buttonEventListener!!.onButtonClicked(clickIndex)
                }
            }
            if (SectorMenuButton.isSelectionMode) {
                if (clickIndex > 0) {
                    val buttonData = SectorMenuButton.buttonDatas!![clickIndex]
                    val mainButton = SectorMenuButton.mainButtonData
                    if (buttonData.isIconButton()) {
                        mainButton.setIsIconButton(true)
                        mainButton.icon = buttonData.icon
                    } else {
                        mainButton.setIsIconButton(false)
                        mainButton.texts = buttonData.texts
                    }
                    mainButton.backgroundColor = buttonData.backgroundColor
                }
            }
            SectorMenuButton.collapse()
        }

        private val touchedButtonIndex: Int
            get() {
                for (i in SectorMenuButton.buttonDatas!!.indices) {
                    val buttonData = SectorMenuButton.buttonDatas!![i]
                    val coordinate = expandDesCoordinateMap[buttonData]
                    if (i == 0) {
                        val rectF = SectorMenuButton.buttonRects!![buttonData]
                        touchRectF.set(rectF!!)
                    } else {
                        touchRectF.set(initialSubButtonRectF)
                        touchRectF.offset(coordinate!!.moveX, -coordinate.moveY)
                    }

                    if (SectorMenuButton.isPointInRectF(SectorMenuButton.pressPointF, touchRectF)) {
                        return i
                    }
                }
                return -1
            }

        fun initButtonRect() {
            for (i in SectorMenuButton.buttonDatas!!.indices) {
                val buttonData = SectorMenuButton.buttonDatas!![i]
                val rectF = SectorMenuButton.buttonRects!![buttonData]
                if (i == 0) {
                    rectF!!.left =
                        SectorMenuButton.rawButtonRectF!!.left + SectorMenuButton.buttonSideMarginPx
                    rectF.right =
                        SectorMenuButton.rawButtonRectF!!.right - SectorMenuButton.buttonSideMarginPx
                    rectF.top =
                        SectorMenuButton.rawButtonRectF!!.top + SectorMenuButton.buttonSideMarginPx
                    rectF.bottom =
                        SectorMenuButton.rawButtonRectF!!.bottom - SectorMenuButton.buttonSideMarginPx
                } else {
                    val leftTmp = rectF!!.left
                    val topTmp = rectF.top
                    val buttonRadius = SectorMenuButton.subButtonSizePx / 2
                    rectF.left =
                        leftTmp + SectorMenuButton.rawButtonRectF!!.centerX() - SectorMenuButton.buttonSideMarginPx - buttonRadius
                    rectF.right =
                        leftTmp + SectorMenuButton.rawButtonRectF!!.centerX() - SectorMenuButton.buttonSideMarginPx + buttonRadius
                    rectF.top =
                        topTmp + SectorMenuButton.rawButtonRectF!!.centerY() - SectorMenuButton.buttonSideMarginPx - buttonRadius
                    rectF.bottom =
                        topTmp + SectorMenuButton.rawButtonRectF!!.centerY() - SectorMenuButton.buttonSideMarginPx + buttonRadius
                    initialSubButtonRectF.set(rectF)
                    touchRectF.set(rectF)
                }
            }
        }

        fun updateButtons() {
            val buttonDatas = SectorMenuButton.buttonDatas
            val mainButtonRadius = SectorMenuButton.mainButtonSizePx / 2
            val subButtonRadius = SectorMenuButton.subButtonSizePx / 2
            var matrix = matrixArray[0]
            matrix!!.reset()
            matrix.postRotate(
                SectorMenuButton.mainButtonRotateDegree * SectorMenuButton.rotateProgress,
                SectorMenuButton.rawButtonRectF!!.centerX(),
                SectorMenuButton.rawButtonRectF!!.centerY()
            )
            for (i in 1 until buttonDatas!!.size) {
                matrix = matrixArray[i]
                val buttonData = buttonDatas[i]
                matrix!!.reset()
                if (SectorMenuButton.expanded) {
                    val coordinate = expandDesCoordinateMap[buttonData]
                    val dx = SectorMenuButton.expandProgress * (coordinate!!.moveX)
                    val dy = SectorMenuButton.expandProgress * (-coordinate.moveY)
                    matrix.postTranslate(dx, dy)
                } else {
                    val radius = mainButtonRadius + subButtonRadius + SectorMenuButton.buttonGapPx
                    var moveX: Float
                    var moveY: Float
                    var coordinate = expandDesCoordinateMap[buttonData]
                    if (coordinate == null) {
                        moveX = SectorMenuButton.angleCalculator!!.getMoveX(radius, i).toFloat()
                        moveY = SectorMenuButton.angleCalculator!!.getMoveY(radius, i).toFloat()
                        coordinate = ExpandMoveCoordinate(moveX, moveY)
                        expandDesCoordinateMap[buttonData] = coordinate
                    } else {
                        moveX = coordinate.moveX
                        moveY = coordinate.moveY
                    }
                    val dx = SectorMenuButton.expandProgress * (moveX)
                    val dy = SectorMenuButton.expandProgress * (-moveY)
                    matrix.postTranslate(dx, dy)
                }
            }
        }

        private fun drawButtons(canvas: Canvas, paint: Paint) {
            for (i in SectorMenuButton.buttonDatas!!.indices.reversed()) {
                canvas.save()
                canvas.concat(matrixArray[i])
                val buttonData = SectorMenuButton.buttonDatas!![i]
                SectorMenuButton.drawButton(canvas, paint, buttonData)
                canvas.restore()
            }
        }

        companion object {
            const val IDLE = 0
            const val RIPPLING = 1
            const val RIPPLED = 2
        }
    }

    private open class SimpleAnimatorListener : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
        }

        override fun onAnimationEnd(animator: Animator) {
        }

        override fun onAnimationCancel(animator: Animator) {
        }

        override fun onAnimationRepeat(animator: Animator) {
        }
    }

    companion object {
        private const val BUTTON_SHADOW_COLOR = -0x1000000
        private const val BUTTON_SHADOW_ALPHA = 32

        //初始化ButtonData默认值
        private const val DEFAULT_EXPAND_ANIMATE_DURATION = 225
        private const val DEFAULT_ROTATE_ANIMATE_DURATION = 300
        private const val DEFAULT_BUTTON_GAP_DP = 25
        private const val DEFAULT_BUTTON_MAIN_SIZE_DP = 60
        private const val DEFAULT_BUTTON_SUB_SIZE_DP = 60
        private const val DEFAULT_BUTTON_ELEVATION_DP = 4
        private const val DEFAULT_BUTTON_TEXT_SIZE_SP = 20
        private const val DEFAULT_START_ANGLE = 90
        private const val DEFAULT_END_ANGLE = 90
        private const val DEFAULT_BUTTON_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_MASK_BACKGROUND_COLOR = Color.TRANSPARENT
        private const val DEFAULT_BLUR_RADIUS = 10
    }
}
