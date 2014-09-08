package com.skhu.bobinlee.skhuapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.gcm.GCMBaseIntentService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.code.PS0001;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.skhu.bobinlee.skhuapp.util.Notifier;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered : " + registrationId);
        postGCM(context, registrationId);
    }

    public void postGCM(final Context context, String registrationId){
        PS0001 ps = new PS0001();
        ps.mac = CommonUtils.getMACAddress("eth0");
        ps.pushTokenId = registrationId;
        ps.pushYn = "n";
     
        APICode reqCode = new APICode();
        reqCode.tranCd = "PS0001";
        reqCode.tranData = ps;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0001> resCode = JacksonUtils.<APICode<PS0001>>jsonToObject(response.toString(), new TypeReference<APICode<PS0001>>() {
                });
                if(resCode.tranData.resultYn.equals("Y")){
                    Toast.makeText(context, "알람 등록 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered : " + registrationId);

    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received new message : ");
        try{
            Notifier notifer = new Notifier(context);
            notifer.Notify("Global control system", intent.getStringExtra("message"));
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