package com.bluetree.myskinlib

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_change_skin1_sample.*

class SampleChangeSkin1Activity : SkinBaseActivity(),View.OnClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_skin1_sample)



    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_load_skin->{
                SkinManager.getInstance().skinPath = Environment.getExternalStorageDirectory().absolutePath+"/Download/app-debug.apk"

                btn_load_skin.setBackgroundColor(SkinManager.getInstance().skinRes!!.getColor(R.color.colorAccent))


            }
        }
    }
}
