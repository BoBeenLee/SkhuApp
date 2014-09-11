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
import android.widget.ImageView;
import android.widget.TextView;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.core.SessionManager;
import com.skhu.bobinlee.skhuapp.model.Facebook;
import com.skhu.bobinlee.skhuapp.thread.DownloadImageTask;

import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class FacebookAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Facebook> mFacebooks;

    private int refreshCnt;

    public FacebookAdapter(Context context, List<Facebook> facebooks) {
        super();
        mContext = context;
        mFacebooks = facebooks;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshCnt = 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView != null)
            holder = (Holder) convertView.getTag();

        if(convertView == null || holder.postition != position){
            convertView = mInflater.inflate(R.layout.list_facebook_item, null);
            holder = new Holder();

            holder.postition = position;
            holder.no = (TextView) convertView.findViewById(R.id.facebook_no);
            holder.content = (TextView) convertView.findViewById(R.id.facebook_content);
            holder.img = (ImageView) convertView.findViewById(R.id.facebook_img);
            holder.writer = (TextView) convertView.findViewById(R.id.facebook_writer);
            holder.created = (TextView) convertView.findViewById(R.id.facebook_created);

            holder.no.setText("" + (position + 1));
            if(mFacebooks.get(position).content != null) {
                holder.content.setVisibility(View.VISIBLE);
                holder.content.setText(mFacebooks.get(position).content);
            }
            if(mFacebooks.get(position).img != null){
                holder.img.setVisibility(View.VISIBLE);
                new DownloadImageTask(holder.img).execute(mFacebooks.get(position).img);
            }
            holder.writer.setText(mFacebooks.get(position).writer);
            holder.created.setText(mFacebooks.get(position).created);

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
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFacebooks.get(position).link));
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
        public TextView no, content, writer, created;
        public ImageView img;
    }
}
