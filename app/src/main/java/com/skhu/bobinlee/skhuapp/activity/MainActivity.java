package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gcm.GCMRegistrar;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.core.SessionManager;
import com.skhu.bobinlee.skhuapp.util.TypefaceUtil;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btnHome, btnQna, btnEtc;
    private ImageView btnAlarm;
    private Button btnMenu, btnCal;

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
        btnAlarm = (ImageView) findViewById(R.id.btn_alarm);
        btnHome = (Button) findViewById(R.id.btn_home);
        btnQna = (Button) findViewById(R.id.btn_qna);
        btnEtc = (Button) findViewById(R.id.btn_facebook);
        btnMenu = (Button) findViewById(R.id.btn_menu);
        btnCal = (Button) findViewById(R.id.btn_cal);
        setAlarm();
    }

    public void initEvent(){
        btnHome.setOnClickListener(this);
        btnQna.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
        btnCal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home :
                show(HomeActivity.class);
                break;
            case R.id.btn_qna :
                show(QnaActivity.class);
                break;
            case R.id.btn_facebook :
                show(FacebookActivity.class);
                break;
            case R.id.btn_menu :
                show(MenuActivity.class);
                break;
            case R.id.btn_alarm :
                show(AlarmActivity.class);
                break;
            case R.id.btn_cal :
                show(CalendarActivity.class);
                break;
        }
    }

    public void show(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void setAlarm(){
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);
        String productId = getString(R.string.product_id);
//        if (regId.equals("")) {
            GCMRegistrar.register(this, productId);
//        } else {
//            Log.e("GCMRegistrar id", regId);
//        }
    }
}
