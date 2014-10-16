package com.skhu.bobinlee.skhuapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.service.textservice.SpellCheckerService;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.gcm.GCMBaseIntentService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.core.SessionManager;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Category;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.code.PS0001;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.skhu.bobinlee.skhuapp.util.Notifier;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";

    @Override
    protected void onRegistered(final Context context, final String registrationId) {
//        Log.i(TAG, "Device registered : " + registrationId);
        postGCM(context, registrationId);
    }

    public void postGCM(final Context context, String registrationId){
        PS0001 ps = new PS0001();

        ps.mac = CommonUtils.getMACAddress(context);
        ps.pushTokenId = registrationId;
        ps.pushYn = "Y";
     
        APICode reqCode = new APICode();
        reqCode.tranCd = "PS0001";
        reqCode.tranData = ps;

        PostMessageTask.postSyncJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0001> resCode = JacksonUtils.<APICode<PS0001>>jsonToObject(response.toString(), new TypeReference<APICode<PS0001>>() {
                });
                // 알람 toast 미확인
                if (resCode.tranData.resultYn != null && resCode.tranData.resultYn.equals("Y")) {
                    Log.d("postGCM", "알람 설정 완료");
                } else {
                    Log.d("postGCM", "알람 이미 설정 완료");
                }
            }
        });
    }
    
    @Override
    protected void onUnregistered(Context context, String registrationId) {
//        Log.i(TAG, "Device unregistered : " + registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // cateNM subject dbType
//        Log.i(TAG, "Received new message : " + intent.getStringExtra("cateNM") + " - " + intent.getStringExtra("subject"));
        String cateNM = intent.getStringExtra("cateNM");
        String subject = intent.getStringExtra("subject");
        String url = intent.getStringExtra("url");
        int dbType = Integer.parseInt(intent.getStringExtra("dbType"));

        try{
            Notifier notifer = new Notifier(context);
            notifer.Notify(intent.getStringExtra("cateNM"), intent.getStringExtra("subject"), url, dbType);
        }
        catch (Exception ex){
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
}
/*
public class GCMIntentService extends GCMBaseIntentService {
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    @Override
    protected void onError(Context arg0, String arg1) {
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Log.e("getmessage", "getmessage:" + msg);
        generateNotification(context, msg);
    }

    @Override
    protected void onRegistered(Context context, String reg_id) {
        Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.e("키를 제거합니다.(GCM INTENTSERVICE)", "제거되었습니다.");
    }
}
*/