package com.nanit.websocket.app.ui

import com.nanit.websocket.app.R
import com.nanit.websocket.app.data.ThemeType

object ThemeManager {
    fun getScreenTheme(type: ThemeType) : ThemedResources {

      val numbersMap = hashMapOf<Int,Int>()
        numbersMap[0] = R.drawable.num0_icon
        numbersMap[1] = R.drawable.num1_icon
        numbersMap[2] = R.drawable.num2_icon
        numbersMap[3] = R.drawable.num3_icon
        numbersMap[4] = R.drawable.num4_icon
        numbersMap[5] = R.drawable.num5_icon
        numbersMap[6] = R.drawable.num6_icon
        numbersMap[7] = R.drawable.num7_icon
        numbersMap[8] = R.drawable.num8_icon
        numbersMap[9] = R.drawable.num9_icon
        numbersMap[10] = R.drawable.num10_icon
        numbersMap[11] = R.drawable.num11_icon
        numbersMap[12] = R.drawable.num12_icon

      return  when(type) {
            ThemeType.FOX -> ThemedResources(
                R.drawable.baby_placeholder_green,
                R.drawable.camera_icon_green,
                R.drawable.fox_bg,
                R.color.fox_bg_color,
                numbersMap)
            ThemeType.PELICAN -> ThemedResources(
                R.drawable.baby_placeholder_blue,
                R.drawable.camera_icon_blue,
                R.drawable.pelican_bg,
                R.color.pelican_bg_color,
                numbersMap)
            ThemeType.ELEPHANT -> ThemedResources(
                R.drawable.baby_placeholder_yellow,
                R.drawable.camera_icon_yellow,
                R.drawable.elephant_bg,
                R.color.elephant_bg_color,numbersMap)
        }
    }
}

data class ThemedResources(val placeHolderIcon: Int, val cameraIcon: Int, val bgLayer: Int, val bgColorRes : Int, private val numMap: HashMap<Int,Int>) {
    fun getDrawableResourceForNum(num : Int): Int {
        return numMap[num] ?: numMap[0]!!
    }
}