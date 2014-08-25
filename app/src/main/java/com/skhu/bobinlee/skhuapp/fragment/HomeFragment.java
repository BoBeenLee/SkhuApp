package com.skhu.bobinlee.skhuapp.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.activity.AsyncActivity;
import com.skhu.bobinlee.skhuapp.adapter.HomeAdapter;
import com.skhu.bobinlee.skhuapp.config.SkhuConfig;
import com.skhu.bobinlee.skhuapp.model.data.Skhu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class HomeFragment extends AbstractFragment {
    private View mView;

    private PullToRefreshListView mPullRefreshListView;
    private HomeAdapter mAdapter;
    private int mHomeType;
    private int mPageNum;

    private ArrayList<String> mSkhuUrls;
    private GetDataTask mGetDataTask;

    private AsyncActivity mAsyncActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAsyncActivity = (AsyncActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (View) inflater.inflate(R.layout.fragment_home, null);
        initResource();
        initEvent();
        return mView;
    }

    public void initResource(){
        Bundle bundle = this.getArguments();
        this.mHomeType = bundle.getInt("type");

        mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list);

        final ListView actualListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new HomeAdapter(getActivity());

        mPageNum = 0;
        mSkhuUrls = new ArrayList<String>();
        mGetDataTask = new GetDataTask();
//        mGetDataTask.execute();

        actualListView.setAdapter(mAdapter);
    }

    public void initEvent(){
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(HomeFragment.this.getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                Log.d(HomeFragment.class.getCanonicalName(), "= onRefreshing..." + label);
                // Do work to refresh the list here.

                if(!mGetDataTask.getStat()) {
                    mGetDataTask.setPullToRefreshListView(mPullRefreshListView);
                    mGetDataTask.execute();
                } else {
                    mPullRefreshListView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public AbstractFragment newInstance()  {
        return new HomeFragment();
    }

    private class GetDataTask extends AsyncTask<Void, Void, Integer> {
        private PullToRefreshListView mListView = null;
        private AtomicBoolean mAtomicBoolean;

        public GetDataTask(){
            super();
            mAtomicBoolean = new AtomicBoolean();
            mAtomicBoolean.set(false);
        }

        public boolean getStat(){
            return mAtomicBoolean.get();
        }

        public void setPullToRefreshListView(PullToRefreshListView pullToRefreshListView){
            mListView = pullToRefreshListView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAtomicBoolean.set(true);
            mAsyncActivity.showLoadingProgressDialog();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("doInBackground", "doInBackground : " + mPageNum);
            mPageNum += 1;
            try {
                mSkhuUrls.addAll(SkhuConfig.exportArticleUrls(mPageNum, mHomeType));
                for (int i = 0; i < mSkhuUrls.size(); i++)
                    mAdapter.add(SkhuConfig.getArticle(mSkhuUrls.get(i)));
            } catch (Exception e){
                e.printStackTrace();
            }
            return mPageNum;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d("GetDataTask", "GetDataTask : " + mSkhuUrls.size());
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            if(mListView != null)
                mListView.onRefreshComplete();
            mAsyncActivity.dismissProgressDialog();
            mAtomicBoolean.set(false);
            super.onPostExecute(result);
        }
    }
}
