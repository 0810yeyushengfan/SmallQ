package com.example.niu.qqapp

import android.content.Context

fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}