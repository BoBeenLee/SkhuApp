package com.skhu.bobinlee.skhuapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.adapter.FoodAdapter;
import com.skhu.bobinlee.skhuapp.model.APICode;
import com.skhu.bobinlee.skhuapp.model.Alarm;
import com.skhu.bobinlee.skhuapp.model.Food;
import com.skhu.bobinlee.skhuapp.model.code.SK0005;
import com.skhu.bobinlee.skhuapp.thread.PostMessageTask;
import com.skhu.bobinlee.skhuapp.util.CommonUtils;
import com.skhu.bobinlee.skhuapp.util.JacksonUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AbstractAsyncActivity {
    private ListView mFoodView;
    private FoodAdapter mFoodAdapter;
    List<Food> mFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initResource();
    }

    public void initResource(){
        mFoods = new ArrayList<Food>();
        mFoodAdapter = new FoodAdapter(this, mFoods);

        mFoodView = (ListView) findViewById(R.id.normal_list);
        mFoodView.setAdapter(mFoodAdapter);
        getMenu();
    }

    public void getMenu(){
        APICode reqCode = new APICode();
        SK0005 sk = new SK0005();
        reqCode.tranCd = "SK0005";

        PostMessageTask.postJson(this, reqCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                APICode<SK0005> resCode = JacksonUtils.<APICode<SK0005>>jsonToObject(response.toString(), new TypeReference<APICode<SK0005>>() {
                });
                SK0005 sk = resCode.tranData;

                for(int i=0; sk.res != null && i < sk.res.size(); i++){
                    Food food = new Food();
                    food.date = sk.res.get(i).menuDate;
                    food.lunch = sk.res.get(i).lunch;
                    food.specialLunch = sk.res.get(i).specialLunch;
                    food.dinner = sk.res.get(i).dinner;
                    mFoods.add(food);
                }
                mFoodAdapter.notifyDataSetChanged();
            }
        });
    }
}
