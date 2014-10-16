package com.skhu.bobinlee.skhuapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.fasterxml.jackson.core.type.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.FacebookAdapter;
import com.skhu.bobinlee.skhuapp.adapter.HomeAdapter;
import com.skhu.bobinlee.skhuapp.adapter.TabFragmentPagerAdapter;
import com.skhu.bobinlee.skhuapp.fragment.FacebookFragment;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Alarm;
import com.skhu.bobinlee.skhuapp.model.Category;
import com.skhu.bobinlee.skhuapp.model.Facebook;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.TabMenu;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.model.code.SK0004;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.viewpagerindicator.TabPageIndicator;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacebookActivity extends CommonActivity {
    private ArrayList<TabMenu> menus;

    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private TabPageIndicator tabIndicator;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_facebook);
        super.onCreate(savedInstanceState);
        initResource();
        initEvent();
    }

    public void getMenus(){
        APICode reqCode = new APICode();
        SK0004 sk = new SK0004();
        reqCode.tranCd = "SK0004";

        sk.type = SK0004.DEPTH;
        sk.dbType = 2;
        sk.depth = 1;
        reqCode.tranData = sk;

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0004> resCode = JacksonUtils.<APICode<SK0004>>jsonToObject(response.toString(), new TypeReference<APICode<SK0004>>() { });
                SK0004 sk = resCode.tranData;
                List<SK0004.SK0004Category> categories = sk.res;

                menus.clear();
                for (int i=0; categories != null && i < categories.size(); i++) {
                    TabMenu tabMenu = new TabMenu();
                    tabMenu.title = categories.get(i).name;
                    tabMenu.id = categories.get(i).cateNo;
                    menus.add(tabMenu);
                }
                tabFragmentPagerAdapter.notifyDataSetChanged();
                tabIndicator.notifyDataSetChanged();
            }
        });
    }

    public void initResource(){
        menus = new ArrayList<TabMenu>();

        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), menus, new FacebookFragment());
        viewPager = (ViewPager) findViewById(R.id.facebook_pager);
        viewPager.setAdapter(tabFragmentPagerAdapter);

        tabIndicator = (TabPageIndicator) findViewById(R.id.facebook_indicator);
        tabIndicator.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        getMenus();
        super.onResume();
    }

    public void initEvent()    {

    }
}
