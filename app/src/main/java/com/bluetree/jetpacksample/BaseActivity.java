package com.bluetree.jetpacksample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bluetree.jetpacksample.utils.LogUtils;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(this,"onCreate");
        progressDialog = new ProgressDialog(this);
    }

    public void showLoadingBar() {
        showLoadingBar("");
    }

    public void showLoadingBar(String title) {
        if (!progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.show();
        }
    }

    public void hideLoadingBar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(this,"onDestroy");
        super.onDestroy();
    }
}
