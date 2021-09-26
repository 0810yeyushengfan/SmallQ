package com.smallq.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smallq.android.adapter.ContactsPageListAdapter
import com.smallq.android.databinding.ActivitySearchBinding
import com.smallq.android.databinding.ContactsPageLayoutBinding
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    //视图绑定
    private lateinit var binding: ActivitySearchBinding
    //用来保存搜索结果
    private val searchResultList=ArrayList<MyContactInfo>()
    //为了能保存所在组的组名，创建此类，增加一个信息:所在组的组名
    class MyContactInfo(val info:ContactsPageListAdapter.ContactInfo,val groupName:String)
    //建立显示搜索结果的适配器
    inner class ResultListAdapter():RecyclerView.Adapter<ResultListAdapter.MyViewHolder>(){
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ResultListAdapter.MyViewHolder {
            val view=this@SearchActivity.layoutInflater.inflate(R.layout.search_result_item,parent,false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResultListAdapter.MyViewHolder, position: Int) {
            //获取联系人信息，设置到相应的控件中
            val info=searchResultList.get(position) as MyContactInfo
//            holder.imageViewHead.setImageBitmap(info.info.avatarURL)
            holder.textViewName.setText(info.info.name)
            holder.textViewDetail.setText("来自分组"+info.groupName)
        }


        override fun getItemCount(): Int {
            return searchResultList.size
        }
        inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val imageViewHead=itemView.findViewById(R.id.imageViewHead) as ImageView
            val textViewName=itemView.findViewById(R.id.textViewName) as TextView
            val textViewDetail=itemView.findViewById(R.id.textViewDetail) as TextView
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //视图绑定
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //设置搜索
        initSearching()
    }
    //设置搜索相关的东西
    private fun initSearching(){
        //设置搜素控件不以图标的形式显示
        binding.searchView.isIconifiedByDefault=false
        //显示非实时搜索的图标
        binding.searchView.isSubmitButtonEnabled=true
        //搜索结果列表
        val layoutManager=LinearLayoutManager(this)
        binding.resultListView.layoutManager=layoutManager
        binding.resultListView.adapter=ResultListAdapter()
        //响应SearchView的文本输入事件，以实现实时搜索
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //当单击了"搜索"键时回调的一个方法，因使用了实时搜索，此处没有实现的必要了，所以返回false，表示我们并没有处理，而是交由系统处理，但是其实系统也没做什么处理
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //根据newText中的字符串进行搜索，搜索其中包含关键字的节点
                val tree=MainFragment.getContacts()
                //必须每次清空保存结果的集合对象
                searchResultList.clear()
                if(!newText.equals("")){
                    //遍历整棵树
                    //开始遍历
                    var pos=tree.startEnumNode()
                    //当节点为null时停止遍历
                    while(pos!=null){
                        //获取当前遍历到的节点
                        val node=tree.getNodeByEnumPos(pos)
                        //如果这个节点中存的是联系人信息
                        if(node.data is ContactsPageListAdapter.ContactInfo){
                            //获取联系人信息对象
                            val contactInfo=node.data as ContactsPageListAdapter.ContactInfo
                            //获取此联系人的组名
                            val groupNode=node.parent
                            val groupInfo=groupNode.data as ContactsPageListAdapter.GroupInfo
                            val groupName=groupInfo.title
                            //查看联系人的名字或状态中是否包含了要搜索的字符串
                            if(contactInfo.name.contains(newText!!)||contactInfo.status.contains(newText!!)){
                                //如果搜到了，列出这个联系人的信息
                                searchResultList.add(MyContactInfo(contactInfo,groupName))
                            }
                        }
                        pos=tree.enumNext(pos)//继续遍历，获取下一个节点
                    }
                }
                //全部遍历并添加搜索结果完毕之后，通知RecyclerView，刷新数据
                binding.resultListView.adapter?.notifyDataSetChanged()
                return true
            }
        })
    }
}