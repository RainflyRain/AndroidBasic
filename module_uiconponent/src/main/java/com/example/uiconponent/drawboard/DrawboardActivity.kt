package com.example.uiconponent.drawboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.uiconponent.R

/**
 * 画板
 */
class DrawboardActivity : AppCompatActivity() {

    private var paletteView:PaletteView? = null
    private val colorArray = arrayOfNulls<Int>(4)
    private var count = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawboard)
        paletteView = findViewById(R.id.whiteBoard)
        paletteView?.mode = PaletteView.Mode.DRAW
        initView()
    }

    private fun initView() {
        colorArray[0] = resources.getColor(R.color.blue_paint)
        colorArray[1] = resources.getColor(R.color.green_paint)
        colorArray[2] = resources.getColor(R.color.red_paint)
        colorArray[3] = resources.getColor(R.color.yellow_paint)
    }

    fun onViewClick(view: View) {
        when(view.id){
            R.id.btnNextOption->{
                paletteView?.redo()
            }
            R.id.btnPreOption->{
                paletteView?.undo()
            }
            R.id.btnDelOption->{
                paletteView?.clear()
            }
            R.id.btnColorPaint->{
                count++
                paletteView?.penColor = colorArray[count%4]!!
            }
        }
    }
}