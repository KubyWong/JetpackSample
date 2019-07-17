package com.bluetree.jetpacksample.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.bluetree.jetpacksample.BaseActivity
import com.bluetree.jetpacksample.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * rxjava使用demo
 */
class SampleRxjavaActivity : BaseRecycleActivity() {
    override fun initList(): MutableList<Any?> {
        val listData = mutableListOf<SampleRecycleViewActivity.MyDataModel>()
        listData.add(SampleRecycleViewActivity.MyDataModel("", "rxjavaf1"))
        listData.add(SampleRecycleViewActivity.MyDataModel("", "rxjavaf2"))
        listData.add(SampleRecycleViewActivity.MyDataModel("", "rxjavaf3"))
        listData.add(SampleRecycleViewActivity.MyDataModel("", "rxjavaf4"))
        return listData.toMutableList()
    }

    override fun getItemClickListener(): BaseQuickAdapter.OnItemClickListener {
        return object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

            }

        }
    }

}
