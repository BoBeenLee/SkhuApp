package com.skhu.bobinlee.skhuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.data.Skhu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Skhu> skhuArticles;

    public HomeAdapter(Context context) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        skhuArticles = new ArrayList<Skhu>();
    }

    public void add(Skhu skhu){
        skhuArticles.add(skhu);
    }

    public void addAll(List<Skhu> skhus){
        skhuArticles.addAll(skhus);
    }

    @Override
    public int getCount() {
        return skhuArticles.size();
    }

    @Override
    public Object getItem(int position) {
        return skhuArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null || (Integer) convertView.getTag() != position){
            convertView = mInflater.inflate(R.layout.list_home_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.home_no);
            textView.setText(position + "");
            convertView.setTag(position);
        }
        return convertView;
    }
}
