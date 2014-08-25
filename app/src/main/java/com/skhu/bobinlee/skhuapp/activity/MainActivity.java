package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.util.TypefaceUtil;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btnHome;
    private Button btnEtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "NotoSansKR-Bold.otf");
        setContentView(R.layout.activity_main);

        initResource();
        initEvent();
    }

    public void initResource(){
        btnHome = (Button) findViewById(R.id.btn_home);
        btnEtc = (Button) findViewById(R.id.btn_etc);
    }

    public void initEvent(){
        btnHome.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home :
                show(HomeActivity.class);
                break;
            case R.id.btn_department :
                show(DepartmentActivity.class);
                break;
            case R.id.btn_etc :
                show(EtcActivity.class);
                break;
        }
    }

    public void show(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
