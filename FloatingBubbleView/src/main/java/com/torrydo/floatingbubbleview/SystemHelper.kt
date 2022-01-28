package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View


private var exclusionRects: MutableList<Rect> = ArrayList()

fun View.updateGestureExclusion(context: Context) {
    if (Build.VERSION.SDK_INT < 29) return


    val screenSize = ScreenInfo.getScreenSize(context.applicationContext)

    exclusionRects.clear()

    val rect = Rect(0, 0, this.width, screenSize.height)
    exclusionRects.add(rect)


    this.systemGestureExclusionRects = exclusionRects
}