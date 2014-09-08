package com.skhu.bobinlee.skhuapp.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.HomeAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.code.SK0001;
import com.skhu.bobinlee.skhuapp.model.code.SK0001.*;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AbstractAsyncActivity {

    private HomeAdapter mHomeAdapter;
    private PullToRefreshListView mHomeView;
    private List<Home> mHomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initResource();
        initEvent();
    }

    public void initResource(){
        mHomes = new ArrayList<Home>();
/*
        for(int i=0; i<10; i++){
            Home home = new Home();
            home.no = i + 1;
            home.cateNo = 1;
            home.created = "2010-1-1";
            home.title = "title title title";
            home.writer = "bobinlee";
            mHomes.add(home);
        }
*/
        mHomeView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        final ListView actualListView = mHomeView.getRefreshableView();
        mHomeAdapter = new HomeAdapter(this, mHomes);

        actualListView.setAdapter(mHomeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        postHomes();
    }

    public void initEvent(){

    }

    public void postHomes(){
        APICode reqCode = new APICode();
        SK0001 sk = new SK0001();
        reqCode.tranCd = "SK0001";
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0001> resCode = JacksonUtils.<APICode<SK0001>>jsonToObject(response.toString(), new TypeReference<APICode<SK0001>>() {
                });
                SK0001 sk = resCode.tranData;
                List<SK0001Article> articles = sk.res;

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
                    mHomes.add(home);
                }
                mHomeAdapter.notifyDataSetChanged();
            }
        });
    }
}
