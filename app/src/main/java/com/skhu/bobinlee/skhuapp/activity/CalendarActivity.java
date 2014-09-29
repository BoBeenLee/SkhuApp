package com.skhu.bobinlee.skhuapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.CalendarAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.code.SK0006;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.squareup.timessquare.CalendarPickerView;
//import com.squareup.timessquare.CalendarPickerView;


import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarActivity extends AbstractAsyncActivity implements View.OnClickListener {
    public GridView mCalendarView;
    public CalendarAdapter mCalendarAdapter;// adapter instance
    public Calendar mCurrentCalendar;
    public Button mBtnPrev, mBtnNext;
    public TextView mCalendarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initResource();
        initEvent();
    }

    public void initResource(){
        mCurrentCalendar = Calendar.getInstance();
        mCalendarView = (GridView) findViewById(R.id.calendar_view);
        mCalendarAdapter = new CalendarAdapter(this, mCurrentCalendar);
        mCalendarAdapter.setContents(new String[31]);

        mCalendarView.setAdapter(mCalendarAdapter);

        mBtnPrev = (Button) findViewById(R.id.btn_calendar_prev);
        mBtnNext = (Button) findViewById(R.id.btn_calendar_next);
        mCalendarTitle = (TextView) findViewById(R.id.calendar_text);

        mCalendarTitle.setText(mCurrentCalendar.get(Calendar.YEAR) + "년 " + (mCurrentCalendar.get(Calendar.MONTH) + 1) + "월");
        addCalendars(mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH));
    }

    public void initEvent(){
        mBtnPrev.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_calendar_prev :
                mCurrentCalendar.add(Calendar.MONTH, -1);
                mCalendarTitle.setText(mCurrentCalendar.get(Calendar.YEAR) + "년 " + (mCurrentCalendar.get(Calendar.MONTH) + 1) + "월");
                break;
            case R.id.btn_calendar_next :
                mCurrentCalendar.add(Calendar.MONTH, 1);
                mCalendarTitle.setText(mCurrentCalendar.get(Calendar.YEAR) + "년 " + (mCurrentCalendar.get(Calendar.MONTH) + 1) + "월");
                break;
        }
        addCalendars(mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH));
    }

    public void addCalendars(int year, int month){
        APICode reqCode = new APICode();
        SK0006 sk = new SK0006();
        reqCode.tranCd = "SK0006";

        sk.year = year;
        sk.month = month;
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0006> resCode = JacksonUtils.<APICode<SK0006>>jsonToObject(response.toString(), new TypeReference<APICode<SK0006>>() {
                });
                SK0006 sk = resCode.tranData;
                List<SK0006.SK0006Calendar> calendars = sk.res;
                String[] sDates, lDates;
                final int DATE_NUM = 31;
                sDates = new String[DATE_NUM];
                lDates = new String[DATE_NUM];
                
                for (int i = 0; calendars != null && i < calendars.size(); i++) {
                    int day = 0;
                    Calendar sDate = Calendar.getInstance();
                    Calendar lDate = Calendar.getInstance();

                    sDate.setTimeInMillis(calendars.get(i).sDate.getTime());
                    lDate.setTimeInMillis(calendars.get(i).lDate.getTime());

                    day = sDate.get(Calendar.DATE) - 1;
                    if(sDates[day] != null)
                        sDates[day] = sDates[day] + ", " + calendars.get(i).content;
                    else
                        sDates[day] = calendars.get(i).content;
                    day = lDate.get(Calendar.DATE) - 1;
                    if(lDates[day] != null)
                        lDates[day] = lDates[day] + ", " + calendars.get(i).content;
                    else
                        lDates[day] = calendars.get(i).content;
                }
                mCalendarAdapter.setContents(sDates);
                mCalendarAdapter.notifyDataSetChanged();
            }
        });
    }

}
