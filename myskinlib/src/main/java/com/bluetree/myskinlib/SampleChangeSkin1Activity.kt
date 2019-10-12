package com.bluetree.myskinlib

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_change_skin1_sample.*

class SampleChangeSkin1Activity : SkinBaseActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_skin1_sample)
        btn_load_skin.setOnClickListener(this)

    }

     override fun onClick(v: View?) {
         when (v?.id) {
             R.id.btn_load_skin -> {
                 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                     SkinManager.getInstance().context = this
                     //这个apk可以在raw目录下找到，拷贝到手机目录下对应的目录即可。主要是模拟下载到手机本地的操作
                     SkinManager.getInstance().skinPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/app_release.apk"

                     applySkin()
                 }else{
                     ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),2)
                 }
             }

         }
    }


}
