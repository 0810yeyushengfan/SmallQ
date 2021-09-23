package com.smallq.android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import com.smallq.android.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //创建三条历史记录菜单项，添加到layoutHistory中
        for(i in 0..2) {
            var layoutItem = activity!!.layoutInflater.inflate(R.layout.login_history_item, null)
            //响应菜单项的单击，把他里面的信息填到输入框中
            layoutItem.setOnClickListener{
                binding.editTextQQNum.setText("2160949409")
                binding.layoutHistory.visibility=View.INVISIBLE
                binding.layoutContext.visibility=View.VISIBLE
            }
            binding.layoutHistory.addView(layoutItem)
        }
        binding.textViewHistory.setOnClickListener {
            if(binding.layoutHistory.visibility==View.INVISIBLE) {
                binding.layoutContext.visibility = View.INVISIBLE
                binding.layoutHistory.visibility = View.VISIBLE
                //使用动画显示历史记录
                val set=AnimationUtils.loadAnimation(context,R.anim.login_history_anim) as AnimationSet
                binding.layoutHistory.startAnimation(set)
            } else{
                binding.layoutHistory.visibility=View.INVISIBLE
                binding.layoutContext.visibility=View.VISIBLE
            }

        }

        view.setOnClickListener{
            if(binding.layoutHistory.visibility==View.VISIBLE){
                binding.layoutHistory.visibility=View.INVISIBLE
                binding.layoutContext.visibility=View.VISIBLE
            }
        }
        //登录成功
        binding.buttonLogin.setOnClickListener{
            val fragmentManager=activity!!.supportFragmentManager
            val fragment=MainFragment()
            //替换掉FrameLayout中现有的Fragment
            val fragmentTransaction=fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,fragment)
            //将这次切换放入后退栈中，这样可以在单击后退键时自动返回上一个页面
            fragmentTransaction.addToBackStack("login")
            fragmentTransaction.commit()
        }
        //进入注册页面
        binding.textViewRegister.setOnClickListener{
            //启动注册Activity
            val intent= Intent(context,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}