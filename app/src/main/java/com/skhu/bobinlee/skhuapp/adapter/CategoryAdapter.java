package com.skhu.bobinlee.skhuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.Alarm;
import com.skhu.bobinlee.skhuapp.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-09-10.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Category> mCategories;
    private LayoutInflater mInflater;

    public CategoryAdapter(Context context, List<Category> categories) {
        super();
        mContext = context;
        mCategories = categories;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.position != position){
            convertView = mInflater.inflate(R.layout.list_category_item, null);
            holder = new Holder();

            holder.position = position;
            holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);


            holder.categoryName.setText(mCategories.get(position).name);
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class Holder {
        public int position;
        public TextView categoryName;
    }

    public void add(Category category){
        mCategories.add(category);
    }

    public void clear(){
        mCategories.clear();
    }

    @Override
    public int getCount() {
        return mCategories.size();
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
