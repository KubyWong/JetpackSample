package com.bluetree.jetpacksample.activity

import android.view.View
import com.bluetree.jetpacksample.BaseRecycleActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import android.widget.Toast
import android.databinding.adapters.CompoundButtonBindingAdapter.setChecked
import android.support.design.widget.NavigationView
import android.view.Window.FEATURE_NO_TITLE
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import android.view.Window
import com.bluetree.jetpacksample.BaseActivity
import com.bluetree.jetpacksample.R
import com.bluetree.jetpacksample.fragment.BlankFragment1
import com.bluetree.jetpacksample.fragment.CustomViewFragment

/**
 * Created by Gavin
 * Date: 2019/8/29
 * Time: 13:49
 */
class SampleCustomViewActivity : BaseActivity() {

    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_sample_customview)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)

        mNavigationView!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                item.setChecked(true)
                val title = item.getTitle().toString()
                mDrawerLayout!!.closeDrawers()

                supportFragmentManager.beginTransaction()
                        .replace(R.id.container,CustomViewFragment.newInstance(title,item.itemId))
                        .commit()
                return true
            }
        })


        supportFragmentManager.beginTransaction()
                .replace(R.id.container,CustomViewFragment.newInstance("",R.id.nav_draw_point))
                .commit()

    }

    /*override fun initList(): MutableList<MyDataModel>? {
        val listData = ArrayList<MyDataModel>()
        listData.add(MyDataModel("0", "动画绘制一条线"))

        return listData
    }


    override fun getItemClickListener(): BaseQuickAdapter.OnItemClickListener {
        return object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (position) {
                    0 ->{
                        //动画绘制一条线
                        jumpActivity(TreeViewActivity::class.java)

                    }
                }
            }

        }
    }*/
}