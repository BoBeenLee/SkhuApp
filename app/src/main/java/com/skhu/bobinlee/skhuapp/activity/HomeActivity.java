package com.skhu.bobinlee.skhuapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.HomeAdapter;
import com.skhu.bobinlee.skhuapp.core.SessionManager;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Category;
import com.skhu.bobinlee.skhuapp.model.DBType;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.TabMenu;
import com.skhu.bobinlee.skhuapp.model.code.SK0001;
import com.skhu.bobinlee.skhuapp.model.code.SK0001.*;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class HomeActivity extends CommonActivity {
    private HomeAdapter mHomeAdapter;
    private PullToRefreshListView mHomeView;
    private List<Home> mHomes;

    private LinearLayout mCategoryLayout;
    private LinkedHashMap<String, List<Category>> mCategories;
    private HashMap<Integer, String> mParentCategories, selectedParentCateNms;
    private List<Integer> selectedCates;
    private int startNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);
        initResource();
        initEvent();
    }

    public void initResource(){
        selectedCates = (List<Integer>) SessionManager.getInstance(this).getSessionDetails().get(SessionManager.KEY_CATES);
        selectedParentCateNms = (HashMap<Integer, String>) SessionManager.getInstance(this).getSessionDetails().get(SessionManager.KEY_PARENT_CATE_NM);

        if(selectedParentCateNms == null)
            selectedParentCateNms = new HashMap<Integer, String>();
//        else {
//            for (Integer num : selectedParentCateNms.keySet()) {
//                Log.d("parentcates : ", "parentcate : " + num);
//            }
//        }
        if(selectedCates == null)
            selectedCates = new ArrayList<Integer>();

        mHomes = new ArrayList<Home>();
        mCategories = new LinkedHashMap<String, List<Category>>();
        mParentCategories = new HashMap<Integer, String>();

        mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);
        mHomeView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

        final ListView actualListView = mHomeView.getRefreshableView();
        mHomeAdapter = new HomeAdapter(this, mHomes);

        actualListView.setAdapter(mHomeAdapter);

        // settings
        mBtnHome.setVisibility(View.GONE);
    }

    public void initEvent(){
        mHomeView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                mPullRefreshListView.onRefreshComplete();
//                Log.d("onRefresh", "onRefresh");
                postHomes();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCategoryLayout.removeAllViews();
        listCategory(SK0004.PARENT, 0, null);
        postHomes();
    }

    public void addCategory(final String name){
        int columnSize = mCategories.get(name).size() / 2 + 1;
        int nameColor = getResources().getColor(R.color.category_name);
        int contentColor = getResources().getColor(R.color.category_content);

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gridLayout.setOrientation(GridLayout.HORIZONTAL);
        gridLayout.setPadding(10, 0, 10, 0);
        gridLayout.setColumnCount(columnSize); // 사이즈에 따라
        gridLayout.setUseDefaultMargins(true);

        TextView categoryName = new TextView(this);
        categoryName.setText(name);
        categoryName.setTextSize(13f);
        categoryName.setTextColor(nameColor);
        categoryName.setPadding(10, 0, 0, 0);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(0, columnSize);
        categoryName.setLayoutParams(params);
        gridLayout.addView(categoryName);
//        categoryName.span

        for(final Category category : mCategories.get(name)){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(category.name);
            checkBox.setTextColor(contentColor);
            if(selectedCates.contains(category.cateNo))
                checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
                Integer cateNo = new Integer(category.cateNo);
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        selectedParentCateNms.put(cateNo, name);
                        selectedCates.add(cateNo);
                    } else {
                        selectedParentCateNms.remove(cateNo);
                        selectedCates.remove(cateNo);
                    }
                    startNo = 0;
                    postHomes();
                }
            });
            gridLayout.addView(checkBox);
        }
        mCategoryLayout.addView(gridLayout);

        // seperator line
//        View view = new View(this);
//        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(1, 1);
//        view.setBackgroundColor(getResources().getColor(R.color.home_seperator));
//        mCategoryLayout.addView(view);
    }

    public void listCategory(int type, int parentNo, String name){
        APICode reqCode = new APICode();
        SK0004 sk = new SK0004();
        reqCode.tranCd = "SK0004";

        sk.type = type;
        sk.dbType = 1;
        sk.parentCateNo = parentNo;
        reqCode.tranData = sk;

        if(name == null)
            postCategory2(reqCode);
        else
            postCategory1(reqCode, name);
    }

    public void postCategory2(APICode reqCode){
        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0004> resCode = JacksonUtils.<APICode<SK0004>>jsonToObject(response.toString(), new TypeReference<APICode<SK0004>>() { });
                SK0004 sk = resCode.tranData;
                List<SK0004.SK0004Category> categories = sk.res;

                for (int i=0; categories != null && i < categories.size(); i++) {
                    mCategories.put(categories.get(i).name, new ArrayList<Category>());
                    listCategory(SK0004.PARENT, categories.get(i).cateNo, categories.get(i).name);
                }
            }
        });
    }

    public void postCategory1(APICode reqCode, final String name){
        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0004> resCode = JacksonUtils.<APICode<SK0004>>jsonToObject(response.toString(), new TypeReference<APICode<SK0004>>() { });
                SK0004 sk = resCode.tranData;
                List<SK0004.SK0004Category> categories = sk.res;

                for (int i=0; categories != null && i < categories.size(); i++) {
                    Category category = new Category();
                    category.name = categories.get(i).name;
                    category.cateNo = categories.get(i).cateNo;
                    mCategories.get(name).add(category);
                    mParentCategories.put(category.cateNo, name);
                }
                if(mCategories != null) {
//                    Log.d("key", " key --- " + name);
                    addCategory(name);
//                    for (Category category : mCategories.get(name)) {
//                        Log.d("value", "value --- " + category.name);
//                    }
                }
            }
        });
    }

    public void postHomes(){
        APICode reqCode = new APICode();
        SK0001 sk = new SK0001();
        reqCode.tranCd = "SK0001";

//        for(int i=0; i<selectedCates.size(); i++)
//            Log.d("selectedCates", "selectedCates : " + selectedCates.get(i));
        sk.cateNo = selectedCates;
        sk.reqPoCnt = 15;
        sk.reqPoNo = startNo;
        sk.dbType = DBType.SKHU;
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0001> resCode = JacksonUtils.<APICode<SK0001>>jsonToObject(response.toString(), new TypeReference<APICode<SK0001>>() {
                });
                SK0001 sk = resCode.tranData;
                List<SK0001Article> articles = sk.res;

                if(startNo == 0)
                    mHomeAdapter.clear();
                for (SK0001Article article : articles) {
                    Home home = new Home();
                    home.no = article.brdNo;
                    home.title = article.brdSubject;
                    home.writer = article.brdNm;
                    home.created = article.brdCreated;
                    home.cateNo = article.brdCate;
                    home.cateNm = article.brdCateNm;
                    if (article.brdContent != null)
                        home.content = new String(article.brdContent);
                    home.link = article.brdUrl;
                    home.attaches = article.brdAttachUrls;
                    home.imgs = article.brdImgUrls;
                    home.parentCateNm = selectedParentCateNms.get(home.cateNo);
                    mHomeAdapter.add(home);
                }
                startNo = sk.resLastNo;
                mHomeAdapter.notifyDataSetChanged();
                mHomeView.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        SessionManager sessionManager = SessionManager.getInstance(this);

        HashMap<String, Object> sessions = sessionManager.getSessionDetails();
        sessions.put(SessionManager.KEY_CATES, selectedCates);
        sessions.put(SessionManager.KEY_PARENT_CATE_NM, selectedParentCateNms);
        sessionManager.setSessionDetails(sessions);
        super.onDestroy();
    }
}
