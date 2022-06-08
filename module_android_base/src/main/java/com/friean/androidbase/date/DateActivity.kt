package com.friean.androidbase.date

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import com.friean.androidbase.R
import java.text.SimpleDateFormat
import java.util.*

class DateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
    }

    fun viewClick(view: View) {
        when(view.id){
            R.id.btnGetSystemPatern->{
                val dateFormat = DateFormat.getDateFormat(this) as java.text.SimpleDateFormat
                val pattern = dateFormat.toLocalizedPattern()
                Log.i(TAG, "viewClick:pattern = $pattern")
                Log.i(TAG, "viewClick: ${pattern.split("/").map { 
                    when(it.lowercase().first().toString()){
                        "y"->{
                            "年"
                        }
                        "m"->{
                            "月"
                        }else -> {
                            "日"
                        }
                    }}}")

//                val flags = DateUtils.FORMAT_NO_MONTH_DAY or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_NOON
                val flags = DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_SHOW_YEAR
                val locale = resources.configuration.locale
                Log.i(TAG, "viewClick: locale = $locale")

                val f = Formatter(StringBuilder(50), locale)
                val time = Calendar.getInstance().timeInMillis
                val timeZone = Calendar.getInstance().timeZone
                val date = DateUtils.formatDateRange(this,f,time,time,flags,timeZone.id)
                Log.i(TAG, "viewClick: format date = $date")

            }
        }
    }

    companion object{
        private const val TAG = "DateActivityTAG"
    }
}