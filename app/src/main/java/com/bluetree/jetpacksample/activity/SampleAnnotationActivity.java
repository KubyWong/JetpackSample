package com.bluetree.jetpacksample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bluetree.annotation_lib.BindView;
import com.bluetree.annotation_lib.MyButterKnife;
import com.bluetree.jetpacksample.R;

public class SampleAnnotationActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button2)
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_annotation);
        MyButterKnife.bind(SampleAnnotationActivity.this);

        button2.setText("333");

    }
}
