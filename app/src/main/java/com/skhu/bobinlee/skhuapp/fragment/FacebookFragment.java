package com.skhu.bobinlee.skhuapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.activity.AsyncActivity;
import com.skhu.bobinlee.skhuapp.adapter.FacebookAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Facebook;
import com.skhu.bobinlee.skhuapp.model.code.SK0002;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.DateUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class FacebookFragment extends AbstractFragment {
    private View mView;

    private FacebookAdapter mFacebookAdapter;
    private PullToRefreshListView mFacebookView;
    private List<Facebook> mFacebooks;

    private AsyncActivity mAsyncActivity;
    private int cateNo;
    private String lastNo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAsyncActivity = (AsyncActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (View) inflater.inflate(R.layout.fragment_facebook, null);
        initResource();
        initEvent();
        return mView;
    }

    public void initResource(){
        Bundle bundle = this.getArguments();
        cateNo = bundle.getInt("cateNo");

        mFacebookView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list);
        final ListView actualListView = mFacebookView.getRefreshableView();

        mFacebooks = new ArrayList<Facebook>();
        mFacebookAdapter = new FacebookAdapter(getActivity(), mFacebooks);

        actualListView.setAdapter(mFacebookAdapter);
    }

    public void initEvent(){
        // Set a listener to be invoked when the list should be refreshed.
        mFacebookView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(FacebookFragment.this.getActivity().getApplicationContext(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                Log.d(FacebookFragment.class.getCanonicalName(), "= onRefreshing..." + label);
//                mPullRefreshListView.onRefreshComplete();
                Timestamp created = null;
//                Log.d("onRefresh", "onRefresh");
                if(lastNo != null)
                    postFacebooks(cateNo, lastNo);
                else
                    postFacebooks(cateNo, initReqPoNo());
            }
        });
    }

    @Override
    public AbstractFragment newInstance()  {
        return new FacebookFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("onResume", "onResume");
        postFacebooks(cateNo, initReqPoNo());
    }

    public String initReqPoNo(){
        String reqPoNo = String.valueOf(DateUtils.stringToDate(DateUtils.currentTime()).getTime());
        reqPoNo = reqPoNo.substring(0, reqPoNo.length() - 3);
        return reqPoNo;
    }

    public void postFacebooks(int cateNo, String reqPoNo){
        SK0002 sk = new SK0002();

        sk.reqPoNo = reqPoNo;
        sk.reqPoCnt = 15;
        sk.cateNo = cateNo;

        APICode reqCode = new APICode();
        reqCode.tranCd = "SK0002";
        reqCode.tranData = sk;

        PostMessageTask.postJson(getActivity(), reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0002> resCode = JacksonUtils.<APICode<SK0002>>jsonToObject(response.toString(), new TypeReference<APICode<SK0002>>() { });
                SK0002 sk = resCode.tranData;
                List<SK0002.SK0002Article> articles = sk.res;

                for (SK0002.SK0002Article article : articles) {
                    Facebook facebook = new Facebook();
                    facebook.no = article.brdNo;
                    facebook.writer = article.brdNm;
                    facebook.created = article.brdCreated;
                    facebook.cateNo = article.brdCate;
                    if (article.brdContent != null)
                        facebook.content = new String(article.brdContent);
                    facebook.link = article.brdUrl;
                    facebook.img = article.brdImgUrl;
                    mFacebooks.add(facebook);
                }
                lastNo = sk.resLastNo;
                mFacebookAdapter.notifyDataSetChanged();
                mFacebookView.onRefreshComplete();
            }
        });
    }
}
