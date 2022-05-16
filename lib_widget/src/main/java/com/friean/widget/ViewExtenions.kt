package com.friean.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import java.nio.charset.Charset


fun View.dp2px(dp:Int):Float{
    return (context.resources.displayMetrics.density * dp)
}

fun View.sp2px(sp:Int):Float{
    return (context.resources.displayMetrics.scaledDensity * sp)
}

fun Fragment.dp2px(dp:Int):Float{
    return (context!!.resources.displayMetrics.density * dp)
}

fun Fragment.sp2px(sp:Int):Float{
    return (context!!.resources.displayMetrics.density * sp)
}

fun Activity.dp2px(dp:Int):Float{
    return (resources.displayMetrics.density * dp)
}

fun Activity.sp2px(sp:Int):Float{
    return (resources.displayMetrics.scaledDensity * sp)
}

fun View.toVisibility(visibility:Int){
    if(this.visibility != visibility){
        this.visibility = visibility
    }
}

fun Context.dp2px(dp:Int):Float{
    return (resources.displayMetrics.density * dp)
}

fun Context.sp2px(sp:Int):Float{
    return (resources.displayMetrics.scaledDensity * sp)
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.showCursorWithoutKeyboard(){
    setOnTouchListener{ v, event ->
        v.onTouchEvent(event)
        v.context.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
            (it as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
        }
        v.setOnTouchListener(null)
        return@setOnTouchListener true
    }
    requestFocus()
}

fun View.increasePadding(padding:Int){
    setPadding(paddingLeft+padding, paddingTop+padding,
    paddingRight+padding, paddingBottom+padding)
}

fun View.setListItemCorner(bgColorRes:Int, radii:Int=4, fill:Boolean=true, lineColorRes: Int?=null, dashWidth:Float=0f, dashGap:Float=0f){
    setBgRect(bgColorRes, radii, fill, lineColorRes, dashWidth, dashGap)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        clipToOutline = true
    }
}

fun TextView.setTextColorRes(colorRes:Int){
    setTextColor(context.resources.getColor(colorRes))
}