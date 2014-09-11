package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.code.PS0003;
import com.skhu.bobinlee.skhuapp.model.code.PS0004;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener{
    private EditText mEditFilter;
    private Button mBtnAlarm;
    private Spinner mSpinCategory1, mSpinCategory2;
    private CategoryAdapter mCategory1Adapter, mCategory2Adapter;
    private ArrayList<Category> mCategories1, mCategories2;

    private ListView mAlarmView;
    private AlarmAdapter mAlarmAdapter;
    private ArrayList<Alarm> mAlarms;
    private int selectedCateNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initResource();
        initEvent();
    }

    public void initResource(){
        mAlarms = new ArrayList<Alarm>();
        mAlarmAdapter = new AlarmAdapter(this, mAlarms);

        mCategories1 = new ArrayList<Category>();
        mCategories2 = new ArrayList<Category>();

        mCategory1Adapter = new CategoryAdapter(this, mCategories1);
        mCategory2Adapter = new CategoryAdapter(this, mCategories2);

        mEditFilter = (EditText) findViewById(R.id.filter);
        mBtnAlarm = (Button) findViewById(R.id.btn_alarm);

        mSpinCategory1 = (Spinner) findViewById(R.id.spin_category1);
        mSpinCategory1.setAdapter(mCategory1Adapter);

        mSpinCategory2 = (Spinner) findViewById(R.id.spin_category2);
        mSpinCategory2.setAdapter(mCategory2Adapter);

        mAlarmView = (ListView) findViewById(R.id.normal_list);
        mAlarmView.setAdapter(mAlarmAdapter);
    }

    public void initEvent(){
        mBtnAlarm.setOnClickListener(this);
        mSpinCategory1.setOnItemSelectedListener(this);
        mSpinCategory2.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        addCategories(SK0004.DEPTH, 2, 0, mCategory2Adapter);
        listAlarms();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_alarm :
                addAlarm();
                break;
        }
    }

    public void listAlarms(){
        APICode reqCode = new APICode();
        PS0003 ps = new PS0003();
        reqCode.tranCd = "PS0003";

        ps.mac = CommonUtils.getMACAddress(this.getString(R.string.network_eth));
        reqCode.tranData = ps;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0003> resCode = JacksonUtils.<APICode<PS0003>>jsonToObject(response.toString(), new TypeReference<APICode<PS0003>>() { });
                PS0003 ps = resCode.tranData;

                mAlarmAdapter.clear();
                List<PS0003.PS0003Alarm> alarms = ps.res;
                for(int i=0; alarms != null && i < alarms.size(); i++){
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

    public void addAlarm(){
        APICode reqCode = new APICode();
        PS0004 ps = new PS0004();
        reqCode.tranCd = "PS0004";

        ps.cateNo = selectedCateNo;
        ps.filter = mEditFilter.getText().toString();
        ps.mac = CommonUtils.getMACAddress("eth0");

        reqCode.tranData = ps;
        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<PS0004> resCode = JacksonUtils.<APICode<PS0004>>jsonToObject(response.toString(), new TypeReference<APICode<PS0004>>() { });
                PS0004 ps = resCode.tranData;

                if(ps.resultYn.equals("Y")){
                    mEditFilter.setText("");
                    listAlarms();
                    Toast.makeText(getApplicationContext(), "추가완료", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCategories(int type, int depth, int parentNo, final CategoryAdapter adapter){
        APICode reqCode = new APICode();
        SK0004 sk = new SK0004();
        reqCode.tranCd = "SK0004";

        sk.type = type;
        sk.dbType = 1;
        sk.depth = depth;
        sk.parentCateNo = parentNo;
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0004> resCode = JacksonUtils.<APICode<SK0004>>jsonToObject(response.toString(), new TypeReference<APICode<SK0004>>() { });
                SK0004 sk = resCode.tranData;
                List<SK0004.SK0004Category> categories = sk.res;

                adapter.clear();
                for (int i=0; categories != null && i < categories.size(); i++) {
                    Category category = new Category();
                    category.cateNo = categories.get(i).cateNo;
                    category.cateId = categories.get(i).cateId;
                    category.depth = categories.get(i).depth;
                    category.name = categories.get(i).name;
                    category.parentCateNo = categories.get(i).parentCateNo;
                    adapter.add(category);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(mSpinCategory1)){
            selectedCateNo = mCategories1.get(position).cateNo;
            Toast.makeText(this, "1 : " + selectedCateNo, Toast.LENGTH_SHORT).show();
        } else if(parent.equals(mSpinCategory2)){
//            Toast.makeText(this, "2 : " + position, Toast.LENGTH_SHORT).show();
            addCategories(SK0004.PARENT, 0, mCategories2.get(position).cateNo, mCategory1Adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
