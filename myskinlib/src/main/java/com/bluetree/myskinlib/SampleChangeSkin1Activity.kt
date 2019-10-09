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
                     SkinManager.getInstance().skinPath = Environment.getExternalStorageDirectory().absolutePath + "/Download/app-release.apk"
                     SkinManager.getInstance().changeSkin()

                     applySkin()
                 }else{
                     ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),2)
                 }
             }

         }
    }


}
