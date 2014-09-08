package com.skhu.bobinlee.skhuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.Facebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class FacebookAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Facebook> mFacebooks;

    public FacebookAdapter(Context context, List<Facebook> facebooks) {
        super();
        mContext = context;
        mFacebooks = facebooks;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFacebooks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.postition != position){
            convertView = mInflater.inflate(R.layout.list_facebook_item, null);
            holder = new Holder();

            holder.postition = position;
            holder.no = (TextView) convertView.findViewById(R.id.facebook_no);
            holder.title = (TextView) convertView.findViewById(R.id.facebook_title);
            holder.writer = (TextView) convertView.findViewById(R.id.facebook_writer);
            holder.created = (TextView) convertView.findViewById(R.id.facebook_created);

            holder.no.setText("" + mFacebooks.get(position).no);
            holder.title.setText(mFacebooks.get(position).title);
            holder.writer.setText(mFacebooks.get(position).writer);
            holder.created.setText(mFacebooks.get(position).created);

            convertView.setTag(holder);
        }
        return convertView;
    }
    private class Holder {
        public int postition;
        public TextView no, title, writer, created;
    }
}
