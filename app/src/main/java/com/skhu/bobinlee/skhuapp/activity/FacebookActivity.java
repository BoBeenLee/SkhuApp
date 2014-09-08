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
import com.skhu.bobinlee.skhuapp.model.Facebook;
import com.skhu.bobinlee.skhuapp.model.Home;
import com.skhu.bobinlee.skhuapp.model.TabMenu;
import com.skhu.bobinlee.skhuapp.model.code.SK0002;
import com.skhu.bobinlee.skhuapp.model.code.SK0002;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;
import com.viewpagerindicator.TabPageIndicator;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacebookActivity extends AbstractAsyncActivity {
    public static final String[] FACEBOOK_TITLE = {"1", "2", "3"};
    public static final int[] FACEBOOK_ID = {1, 2, 3};

    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        initResource();
        initEvent();
    }

    public List<TabMenu> getMenus(){
        ArrayList<TabMenu> menus = new ArrayList<TabMenu>();
        for(int i=0; i<FACEBOOK_TITLE.length; i++){
            TabMenu menu = new TabMenu();
            menu.title = FACEBOOK_TITLE[i];
            menu.id = FACEBOOK_ID[i];
            menus.add(menu);
        }
        return menus;
    }

    public void initResource(){
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), getMenus(), new FacebookFragment());
        viewPager = (ViewPager) findViewById(R.id.facebook_pager);
        viewPager.setAdapter(tabFragmentPagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.facebook_indicator);
        indicator.setViewPager(viewPager);
    }

    public void initEvent()    {

    }
}
