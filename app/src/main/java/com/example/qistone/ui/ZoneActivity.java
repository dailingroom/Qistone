package com.example.qistone.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.qistone.Adapter.FriendZoneRecycleViewAdapter;
import com.example.qistone.Bean.DiscoverZoneBean;
import com.example.qistone.R;
import com.example.qistone.Utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 朋友圈活动
 * **/
public class ZoneActivity extends AppCompatActivity implements View.OnClickListener{

    private FriendZoneRecycleViewAdapter mAdapter;
    private List<DiscoverZoneBean> mList;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        initRecycleView();
    }
    private void initRecycleView() {
        //1)添加布局管理器
        mRecyclerView=findViewById(R.id.zone);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //2)配置Adapter
        mList = genData();
        mAdapter = new FriendZoneRecycleViewAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        //3)监听器
        mAdapter.setOnItemClickListener(new FriendZoneRecycleViewAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position, List<DiscoverZoneBean> items) {

            }
        });
    }
    private List<DiscoverZoneBean> genData() {
        List<DiscoverZoneBean> mList = new ArrayList<>();

        String response = FileUtils.getFromAssets(this, "discover_zone.json");
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                DiscoverZoneBean itemData = new DiscoverZoneBean();
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                itemData.setText(jsonObject.optString("text"));
                JSONArray images = jsonObject.optJSONArray("images");
                for (int j = 0; j < images.length(); j++) {
                    String url = images.optString(j);
                    itemData.images.add(url);
                }
                mList.add(itemData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.shuffle(mList);//打乱顺序输出，为了美观
        return mList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.detail_bar_btn_back:
//                finish();
//                break;
        }
    }
}