package com.example.niu.qqapp.adapter


import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.niu.qqapp.R
import com.niuedu.ListTree
import com.niuedu.ListTreeAdapter

class ContactsPageListAdapter(tree: ListTree?) :
    ListTreeAdapter<ContactsPageListAdapter.BaseViewHolder>(tree) {

    //存放组数据
    class GroupInfo(
        val title: String , //组标题
        val onlineCount: Int//此组内在线的人数
    )

    //存放联系人数据
    data class ContactInfo(
        val avatarURL: Bitmap,//头像
        val name: String,  //名字
        val status: String //状态
    )

    override fun onCreateNodeView(parent: ViewGroup?, viewType: Int): BaseViewHolder? {

        //获取从layout创建View的对象
        val inflater=LayoutInflater.from(parent!!.context)
        //创建不同的行View
        when(viewType){
            R.layout.contacts_group_item -> {
                //最后一个参数必须是true
                val view:View=inflater.inflate(viewType,parent,true)
                return GroupViewHolder(view)
            }
            R.layout.contacts_contact_item -> {
                //最后一个参数必须是true
                val view:View=inflater.inflate(viewType,parent,true)
                return ContactViewHolder(view)
            }
            else -> return null
        }


    }

    override fun onBindNodeViewHolder(viewHoler: BaseViewHolder?, position: Int) {
        //获取行控件
        val view=viewHoler!!.itemView
        //获取这一行在树对象中对应的节点
        val node=tree.getNodeByPlaneIndex(position)



    }

    open inner class BaseViewHolder(itemView: View) : ListTreeViewHolder(itemView)

    //组ViewHolder
    internal inner class GroupViewHolder(itemView: View) : BaseViewHolder(itemView) {
        //显示标题的控件
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        //显示好友数/在线数的控件
        var textViewCount: TextView = itemView.findViewById(R.id.textViewCount)
    }

    //好友ViewHolder
    internal inner class ContactViewHolder(itemView: View) : BaseViewHolder(itemView) {
        //显示好友头像的控件
        var imageViewHead: ImageView = itemView.findViewById(R.id.imageViewHead)
        //显示好友名字的控件
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        //显示好友状态的控件
        var textViewDetail: TextView = itemView.findViewById(R.id.textViewDetail)

    }
}