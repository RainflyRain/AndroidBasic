package com.friean.widget

/**
 * Created by zpf on 2022/5/16.
 */
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * 支持shape背景的RelativeLayout
 */
class ShapeRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    // 默认阴影值
    private val DEFAULT_ELEVATION = 4f

    /**
     * shape模式
     * 矩形（rectangle）、椭圆形(oval)、线形(line)、环形(ring)
     */
    private var mShapeMode = 0

    /**
     * 填充颜色
     */
    private var mFillColor = 0

    /**
     * 描边颜色
     */
    private var mStrokeColor = 0

    /**
     * 描边宽度
     */
    private var mStrokeWidth = 0

    /**
     * 圆角半径
     */
    private var mCornerRadius = 0
    /**
     * 圆角位置
     * topLeft、topRight、bottomRight、bottomLeft
     */
    private var mCornerPosition = -1

    /**
     * 起始颜色
     */
    private var mStartColor = 0

    /**
     * 结束颜色
     */
    private var mEndColor = 0

    /**
     * 渐变方向
     * 0-GradientDrawable.Orientation.TOP_BOTTOM
     * 1-GradientDrawable.Orientation.LEFT_RIGHT
     */
    private var mOrientation = 0

    /**
     * 阴影效果
     */
    private var mWithElevation = false

    /**
     * shape样式
     */
    private val mGradientDrawable: GradientDrawable by lazy { GradientDrawable() }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ShapeRelativeLayout).apply {
            mShapeMode = getInt(R.styleable.ShapeRelativeLayout_sp_shapeMode, 0)
            mFillColor = getColor(R.styleable.ShapeRelativeLayout_sp_fillColor, 0xFFFFFFFF.toInt())
            mStrokeColor = getColor(R.styleable.ShapeRelativeLayout_sp_strokeColor, 0)
            mStrokeWidth = getDimensionPixelSize(R.styleable.ShapeRelativeLayout_sp_strokeWidth, 0)
            mCornerRadius = getDimensionPixelSize(R.styleable.ShapeRelativeLayout_sp_cornerRadius, 0)
            mCornerPosition = getInt(R.styleable.ShapeRelativeLayout_sp_cornerPosition, -1)
            mStartColor = getColor(R.styleable.ShapeRelativeLayout_sp_startColor, 0xFFFFFFFF.toInt())
            mEndColor = getColor(R.styleable.ShapeRelativeLayout_sp_endColor, 0xFFFFFFFF.toInt())
            mOrientation = getColor(R.styleable.ShapeRelativeLayout_sp_orientation, 0)
            mWithElevation = getBoolean(R.styleable.ShapeRelativeLayout_sp_withElevation, false)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 初始化shape
        with(mGradientDrawable) {
            // 渐变色
            if (mStartColor != 0xFFFFFFFF.toInt() && mEndColor != 0xFFFFFFFF.toInt()) {
                colors = intArrayOf(mStartColor, mEndColor)
                when (mOrientation) {
                    0 -> orientation = GradientDrawable.Orientation.TOP_BOTTOM
                    1 -> orientation = GradientDrawable.Orientation.LEFT_RIGHT
                }
            }
            // 填充色
            else {
                setColor(mFillColor)
            }
            when (mShapeMode) {
                0 -> shape = GradientDrawable.RECTANGLE
                1 -> shape = GradientDrawable.OVAL
                2 -> shape = GradientDrawable.LINE
                3 -> shape = GradientDrawable.RING
            }
            // 统一设置圆角半径
            if (mCornerPosition == -1) {
                cornerRadius = mCornerRadius.toFloat()
            }
            // 根据圆角位置设置圆角半径
            else {
                cornerRadii = getCornerRadiusByPosition()
            }
            // 默认的透明边框不绘制
            if (mStrokeColor != 0) {
                setStroke(mStrokeWidth, mStrokeColor)
            }
        }

        // 设置背景
        background = mGradientDrawable

        // 5.0以上设置阴影
        if (mWithElevation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = DEFAULT_ELEVATION
        }
    }

    /**
     * 根据圆角位置获取圆角半径
     */
    private fun getCornerRadiusByPosition(): FloatArray {
        val result = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        val cornerRadius = mCornerRadius.toFloat()
        if (containsFlag(mCornerPosition, TOP_LEFT)) {
            result[0] = cornerRadius
            result[1] = cornerRadius
        }
        if (containsFlag(mCornerPosition, TOP_RIGHT)) {
            result[2] = cornerRadius
            result[3] = cornerRadius
        }
        if (containsFlag(mCornerPosition, BOTTOM_RIGHT)) {
            result[4] = cornerRadius
            result[5] = cornerRadius
        }
        if (containsFlag(mCornerPosition, BOTTOM_LEFT)) {
            result[6] = cornerRadius
            result[7] = cornerRadius
        }
        return result
    }

    /**
     * 是否包含对应flag
     * 按位或
     */
    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    companion object {
        const val TOP_LEFT = 1
        const val TOP_RIGHT = 2
        const val BOTTOM_RIGHT = 4
        const val BOTTOM_LEFT = 8
    }
}