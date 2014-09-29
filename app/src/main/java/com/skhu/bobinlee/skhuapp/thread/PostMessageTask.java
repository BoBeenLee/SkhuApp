package com.skhu.bobinlee.skhuapp.thread;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonStreamerEntity;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by BoBinLee on 2014-09-04.
 */
public class PostMessageTask {
    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static AsyncHttpClient syncClient = new SyncHttpClient();

    public static void postJson(Context context, APICode reqCode, AsyncHttpResponseHandler responseHandler) {
        String url = context.getString(R.string.base_uri);

        StringEntity jsonParams = null;
        try {
            jsonParams = new StringEntity(JacksonUtils.objectToJson(reqCode));
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("postJson", "" + JacksonUtils.objectToJson(reqCode));
        asyncClient.post(context, url, jsonParams, "application/json",
                responseHandler);
    }

    public static void postSyncJson(Context context, APICode reqCode, AsyncHttpResponseHandler responseHandler) {
        String url = context.getString(R.string.base_uri);

        StringEntity jsonParams = null;
        try {
            jsonParams = new StringEntity(JacksonUtils.objectToJson(reqCode));
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("postSyncJson", "" + JacksonUtils.objectToJson(reqCode));
        syncClient.post(context, url, jsonParams, "application/json",
                responseHandler);
    }
}
