package com.skhu.bobinlee.skhuapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.skhu.bobinlee.skhuapp.adapter.QnaAdapter;
import com.skhu.bobinlee.skhuapp.adapter.QnaAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.DBType;
import com.skhu.bobinlee.skhuapp.model.Qna;
import com.skhu.bobinlee.skhuapp.model.Qna;
import com.skhu.bobinlee.skhuapp.model.code.SK0001;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.DateUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-09-18.
 */
public class QnaFragment extends AbstractFragment {
    private View mView;

    private QnaAdapter mQnaAdapter;
    private PullToRefreshListView mQnaView;
    private List<Qna> mQnas;

    private AsyncActivity mAsyncActivity;
    private int cateNo;
    private int startNo;

    @Override
    public void onAttach(Activity activity) {
        mAsyncActivity = (AsyncActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (View) inflater.inflate(R.layout.fragment_qna, null);
        initResource();
        initEvent();
        return mView;
    }

    public void initResource(){
        Bundle bundle = this.getArguments();
        cateNo = bundle.getInt("cateNo");

        mQnaView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list);
        final ListView actualListView = mQnaView.getRefreshableView();

        mQnas = new ArrayList<Qna>();
        mQnaAdapter = new QnaAdapter(getActivity(), mQnas);

        actualListView.setAdapter(mQnaAdapter);
    }

    public void initEvent(){
        // Set a listener to be invoked when the list should be refreshed.
        mQnaView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(QnaFragment.this.getActivity().getApplicationContext(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                Log.d(QnaFragment.class.getCanonicalName(), "= onRefreshing..." + label);
//                mPullRefreshListView.onRefreshComplete();
//                Log.d("onRefresh", "onRefresh");
                  postQnas();
            }
        });
    }

    @Override
    public AbstractFragment newInstance()  {
        return new QnaFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("onResume", "onResume");
        postQnas();
    }

    public void postQnas(){
        SK0001 sk = new SK0001();

        sk.reqPoNo = startNo;
        sk.reqPoCnt = 15;

        ArrayList<Integer> cates = new ArrayList<Integer>();
        cates.add(cateNo);
        sk.cateNo = cates;
        sk.dbType = DBType.QNA;

        APICode reqCode = new APICode();
        reqCode.tranCd = "SK0001";
        reqCode.tranData = sk;

        PostMessageTask.postJson(getActivity(), reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0001> resCode = JacksonUtils.<APICode<SK0001>>jsonToObject(response.toString(), new TypeReference<APICode<SK0001>>() {
                });
                SK0001 sk = resCode.tranData;
                List<SK0001.SK0001Article> articles = sk.res;
                for (SK0001.SK0001Article article : articles) {
                    Qna qna = new Qna();
                    qna.no = article.brdNo;
                    qna.title = article.brdSubject;
                    qna.writer = article.brdNm;
                    qna.created = article.brdCreated;
                    qna.cateNo = article.brdCate;
                    qna.cateNm = article.brdCateNm;
                    if (article.brdContent != null)
                        qna.content = new String(article.brdContent);
                    qna.link = article.brdUrl;
                    qna.replyState = article.replyState;
                    mQnas.add(qna);
                }
                startNo = sk.resLastNo;
                mQnaAdapter.notifyDataSetChanged();
                mQnaView.onRefreshComplete();
            }
        });
    }
}
