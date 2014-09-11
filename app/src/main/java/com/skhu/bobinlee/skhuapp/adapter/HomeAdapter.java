package com.skhu.bobinlee.skhuapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.code.SK0001;

import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Home> mHomes;

    private int refreshCnt;

    public HomeAdapter(Context context, List<Home> homes) {
        super();
        mContext = context;
        mHomes = homes;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshCnt = 0;
    }

    @Override
    public int getCount() {
        return mHomes.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.postition != position || holder.refresh != refreshCnt){
            convertView = mInflater.inflate(R.layout.list_home_item, null);
            holder = new Holder();

            holder.refresh = refreshCnt;
            holder.postition = position;
            holder.no = (TextView) convertView.findViewById(R.id.home_no);
            holder.title = (TextView) convertView.findViewById(R.id.home_title);
            holder.writer = (TextView) convertView.findViewById(R.id.home_writer);
            holder.created = (TextView) convertView.findViewById(R.id.home_created);

            holder.no.setText("" + mHomes.get(position).no);
            holder.title.setText(mHomes.get(position).title);
            holder.writer.setText(mHomes.get(position).writer);
            holder.created.setText(mHomes.get(position).created);

            // event
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // 팝업창 띄우기
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mContext);
                    confirmDialog.setTitle("해당링크로 이동하시겠습니까?");
                    confirmDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mHomes.get(position).link));
                            mContext.startActivity(intent);
                        }
                    });
                    confirmDialog.setNegativeButton("아니오", null);
                    confirmDialog.show();
                }
            });
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class Holder {
        public int refresh;
        public int postition;
        public TextView no, title, writer, created;
    }

    public void add(Home home){
        mHomes.add(home);
    }

    public void clear(){
        mHomes.clear();
    }

    @Override
    public void notifyDataSetChanged() {
        refreshCnt += 1;
        super.notifyDataSetChanged();
    }
}
