package com.skhu.bobinlee.skhuapp.activity;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.TabFragmentPagerAdapter;
import com.skhu.bobinlee.skhuapp.fragment.HomeFragment;
import com.skhu.bobinlee.skhuapp.model.TabMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AbstractAsyncActivity {
    private static final String[] HOME_TITLE = new String[] { "학사공지", "수업공지", "장학공지", "일반공지", "행사공지" };
    private static final int[] HOME_ID = new int[] { 10004, 10005, 10006, 10007, 10008 };
    /*
        down vote
        accepted
        You need to use Html.fromHtml() to use HTML in your XML Strings. Simply referencing a String with HTML in your layout XML will not work.
        For example:
        myTextView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
     */
    TabFragmentPagerAdapter tabFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initResource();
    }

    public List<TabMenu> getMenus(){
        ArrayList<TabMenu> menus = new ArrayList<TabMenu>();

        for(int i=0; i<HOME_TITLE.length; i++){
            TabMenu menu = new TabMenu();
            menu.title = HOME_TITLE[i];
            menu.id = HOME_ID[i];
            menus.add(menu);
        }
        return menus;
    }

    public void initResource(){
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), getMenus(), new HomeFragment());
        viewPager = (ViewPager) findViewById(R.id.home_pager);
        viewPager.setAdapter(tabFragmentPagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.home_indicator);
        indicator.setViewPager(viewPager);
    }

    public void initEvent(){

    }
}
