package com.skhu.bobinlee.skhuapp.adapter;

import android.content.Context;
import android.util.Log;
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
    private int refreshCnt;
    public int num;

    public CategoryAdapter(Context context, List<Category> categories, int num) {
        super();
        this.num = num;
        mContext = context;
        mCategories = categories;
        refreshCnt = 0;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.position != position || holder.refresh != refreshCnt){
            convertView = mInflater.inflate(R.layout.list_category_item, null);
            holder = new Holder();

//            Log.d("Category Adapter : ", "Category Adapter " + num + " : " + position + " - " + mCategories.get(position).cateNo);

            holder.refresh = refreshCnt;
            holder.position = position;
            holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
            holder.categoryName.setText(mCategories.get(position).name);
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class Holder {
        public int refresh;
        public int position;
        public TextView categoryName;
    }

    public Category get(int index){
        return mCategories.get(index);
    }

    @Override
    public void notifyDataSetChanged() {
        refreshCnt += 1;
        super.notifyDataSetChanged();
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
