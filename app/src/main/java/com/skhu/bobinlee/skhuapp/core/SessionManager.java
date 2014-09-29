package com.skhu.bobinlee.skhuapp.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        mEditor.putString(KEY_CATES, null);
		mEditor.commit();
	}

    public void setSessionDetails(HashMap<String, Object> sessions){
        List<Integer> cates = (List<Integer>) sessions.get(KEY_CATES);
        String strCates = StringUtils.<Integer>join(cates.toArray(new Integer[cates.size()]), ",");

        // Storing name in pref
        mEditor.putString(KEY_CATES, strCates);
        mEditor.commit();
    }

	public HashMap<String, Object> getSessionDetails() {
		HashMap<String, Object> sessions = new HashMap<String, Object>();

        String strCates = mPref.getString(KEY_CATES, null);

        List<Integer> cates = null;
        if(strCates != null) {
            String[] strings = strCates.split(",");
            cates = new ArrayList<Integer>();
            for (int i = 0; strings != null && i < strings.length; i++)
                cates.add(Integer.parseInt(strings[i]));
        }
        sessions.put(KEY_CATES, cates);
		return sessions;
	}
}
