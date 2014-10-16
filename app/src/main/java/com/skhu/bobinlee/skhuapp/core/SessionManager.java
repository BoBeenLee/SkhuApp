package com.skhu.bobinlee.skhuapp.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.skhu.bobinlee.skhuapp.util.StringUtils;

/**
 * Created by BoBinLee on 2014-08-01.
 * 카카오톡, 페이스북, 서버로그인 부분
 */
public class SessionManager {
    private static SessionManager mSessionManager;
	private SharedPreferences mPref;
	private SharedPreferences.Editor mEditor;
	private Context mContext;

	// Shared pref mode
	private int PRIVATE_MODE = 0;

    // Sharedpref file name
	private static final String PREF_NAME = "SessionPref";
	// Cates
	public static final String KEY_CATES = "cates";
    // Parent Cate Names
    public static final String KEY_PARENT_CATE_NM = "parentCateNms";
    // Token
    public static final String KEY_TOKEN_ID = "tokenId";

	public SessionManager(Context context) {
		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mPref.edit();
	}

	public static SessionManager getInstance(Context context) {
		if(mSessionManager == null) {
            mSessionManager = new SessionManager(context);
            mSessionManager.createSession();
        }
		return mSessionManager;
	}

	public void createSession() {
        // Storing name in pref
        mEditor.putString(KEY_PARENT_CATE_NM, null);
        mEditor.putString(KEY_CATES, null);
        mEditor.putString(KEY_TOKEN_ID, null);
		mEditor.commit();
	}

    public void setSessionDetails(HashMap<String, Object> sessions){
        List<Integer> cates = null;
        String strCates = null, strParentCateNms = null, tokenId = null;
        if(sessions.get(KEY_CATES) != null) {
            cates = (List<Integer>) sessions.get(KEY_CATES);
            strCates = StringUtils.<Integer>join(cates.toArray(new Integer[cates.size()]), ",");
        }
        if(sessions.get(KEY_PARENT_CATE_NM) != null) {
            strParentCateNms = JacksonUtils.objectToJson((HashMap<Integer, String>) sessions.get(KEY_PARENT_CATE_NM));
        }
        if(sessions.get(KEY_TOKEN_ID) != null) {
            tokenId = (String) sessions.get(KEY_TOKEN_ID);
        }
        // Storing name in pref
        mEditor.putString(KEY_CATES, strCates);
        mEditor.putString(KEY_PARENT_CATE_NM, strParentCateNms);
        mEditor.putString(KEY_TOKEN_ID, tokenId);
        mEditor.commit();
    }

	public HashMap<String, Object> getSessionDetails() {
		HashMap<String, Object> sessions = new HashMap<String, Object>();

        String strCates = mPref.getString(KEY_CATES, null);
        String strParentCateNms = mPref.getString(KEY_PARENT_CATE_NM, null);
        String tokenId = mPref.getString(KEY_TOKEN_ID, null);

        List<Integer> cates = null;
        if(strCates != null) {
            String[] strings = strCates.split(",");
            cates = new ArrayList<Integer>();
            for (int i = 0; strings != null && i < strings.length; i++)
                cates.add(Integer.parseInt(strings[i]));
        }
        sessions.put(KEY_CATES, cates);
        if(strParentCateNms != null)
            sessions.put(KEY_PARENT_CATE_NM, JacksonUtils.jsonToObject(strParentCateNms, new TypeReference<HashMap<Integer, String>>() {  }));
        sessions.put(KEY_TOKEN_ID, tokenId);
		return sessions;
	}
}
