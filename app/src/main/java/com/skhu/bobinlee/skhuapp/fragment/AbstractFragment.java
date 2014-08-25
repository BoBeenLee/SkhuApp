package com.skhu.bobinlee.skhuapp.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public abstract class AbstractFragment extends Fragment {
    abstract public AbstractFragment newInstance();
    abstract public void initResource();
    abstract public void initEvent();
}
