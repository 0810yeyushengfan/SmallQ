package com.example.niu.qqapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import com.smallq.android.R

class MessagePageListAdapter() :
    RecyclerView.Adapter<MessagePageListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val activity = parent.context as Activity
        val inflater = activity.layoutInflater
        var view: View?
        if (viewType == R.layout.message_list_item_search) {
            view = inflater.inflate(R.layout.message_list_item_search, parent, false)
        } else {
            view = inflater.inflate(R.layout.message_list_item, parent, false)
        }

        return MyViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    }

    override fun getItemViewType(position: Int): Int {
        if(0==position){
            //只有最顶端这行是搜索
            return R.layout.message_list_item_search;
        }
        //其余各合都一样的控件
        return R.layout.message_list_item;
    }

    //将ViewHolder声明为Adapter的内部类，反正外面也用不到
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}