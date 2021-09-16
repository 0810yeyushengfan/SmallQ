package com.smallq.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import java.util.jar.Attributes

//禁止ViewPager滑动翻页
class QQViewPager:ViewPager {
    //必须实现带一个参数的构造方法
    constructor(context:Context):super(context){

    }

    //必须实现此构造方法，否则在界面设计器中不能正常显示
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){

    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}