package com.friean.widget.actionmenu

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable

/**
 * 作者：Rance on 2016/11/10 16:41
 * 邮箱：rance935@163.com
 */
class ButtonData private constructor(//true if the button use icon resource,else string resource
    private var iconButton: Boolean
) : Cloneable {
    var isMainButton: Boolean =
        false //main button is the button you see when buttons are all collapsed

    lateinit var texts: Array<String?> //String array that you want to show at button center,texts[i] will be shown at the ith row
    var icon: Drawable? = null //icon drawable that will be shown at button center
    var iconPaddingDp:Float = 0f //the padding of the icon drawable in button
    var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR //the background color of the button

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        val buttonData = super.clone() as ButtonData
        buttonData.setIsIconButton(this.iconButton)
        buttonData.backgroundColor = backgroundColor
        buttonData.isMainButton = isMainButton
        buttonData.icon = icon
        buttonData.iconPaddingDp = (this.iconPaddingDp)
        buttonData.texts = texts
        return buttonData
    }

    fun setIsIconButton(isIconButton: Boolean) {
        iconButton = isIconButton
    }

    fun setText(vararg text: String?) {
        this.texts = arrayOfNulls(text.size)
        var i = 0
        val length = text.size
        while (i < length) {
            texts!![i] = text[i]
            i++
        }
    }

    fun setIconResId(context: Context, iconResId: Int) {
        this.icon = context.resources.getDrawable(iconResId)
    }

    fun isIconButton(): Boolean {
        return iconButton
    }

    fun setBackgroundColorId(context: Context, backgroundColorId: Int) {
        this.backgroundColor = context.resources.getColor(backgroundColorId)
    }

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.WHITE

        fun buildTextButton(vararg text: String?): ButtonData {
            val buttonData = ButtonData(false)
            buttonData.iconButton = false
            buttonData.setText(*text)
            return buttonData
        }

        fun buildIconButton(context: Context, iconResId: Int, iconPaddingDp: Float): ButtonData {
            val buttonData = ButtonData(true)
            buttonData.iconButton = true
            buttonData.iconPaddingDp = iconPaddingDp
            buttonData.setIconResId(context, iconResId)
            return buttonData
        }
    }
}
