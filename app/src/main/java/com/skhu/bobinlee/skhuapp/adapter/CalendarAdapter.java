package com.skhu.bobinlee.skhuapp.adapter;

/**
 * Created by BoBinLee on 2014-09-28.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private final static String[] weekText = {"일", "월", "화", "수", "목", "금", "토"};
    private final static int TOTAL_COUNT = 7 * 6;
    private final static int WEEK_NUM = 7;
    private Context mContext;
    private LayoutInflater mInflater;
    private Calendar mCurrentCalendar;
    private String[] mDates;
    private int refreshCnt;
    private boolean startCalendar, endCalendar;
    private int mCount;

    public CalendarAdapter(Context context, Calendar currentCalendar){
        mContext = context;
        mCurrentCalendar = currentCalendar;
        refreshCnt = 0;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    public void setContents(String[] dates){
        mDates = dates;
    }

    public void init(){
        mCurrentCalendar.set(Calendar.DATE, 1);
        endCalendar = startCalendar = false;
        mCount = 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.position != position || holder.refresh != refreshCnt) {
            convertView = mInflater.inflate(R.layout.list_calendar_item, null);
            holder = new Holder();
            holder.position = position;
            holder.refresh = refreshCnt;
            holder.num = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.date_text);

            if(position < WEEK_NUM){
//                Log.d("weekTextNum ", "weekTextNum : " + weekTextNum + " - " + weekText[weekTextNum] + " position : " + position + " refresh : " + refreshCnt);
                holder.num.setText("" + weekText[position]);
            } else if(WEEK_NUM <= position){
                int weekNum = (position % 7) + 1;
                if(weekNum == mCurrentCalendar.get(Calendar.DAY_OF_WEEK) && !startCalendar)
                    startCalendar = true;
                if(mCurrentCalendar.getActualMaximum(Calendar.DATE) == mCount - 1)
                    endCalendar = true;
                if(startCalendar && !endCalendar) {
                    holder.content.setVisibility(View.VISIBLE);
                    if(mDates[mCount - 1] != null) {
                        holder.content.setText("" + mDates[mCount - 1]);
                    }
                    holder.num.setText("" + mCount++);
                }
            }
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        refreshCnt += 1;
        init();
        super.notifyDataSetChanged();
    }

    private class Holder {
        private int refresh;
        private int position;
        private TextView num;
        private TextView content;
    }

    @Override
    public int getCount() {
        return TOTAL_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
