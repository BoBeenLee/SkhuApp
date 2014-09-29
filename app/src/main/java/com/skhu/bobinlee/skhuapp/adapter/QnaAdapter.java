package com.skhu.bobinlee.skhuapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Qna;
import com.skhu.bobinlee.skhuapp.model.code.PS0004;
import com.skhu.bobinlee.skhuapp.thread.DownloadImageTask;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by BoBinLee on 2014-09-18.
 */
public class QnaAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Qna> mQnas;

    private int refreshCnt;

    public QnaAdapter(Context context, List<Qna> qnas) {
        super();
        mContext = context;
        mQnas = qnas;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshCnt = 0;
    }

    @Override
    public int getCount() {
        return mQnas.size();
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

        if(convertView == null || holder.position != position || holder.refresh != refreshCnt){
            convertView = mInflater.inflate(R.layout.list_qna_item, null);
            holder = new Holder();

            holder.refresh = refreshCnt;
            holder.position = position;
            holder.no = (TextView) convertView.findViewById(R.id.qna_no);
            holder.title = (TextView) convertView.findViewById(R.id.qna_title);
            holder.writer = (TextView) convertView.findViewById(R.id.qna_writer);
            holder.created = (TextView) convertView.findViewById(R.id.qna_created);
            holder.replyState = (TextView) convertView.findViewById(R.id.qna_reply);
            
            holder.no.setText("" + (position + 1));
            holder.title.setText(mQnas.get(position).title);
            holder.writer.setText(mQnas.get(position).writer);
            holder.created.setText(mQnas.get(position).created);

            int state = mQnas.get(position).replyState;
            String replyState = "";
            switch (state){
                case 1 :
                    replyState = "확인중";
                    break;
                case 2 :
                    replyState = "답변완료";
                    break;
                case 3 :
                    replyState = "유선통보";
                    break;
                case 4 :
                    replyState = "이메일통보";
                    break;
            }
            holder.replyState.setText(replyState);

            // event
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // 팝업창 띄우기
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mContext);
                    confirmDialog.setTitle("선택");
                    confirmDialog.setPositiveButton("링크 이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mQnas.get(position).link));
                            mContext.startActivity(intent);
                        }
                    });
                    confirmDialog.setNeutralButton("알랑 설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addAlarm(mQnas.get(position).cateNo, mQnas.get(position).no, mQnas.get(position).title);
                        }
                    });
                    confirmDialog.setNegativeButton("취소", null);
                    confirmDialog.show();
                }
            });
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class Holder {
        public int refresh;
        public int position;
        public TextView no, title, writer, created, replyState;
    }

    public void addAlarm(int cateNo, String srcNo, String filter){
        APICode reqCode = new APICode();
        PS0004 ps = new PS0004();
        reqCode.tranCd = "PS0004";

        ps.cateNo = cateNo;
        ps.srcNo = srcNo;
        try {
            ps.filter = URLEncoder.encode(filter, "UTF-8");
        } catch(Exception e){ e.printStackTrace(); }

        ps.mac = CommonUtils.getMACAddress(mContext.getString(R.string.network_eth));
        if(ps.mac == null || ps.mac.trim().equals(""))
            ps.mac = CommonUtils.getMACAddress(mContext.getString(R.string.network_eth1));

        reqCode.tranData = ps;
        PostMessageTask.postJson(mContext, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0004> resCode = JacksonUtils.<APICode<PS0004>>jsonToObject(response.toString(), new TypeReference<APICode<PS0004>>() { });
                PS0004 ps = resCode.tranData;

                if (ps.resultYn.equals("Y"))
                    Toast.makeText(mContext, "추가완료", Toast.LENGTH_SHORT).show();
                else if(ps.resultYn.equals("N"))
                    Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, ps.resultYn, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
