package com.smallq.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smallq.android.databinding.ActivityChatBinding
import java.util.*

class ChatActivity : AppCompatActivity() {
    //视图绑定
    private lateinit var binding:ActivityChatBinding
    //存放一条消息数据的类
    class ChatMessage(val contactName:String,//联系人的名字
                      val time:Date,//日期
                      val content:String,//消息的内容
                      val isMe:Boolean)//这个消息是不是我发出的?
    //存放所以的聊天信息
    private val chatMessages=ArrayList<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //视图绑定
        binding= ActivityChatBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        //获取启动此Activity时传过来的数据
        //在启动聊天界面时，通过此方法把对方的名字传过来
        val contactName=intent.getStringExtra("contact_name")
        //设置动作栏标题
        binding.toolbar.title=contactName
        setSupportActionBar(binding.toolbar)
        //设置显示动作栏上的返回图标
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //为RecyclerView设置适配器
        binding.chatMessageListView.layoutManager=LinearLayoutManager(this)
        binding.chatMessageListView.adapter=ChatMessagesAdapter()
        //响应”发送“按钮的单击，发出消息
        binding.buttonSend.setOnClickListener{
            //现在还不能真正发出消息，把消息放在chatMessages中，显示出来即可
            //从EditText控件取得消息
            val msg=binding.editMessage.text.toString()
            //添加到集合中，从而能在RecyclerView中显示
            var chatMessage=ChatMessage("我",Date(),msg,true)
            chatMessages.add(chatMessage)
            //同时把对方的话也加上，由于是模拟聊天，所以对方只有一句回答
            chatMessage= ChatMessage("对方",Date(),"你好啊\\(@^0^@)/",false)
            chatMessages.add(chatMessage)
            //通知RecuclerView，更新两行信息
            binding.chatMessageListView.adapter?.notifyItemRangeInserted(chatMessages.size-2,2)
            //让RecyclerView向下滚动，以显示最新的消息
            binding.chatMessageListView.scrollToPosition(chatMessages.size-1)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            //当单击动作栏上的返回图标时执行
            //关闭自己，显示来时的页面
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //为RecyclerView提供数据的适配器
    inner class ChatMessagesAdapter():RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder>(){
        inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val textView=itemView.findViewById(R.id.textView) as TextView
            val imageView=itemView.findViewById(R.id.imageView) as ImageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            //参数viewType即为行的Layout的资源id，由getItemViewType()的返回值决定
            val itemView=layoutInflater.inflate(viewType,parent,false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val message=chatMessages[position]
            holder.textView.setText(message.content)
        }

        override fun getItemCount(): Int {
            return chatMessages.size
        }

        //有两种行Layout，所以重写此方法
        override fun getItemViewType(position: Int): Int {
            val message=chatMessages[position]
            if(message.isMe){
                //如果是我的，靠右显示
                return R.layout.chat_message_right_item
            }else{
                //对方的，靠左显示
                return R.layout.chat_message_left_item
            }
        }
    }
}