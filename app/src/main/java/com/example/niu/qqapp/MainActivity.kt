package com.example.niu.qqapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //将LoginFragment加入，作为首页
        val fragmentTransaction =supportFragmentManager.beginTransaction()
        val fragment=LoginFragment()
        fragmentTransaction.add(R.id.fragment_container,fragment)
        fragmentTransaction.commit()


    }
}