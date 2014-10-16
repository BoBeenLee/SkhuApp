package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skhu.bobinlee.skhuapp.R;

/**
 * Created by bobinlee on 14. 10. 2.
 */
// implement parent child class ....
public class CommonActivity extends AbstractAsyncActivity {
    protected LinearLayout mBtnAlarm, mBtnQna, mBtnHome, mBtnMenu, mBtnCalendar;
    protected ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initResource();
        initEvent();
    }

    private void initResource() {
        mBtnAlarm = (LinearLayout) findViewById(R.id.btn_alarm);
        mBtnQna = (LinearLayout) findViewById(R.id.btn_qna);
        mBtnHome = (LinearLayout) findViewById(R.id.btn_home);
        mBtnMenu = (LinearLayout) findViewById(R.id.btn_menu);
        mBtnCalendar = (LinearLayout) findViewById(R.id.btn_cal);
        mImageBack = (ImageView) findViewById(R.id.btn_back);
    }

    private void initEvent() {
//        Log.d("mBtnAlarm", "mBtnAlarm : " + mBtnAlarm);

        if (mBtnAlarm != null)
            mBtnAlarm.setOnClickListener(new CommonEvent(this));
        if (mBtnQna != null)
            mBtnQna.setOnClickListener(new CommonEvent(this));
        if (mBtnHome != null)
            mBtnHome.setOnClickListener(new CommonEvent(this));
        if (mBtnMenu != null)
            mBtnMenu.setOnClickListener(new CommonEvent(this));
        if (mBtnCalendar != null)
            mBtnCalendar.setOnClickListener(new CommonEvent(this));
        if (mImageBack != null)
            mImageBack.setOnClickListener(new CommonEvent(this));
    }

    public static class CommonEvent implements View.OnClickListener {
        private Activity activity;

        public CommonEvent(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("class", view.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all
            // activities

//            Log.d("start", "start");
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
