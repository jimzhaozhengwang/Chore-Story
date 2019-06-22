package com.chorestory.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chorestory.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
    }
}
