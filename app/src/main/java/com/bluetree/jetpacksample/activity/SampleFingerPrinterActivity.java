package com.bluetree.jetpacksample.activity;

import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.utils.BiometricPromptManager;

public class SampleFingerPrinterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_finger_printer);

        final BiometricPromptManager manager = new BiometricPromptManager(this);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager.getmImpl().authenticate(new CancellationSignal(), new BiometricPromptManager.OnBiometricIdentifyCallBack() {
                    @Override
                    public void onDialogDismiss() {

                    }

                    @Override
                    public void onUsePassword() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });



            }
        });
    }
}
