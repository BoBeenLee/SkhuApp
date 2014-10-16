package com.skhu.bobinlee.skhuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Alarm;
import com.skhu.bobinlee.skhuapp.model.Facebook;
import com.skhu.bobinlee.skhuapp.model.code.PS0004;
import com.skhu.bobinlee.skhuapp.model.code.PS0005;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-09-10.
 */
public class AlarmAdapter extends BaseAdapter {
    private Context mContext;
    private List<Alarm> mAlarms;
    private LayoutInflater mInflater;

    private int refreshCnt;

    public AlarmAdapter(Context context, List<Alarm> alarms) {
        super();
        mContext = context;
        mAlarms = alarms;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshCnt = 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.position != position || holder.refresh != refreshCnt){
            convertView = mInflater.inflate(R.layout.list_alarm_item, null);
            holder = new Holder();

            holder.refresh = refreshCnt;
            holder.position = position;
            holder.no = (TextView) convertView.findViewById(R.id.alarm_no);
            holder.category = (TextView) convertView.findViewById(R.id.alarm_category);
            holder.filter = (TextView) convertView.findViewById(R.id.alarm_filter);
            holder.remove = (Button) convertView.findViewById(R.id.alarm_remove);


            holder.no.setText("" + (position + 1));
            holder.category.setText(mAlarms.get(position).cateNm);
            holder.filter.setText(mAlarms.get(position).filter);

            // event
            holder.remove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    removeAlarm(mAlarms.get(position).no, position);
                }
            });
            convertView.setTag(holder);
        }
        return convertView;
    }

    public void removeAlarm(long alarmNo, final int position){
        PS0005 ps = new PS0005();
        ps.mac = CommonUtils.getMACAddress(mContext);
        ps.alarmNo = alarmNo;

        APICode<PS0005> reqCode = new APICode<PS0005>();
        reqCode.tranCd = "PS0005";
        reqCode.tranData = ps;

        PostMessageTask.postJson(mContext, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0005> resCode = JacksonUtils.<APICode<PS0005>>jsonToObject(response.toString(), new TypeReference<APICode<PS0005>>() {
                });
                PS0005 ps = resCode.tranData;
                if(ps.resultYn.equals("Y")){
                    mAlarms.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "삭제 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        refreshCnt += 1;
        super.notifyDataSetChanged();
    }

    public void add(Alarm alarm){
        mAlarms.add(alarm);
    }

    public void clear(){
        mAlarms.clear();
    }

    private class Holder {
        public int refresh;
        public int position;
        public TextView no, category, filter;
        public Button remove;
    }

    @Override
    public int getCount() {
        return mAlarms.size();
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
