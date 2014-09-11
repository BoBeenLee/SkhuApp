package com.skhu.bobinlee.skhuapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import com.skhu.bobinlee.skhuapp.fragment.AbstractFragment;
import com.skhu.bobinlee.skhuapp.model.TabMenu;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<TabMenu> mMenus;
    private AbstractFragment mAbstractFragment;

    public TabFragmentPagerAdapter(FragmentManager fm, List<TabMenu> menus, AbstractFragment abstractFragment) {
        super(fm);
        mMenus = menus;
        mAbstractFragment = abstractFragment;
      }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    @Override
    public Fragment getItem(int position) {
        AbstractFragment abstractFragment = mAbstractFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("cateNo", mMenus.get(position).id);
        abstractFragment.setArguments(bundle);
        return abstractFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMenus.get(position).title;
    }
}
