package com.example.niu.qqapp

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.niu.qqapp.adapter.ContactsPageListAdapter
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niu.qqapp.databinding.ActivityChatBinding
import com.example.niu.qqapp.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private val searchResultList = ArrayList<MyContactInfo>()

    //新式view binding，整天TM改来改去！
    private lateinit var binding: ActivitySearchBinding

    //为了能保存所在组的组名，创建此类,增加一个信息：所在组的组名
    class MyContactInfo(val info: ContactsPageListAdapter.ContactInfo ,val groupName:String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        //设置搜索
//        initSearching()
    }

//    //设置搜索相关的东西
//    private fun initSearching() {
//        //设置搜索控件不以图标的形式显示
//        binding.searchView.setIconifiedByDefault(false)
//        //searchView.setSubmitButtonEnabled(true)
//
//        //搜索结果列表
//        binding.resultListView.setLayoutManager(LinearLayoutManager(this))
//        binding.resultListView.setAdapter(ResultListAdapter());
//
//        //响应SearchView的文本输入事件，以实现实时搜索
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                //当点了“搜索”键时执行，因使用了实时搜索，此处
//                //没有实现的必要了，所以返回false，表示我们并没有处理，
//                //交由系统处理，但其实系统也没做什么处理。
//                return false;
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                //跟据newText中的字符串进行搜索，搜索其中包含关键字的节点
//                val tree = MainFragment.getContacts()
//                //必须每次都清空保存结果的集合对象
//                searchResultList.clear()
//
//                //只有当要搜索的字符串非空时，才遍历列表
//                if (!newText.equals("")) {
//                    //遍历整个树
//                    var pos = tree.startEnumNode()
//                    while (pos != null) {
//                        //如果这个节点中存的是联系人信息
//                        val node = tree.getNodeByEnumPos(pos);
//                        if (node.data is ContactsPageListAdapter.ContactInfo) {
//                            //获取联系人信息对象
//                            val contactInfo = node.getData() as ContactsPageListAdapter.ContactInfo
//                            //获取此联系人的组名
//                            val groupNode = node.parent
//                            val groupInfo = groupNode.getData() as ContactsPageListAdapter.GroupInfo
//                            val groupName = groupInfo.title
//                            //查看联系人的名字中或状态中是否包含了要搜索的字符串
//                            if (contactInfo.name.contains(newText) || contactInfo.status.contains(newText)) {
//                                //搜到了！列出这个联系人的信息
//                                searchResultList.add(MyContactInfo(contactInfo, groupName));
//                            }
//                        }
//                        //System.out.println(node.getData().toString());
//                        pos = tree.enumNext(pos);
//                    }
//                }
//
//                //通知RecyclerView，刷新数据
//                binding.resultListView.adapter?.notifyDataSetChanged();
//                return true;
//            }
//        })
//    }

    inner class ResultListAdapter(): RecyclerView.Adapter<ResultListAdapter.MyViewHolder>(){
        override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):MyViewHolder {
            val v= this@SearchActivity.layoutInflater.inflate(R.layout.search_result_item,parent,false);
            return MyViewHolder(v);
        }

        override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
            //获取联系人信息，设置到对应的控件中
            val info = searchResultList.get(position) as MyContactInfo
//            holder.imageViewHead.setImageBitmap(info.info.avatar)
            holder.textViewName.setText(info.info.name)
            holder.textViewDetail.setText("来自分组 "+info.groupName)
        }

        override fun getItemCount():Int {
            return searchResultList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageViewHead = itemView.findViewById(R.id.imageViewHead) as ImageView
            val textViewName = itemView.findViewById(R.id.textViewName) as TextView
            val textViewDetail = itemView.findViewById(R.id.textViewDetail) as TextView
        }
    }

}
