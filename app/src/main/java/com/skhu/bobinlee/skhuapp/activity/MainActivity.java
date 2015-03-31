package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gcm.GCMRegistrar;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.core.SessionManager;
import com.skhu.bobinlee.skhuapp.util.TypefaceUtil;

public class MainActivity extends CommonActivity implements View.OnClickListener {
    private LinearLayout mBtnSettingAlarm, mBtnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "NotoSansKR-Bold.otf");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initResource();
        initEvent();
    }

    public void initResource() {
        mBtnSettingAlarm = (LinearLayout) findViewById(R.id.btn_setting_alarm);
        mBtnFacebook = (LinearLayout) findViewById(R.id.btn_facebook);
        setAlarm();
    }

    private void initEvent() {
        mBtnSettingAlarm.setOnClickListener(this);
//        Log.d("mBtnAlarm", "mBtnAlarm 1 : " + mBtnAlarm);
        mBtnAlarm.setOnClickListener(this);
        mBtnQna.setOnClickListener(this);
        mBtnHome.setOnClickListener(this);
        mBtnMenu.setOnClickListener(this);
        mBtnCalendar.setOnClickListener(this);
        mBtnFacebook.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("class", -1);
        if (id != -1)
            startSubActivity(id);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                show(HomeActivity.class);
                break;
            case R.id.btn_qna:
                show(QnaActivity.class);
                break;
            case R.id.btn_facebook:
                show(FacebookActivity.class);
                break;
            case R.id.btn_menu:
                show(MenuActivity.class);
                break;
            case R.id.btn_setting_alarm :
            case R.id.btn_alarm:
                show(AlarmActivity.class);
                break;
            case R.id.btn_cal:
                show(CalendarActivity.class);
                break;
        }
    }

    public void startSubActivity(int id) {
        Class clazz = null;

        switch (id) {
            case R.id.btn_alarm:
                clazz = AlarmActivity.class;
                break;
            case R.id.btn_qna:
                clazz = QnaActivity.class;
                break;
            case R.id.btn_home:
                clazz = HomeActivity.class;
                break;
            case R.id.btn_menu:
                clazz = MenuActivity.class;
                break;
            case R.id.btn_cal:
                clazz = CalendarActivity.class;
                break;
            default:
//                clazz = MainActivity.class;
        }
        getIntent().putExtra("class", -1);
        if(clazz != null)
            show(clazz);
    }

    public void show(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
//        overridePendingTransition(0, 0);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void setAlarm() {
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
