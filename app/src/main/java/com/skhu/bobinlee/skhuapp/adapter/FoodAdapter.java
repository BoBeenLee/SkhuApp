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
import com.skhu.bobinlee.skhuapp.model.Food;
import com.skhu.bobinlee.skhuapp.model.code.PS0005;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by BoBinLee on 2014-09-10.
 */
public class FoodAdapter extends BaseAdapter {
    private Context mContext;
    private List<Food> mFoods;
    private LayoutInflater mInflater;

    private int refreshCnt;

    public FoodAdapter(Context context, List<Food> foods) {
        super();
        mContext = context;
        mFoods = foods;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshCnt = 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.position != position || holder.refresh != refreshCnt){
            convertView = mInflater.inflate(R.layout.list_food_item, null);
            holder = new Holder();

            holder.refresh = refreshCnt;
            holder.position = position;
            holder.lunch = (TextView) convertView.findViewById(R.id.menu_lunch);
            holder.specialLunch = (TextView) convertView.findViewById(R.id.menu_special_lunch);
            holder.dinner = (TextView) convertView.findViewById(R.id.menu_dinner);
            holder.date = (TextView) convertView.findViewById(R.id.menu_date);
//
            holder.date.setText(mFoods.get(position).date);
            holder.lunch.setText("중식 : " + mFoods.get(position).lunch);
            holder.specialLunch.setText("특선 : " + mFoods.get(position).specialLunch);
            holder.dinner.setText("석식 : " + mFoods.get(position).dinner);
            // event
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class Holder {
        public int refresh;
        public int position;
        public TextView lunch, specialLunch, dinner, date;
    }

    @Override
    public void notifyDataSetChanged() {
        refreshCnt += 1;
        super.notifyDataSetChanged();
    }

    public void add(Food alarm){
        mFoods.add(alarm);
    }

    public void clear(){
        mFoods.clear();
    }

    @Override
    public int getCount() {
        return mFoods.size();
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
