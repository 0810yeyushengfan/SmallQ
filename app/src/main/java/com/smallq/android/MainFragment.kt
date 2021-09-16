package com.smallq.android

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.niu.qqapp.adapter.MessagePageListAdapter
import com.google.android.material.tabs.TabLayout
import com.smallq.android.databinding.FragmentLoginBinding
import com.smallq.android.databinding.FragmentMainBinding

class MainFragment :Fragment(){
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    //创建一个数组，有三个元素，三个元素都初始化为空
    private val listViews= arrayOfNulls<RecyclerView>(3)

    //为ViewPager派生一个适配器类
    internal inner class ViewPageAdapter:PagerAdapter(){
        override fun getCount(): Int {
            return listViews.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view==`object`
        }

        //能够正常显示TabItem的文本内容
        override fun getPageTitle(position: Int): CharSequence? {
            when{
                position==0->return "消息"
                position==1->return "联系人"
                position==2->return "动态"
                else->return null
            }
        }

        //实例化一个子View，container是子View容器，就是ViewPager
        //position是当前的页数，从0开始计数
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val v=listViews[position]
            container.addView(v)
            return v!!
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //创建三个RecyclerView,分别对应QQ消息页，QQ联系人页，QQ空间页
        for(i in 0..2){
            listViews[i]= RecyclerView(context!!)
        }
        //仅供测试使用
//        listViews[0]!!.setBackgroundColor(Color.RED)
//        listViews[1]!!.setBackgroundColor(Color.GREEN)
//        listViews[2]!!.setBackgroundColor(Color.BLUE)
        //为第一个RecyclerView设置
        listViews[0]!!.layoutManager=LinearLayoutManager(context)
        listViews[0]!!.adapter=MessagePageListAdapter()
        //将Adapter设置给ViewPager示例，注意这两句的先后顺序
        binding.viewPager.adapter=ViewPageAdapter()
        //建立ViewPager与TabLayout的联动
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        //能够正常显示TabItem的图标，并将消息TabItem置为选中状态
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.message_focus)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.contacts_normal)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.space_normal)
        //监听Tab Item的改变
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when{
                    tab?.position===0 -> tab.setIcon(R.drawable.message_focus)
                    tab?.position===1 -> tab.setIcon(R.drawable.contacts_focus)
                    else ->tab?.setIcon(R.drawable.space_focus)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when{
                    tab?.position===0 -> tab.setIcon(R.drawable.message_normal)
                    tab?.position===1 -> tab.setIcon(R.drawable.contacts_normal)
                    else ->tab?.setIcon(R.drawable.space_normal)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when{
                    tab?.position===0 -> tab.setIcon(R.drawable.message_focus)
                    tab?.position===1 -> tab.setIcon(R.drawable.contacts_focus)
                    else ->tab?.setIcon(R.drawable.space_focus)
                }
            }
        })
        binding.textViewPopMenu.setOnClickListener{
            //创建PopupWindow，用于承载气泡菜单
            val pop=PopupWindow(context)
            pop.animationStyle=R.style.popupMenuAnim
            //设置窗口出现时获取焦点，这样在按下返回键时窗口才会消失
            pop.isFocusable=true
            //向Fragment容器(FrameLayout)中加入一个View作为上层容器和蒙板
            val maskView=View(context)
            maskView.setBackgroundColor(Color.DKGRAY)
            maskView.alpha=0.5f
            val rootView=view as FrameLayout//这里的view使用的是上面onViewCreated()方法的参数中的view，即父布局
            rootView.addView(maskView,FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
//            //设置再次点击屏幕时蒙板消失的效果
//            maskView.setOnClickListener{
//                //去掉蒙板
//                rootView.removeView(maskView)
//                //隐藏弹出窗口
//                pop.dismiss()
//            }
            //响应弹出窗口的消失事件
            pop.setOnDismissListener {
                //去掉蒙板
                rootView.removeView(maskView)
            }
            //加载气泡图像，以作为window的背景
            val drawable=resources.getDrawable(R.drawable.pop_bk,null)
            //设置气泡图像为window的背景
            pop.setBackgroundDrawable(drawable)
            //加载菜单项资源，是用LinearLaylout模拟的菜单
            val menuView=LayoutInflater.from(context).inflate(R.layout.pop_menu_layout,null)
            //为窗口添加一个控件,设置window中要显示的View
            pop.contentView= menuView
            //计算一下菜单layout的实际大小，然后获取之
            menuView.measure(0,0)
            //显示窗口,将“+”控件设置为窗口的锚点
            pop.showAsDropDown(it,-pop.contentView.measuredWidth-15,-10)
        }
        //响应左上角的头像单击事件，显示抽屉页面
        binding.headImage.setOnClickListener{
            //创建抽屉页面
            val drawerLayout=activity!!.layoutInflater.inflate(R.layout.drawer_layout,view as ViewGroup,false)
            //先计算一下消息页面中左边一排图像的大小，在页面构建器中设置的是dp，
            //在代码中只能用像素px，所以这里要换算一下，因为不同的屏幕分辨率，dp对应的px是不同的
            val messageImageWidth= dipToPx(context!!,60.0f)
            //计算抽屉页面的宽度，根视图是FrameLayout，利用getWidth()获取它当前的宽度
            val drawerWidth=view.width-messageImageWidth
            //设置抽屉页面的宽度
            drawerLayout.layoutParams.width=drawerWidth
            //将抽屉页面加入FrameLayout中
            view.addView(drawerLayout)
            //设置抽屉页面出现时的动画
            //创建一个动画，让抽屉页面向右移，注意它是从左边移出来的，所以其初始位置是-drawerWidth/2，即有一半位于屏幕外
            val animatorDrawer=ObjectAnimator.ofFloat(drawerLayout,"translationX",-drawerWidth/2f,0f)
            //设置动画的持续时间
            val duration=500L
            //把原内容的根控件放到最上层，这样在移动时一直能看到它(QQ就是这个效果)
            binding.contentLayout.bringToFront()
            //创建动画，移动原内容，从0位置移动抽屉页面宽度的距离
            val animatorContent=ObjectAnimator.ofFloat(binding.contentLayout,"translationX",0f,drawerWidth.toFloat())
            //创建蒙板View
            val maskViewdrawer=View(context)
            maskViewdrawer.setBackgroundColor(Color.GRAY)
            //必须将其初始透明度设为无安全透明
            maskViewdrawer.alpha=0f;
            //添加到FrameLayout中
            view.addView(maskViewdrawer)
            //将蒙板View放到最上层
            maskViewdrawer.bringToFront()
            //创建移动蒙板的动画
            val animatorMask=ObjectAnimator.ofFloat(maskViewdrawer,"translationX",0f,drawerWidth.toFloat())
            //创建蒙板逐渐变暗的动画
            val animatorMaskAlpha=ObjectAnimator.ofFloat(maskViewdrawer,"alpha",0f,0.6f)
            //创建动画集合，同时播放四个动画
            val animatorSet=AnimatorSet()
            animatorSet.playTogether(animatorContent,animatorDrawer,animatorMask,animatorMaskAlpha)
            animatorSet.duration=duration
            animatorSet.start()
            //当单击蒙板View时，隐藏抽屉页面
            maskViewdrawer.setOnClickListener{
                //动画反着来，让抽屉消失
                //创建动画，移动原内容，从抽屉页面宽度的距离移动到0位置
                val animatorContent=ObjectAnimator.ofFloat(binding.contentLayout,"translationX",drawerWidth.toFloat(),0f)
                //创建移动蒙板的动画
                val animatorMask=ObjectAnimator.ofFloat(maskViewdrawer,"translationX",drawerWidth.toFloat(),0f)
                //创建蒙板逐渐变亮的动画
                val animatorMaskAlpha=ObjectAnimator.ofFloat(maskViewdrawer,"alpha",0.6f,0f)
                //创建抽屉向左移动的动画，注意最终位置是-drawerWidth/2，即有一半位于屏幕之外
                val animatorDrawer=ObjectAnimator.ofFloat(drawerLayout,"translationX",0f,-drawerWidth/2f)
                //创建动画集合，同时播放四个动画
                val animatorSet=AnimatorSet()
                animatorSet.playTogether(animatorContent,animatorDrawer,animatorMask,animatorMaskAlpha)
                animatorSet.duration=duration
                //设置监听器，主要监听动画关闭事件
                animatorSet.addListener(object :Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        //动画结束，将蒙板和抽屉页面删除
                        view.removeView(maskViewdrawer)
                        view.removeView(drawerLayout)
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                })
                //执行动画
                animatorSet.start()
            }
        }

    }
}