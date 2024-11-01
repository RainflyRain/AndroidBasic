package com.friean.androidbase.actionmenu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.friean.androidbase.R
import com.friean.androidbase.databinding.ActivityMenuBinding
import com.friean.widget.actionmenu.ButtonData
import com.friean.widget.actionmenu.ButtonEventListener
import com.friean.widget.actionmenu.SectorMenuButton

class MenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomSectorMenuButton()
        initTopSectorMenuButton()
        initRightSectorMenuButton()
        initCenterSectorMenuButton()
    }

    private fun initTopSectorMenuButton() {
        val sectorMenuButton: SectorMenuButton =
            findViewById(R.id.top_sector_menu) as SectorMenuButton
        val buttonDatas: MutableList<ButtonData> = ArrayList<ButtonData>()
        val drawable = intArrayOf(
            R.drawable.like, R.drawable.mark,
            R.drawable.search, R.drawable.copy
        )
        for (i in 0..3) {
            val buttonData: ButtonData = ButtonData.buildIconButton(this, drawable[i], 0f)
            buttonData.setBackgroundColorId(this, R.color.primaryColor)
            buttonDatas.add(buttonData)
        }
        sectorMenuButton.setButtonDatas(buttonDatas)
        setListener(sectorMenuButton)
    }

    private fun initRightSectorMenuButton() {
        val sectorMenuButton: SectorMenuButton =
            findViewById(R.id.right_sector_menu) as SectorMenuButton
        val buttonDatas: MutableList<ButtonData> = ArrayList<ButtonData>()
        val drawable = intArrayOf(
            R.drawable.like, R.drawable.mark,
            R.drawable.search, R.drawable.copy
        )
        for (i in 0..3) {
            val buttonData: ButtonData = ButtonData.buildIconButton(this, drawable[i], 0f)
            buttonData.setBackgroundColorId(this, R.color.primaryColor)
            buttonDatas.add(buttonData)
        }
        sectorMenuButton.setButtonDatas(buttonDatas)
        setListener(sectorMenuButton)
    }

    private fun initCenterSectorMenuButton() {
        val sectorMenuButton: SectorMenuButton =
            findViewById(R.id.center_sector_menu) as SectorMenuButton
        val buttonDatas: MutableList<ButtonData> = ArrayList<ButtonData>()
        val drawable = intArrayOf(
            R.drawable.like, R.drawable.mark,
            R.drawable.search, R.drawable.copy, R.drawable.settings,
            R.drawable.heart, R.drawable.info, R.drawable.record,
            R.drawable.refresh
        )
        for (i in 0..8) {
            val buttonData: ButtonData = ButtonData.buildIconButton(this, drawable[i], 0f)
            buttonData.setBackgroundColorId(this, R.color.primaryColor)
            buttonDatas.add(buttonData)
        }
        sectorMenuButton.setButtonDatas(buttonDatas)
        setListener(sectorMenuButton)
    }

    private fun initBottomSectorMenuButton() {
        val sectorMenuButton: SectorMenuButton =
            findViewById(R.id.bottom_sector_menu) as SectorMenuButton
        val buttonDatas: MutableList<ButtonData> = ArrayList<ButtonData>()
        val drawable = intArrayOf(
            R.drawable.like, R.drawable.mark,
            R.drawable.search, R.drawable.copy
        )
        for (i in 0..3) {
            val buttonData: ButtonData = ButtonData.buildIconButton(this, drawable[i], 0f)
            buttonData.setBackgroundColorId(this, R.color.primaryColor)
            buttonDatas.add(buttonData)
        }
        sectorMenuButton.setButtonDatas(buttonDatas)
        setListener(sectorMenuButton)
    }

    private fun setListener(button: SectorMenuButton) {
        button.setButtonEventListener(object : ButtonEventListener {
            override fun onButtonClicked(index: Int) {
                showToast("button$index")
            }

            override fun onExpand() {
                showToast("onExpand")
            }

            override fun onCollapse() {
                showToast("onCollapse")
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}