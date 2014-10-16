package com.skhu.bobinlee.skhuapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.AlarmAdapter;
import com.skhu.bobinlee.skhuapp.adapter.CategoryAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Alarm;
import com.skhu.bobinlee.skhuapp.model.Category;
import com.skhu.bobinlee.skhuapp.model.DBType;
import com.skhu.bobinlee.skhuapp.model.code.PS0003;
import com.skhu.bobinlee.skhuapp.model.code.PS0004;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends CommonActivity implements View.OnClickListener {
    private EditText mEditFilter;
    private LinearLayout mBtnAddAlarm;
    private Spinner mSpinCategory1, mSpinCategory2;
    private CategoryAdapter mCategory1Adapter, mCategory2Adapter;

    private ListView mAlarmView;
    private AlarmAdapter mAlarmAdapter;
    private ArrayList<Alarm> mAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);
        super.onCreate(savedInstanceState);
        initResource();
        initEvent();
    }

    public void initResource() {
        mAlarms = new ArrayList<Alarm>();
        mAlarmAdapter = new AlarmAdapter(this, mAlarms);

        mCategory1Adapter = new CategoryAdapter(this, new ArrayList<Category>(), 1);
        mCategory2Adapter = new CategoryAdapter(this, new ArrayList<Category>(), 2);

        mEditFilter = (EditText) findViewById(R.id.filter);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mBtnAddAlarm = (LinearLayout) findViewById(R.id.btn_add_alarm);

        mSpinCategory1 = (Spinner) findViewById(R.id.spin_category1);
        mSpinCategory1.setAdapter(mCategory1Adapter);

        mSpinCategory2 = (Spinner) findViewById(R.id.spin_category2);
        mSpinCategory2.setAdapter(mCategory2Adapter);

        mAlarmView = (ListView) findViewById(R.id.normal_list);
        mAlarmView.setAdapter(mAlarmAdapter);

        // settings
        mBtnAlarm.setVisibility(View.GONE);
    }

    public void initEvent() {
        mBtnAddAlarm.setOnClickListener(this);
        mSpinCategory2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addCategories(SK0004.PARENT, 0, mCategory2Adapter.get(position).cateNo, mCategory2Adapter.get(position).dbType, mCategory1Adapter, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        mCategory2Adapter.clear();
        addCategories(SK0004.DEPTH, 2, 0, DBType.SKHU, mCategory2Adapter, true);
        addCategories(SK0004.DEPTH, 2, 0, DBType.QNA, mCategory2Adapter, false);
        listAlarms();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_alarm:
                addAlarm();
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
        }
    }

    public void listAlarms() {
        APICode reqCode = new APICode();
        PS0003 ps = new PS0003();
        reqCode.tranCd = "PS0003";

        ps.mac = CommonUtils.getMACAddress(this);
        reqCode.tranData = ps;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0003> resCode = JacksonUtils.<APICode<PS0003>>jsonToObject(response.toString(), new TypeReference<APICode<PS0003>>() {
                });
                PS0003 ps = resCode.tranData;

                mAlarmAdapter.clear();
                List<PS0003.PS0003Alarm> alarms = ps.res;
                for (int i = 0; alarms != null && i < alarms.size(); i++) {
                    Alarm alarm = new Alarm();
                    alarm.no = alarms.get(i).alarmNo;
                    alarm.cateNo = alarms.get(i).cateNo;
                    alarm.cateNm = alarms.get(i).cateNm;
                    alarm.filter = alarms.get(i).filter;
                    mAlarmAdapter.add(alarm);
                }
                mAlarmAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addAlarm() {
        APICode reqCode = new APICode();
        PS0004 ps = new PS0004();
        reqCode.tranCd = "PS0004";

        ps.cateNo = mCategory1Adapter.get(mSpinCategory1.getSelectedItemPosition()).cateNo;
        try {
            ps.filter = URLEncoder.encode(mEditFilter.getText().toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ps.mac = CommonUtils.getMACAddress(this);
        reqCode.tranData = ps;
        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0004> resCode = JacksonUtils.<APICode<PS0004>>jsonToObject(response.toString(), new TypeReference<APICode<PS0004>>() {
                });
                PS0004 ps = resCode.tranData;

                if (ps.resultYn.equals("Y")) {
                    mEditFilter.setText("");
                    listAlarms();
                    Toast.makeText(getApplicationContext(), "추가완료", Toast.LENGTH_SHORT).show();
                } else if (ps.resultYn.equals("N"))
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), ps.resultYn, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCategories(int type, int depth, int parentNo, int dbType, final CategoryAdapter adapter, final boolean isClear) {
        APICode reqCode = new APICode();
        SK0004 sk = new SK0004();
        reqCode.tranCd = "SK0004";

        sk.type = type;
        sk.dbType = dbType;
        sk.depth = depth;
        sk.parentCateNo = parentNo;
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0004> resCode = JacksonUtils.<APICode<SK0004>>jsonToObject(response.toString(), new TypeReference<APICode<SK0004>>() {
                });
                SK0004 sk = resCode.tranData;
                List<SK0004.SK0004Category> categories = sk.res;

                if (isClear)
                    adapter.clear();
                for (int i = 0; categories != null && i < categories.size(); i++) {
                    Category category = new Category();
                    category.cateNo = categories.get(i).cateNo;
                    category.cateId = categories.get(i).cateId;
                    category.depth = categories.get(i).depth;
                    category.name = categories.get(i).name;
                    category.dbType = categories.get(i).dbType;
                    category.parentCateNo = categories.get(i).parentCateNo;
                    adapter.add(category);
                }
//                for(int i=0; i<adapter.getCount(); i++){
//                    Log.d("category SK0004 : ", adapter.get(i).cateId + " - " + adapter.get(i).name);
//                }
                adapter.notifyDataSetChanged();
                mSpinCategory1.invalidate();
            }
        });
    }
}
