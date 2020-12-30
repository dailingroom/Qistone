package com.example.qistone.ui;

import android.app.usage.UsageEvents;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.qistone.Adapter.StoneAdapter;
import com.example.qistone.R;
import com.example.qistone.ViewModel.HomeViewModel;
import com.example.qistone.entity.Stone;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

public class home extends Fragment {
   //列表
   RecyclerView mRecyclerView;
    ArrayList<Stone> list=new ArrayList<>();

    private HomeViewModel mViewModel;
    private CollapsingToolbarLayoutState state=CollapsingToolbarLayoutState.COLLAPSED;;
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    public static home newInstance() {
        return new home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //recyclerView
        mRecyclerView=getActivity().findViewById(R.id.rv);
        initStone();
        //定义布局管理器为Grid管理器，设置一行放3个
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        //给RecyclerView添加布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        StoneAdapter adapter=new StoneAdapter(list);
        mRecyclerView.setAdapter(adapter);

        //滑动栏
        AppBarLayout app_bar=(AppBarLayout)getActivity().findViewById(R.id.app_bar);
        final CollapsingToolbarLayout collapsingToolbarLayout=getActivity().findViewById(R.id.collapsingToolbarLayout);
        final LinearLayout search_bar=getActivity().findViewById(R.id.search_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为展开
                        collapsingToolbarLayout.setTitle("COLLAPSED");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbarLayout.setTitle("");//设置title不显示
                        search_bar.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
                            search_bar.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        collapsingToolbarLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        // TODO: Use the ViewModel
    }
//初始化列表
    private void initStone() {
            Stone friut=new Stone("苹果",R.drawable.find);
            list.add(friut);
            Stone friut1=new Stone("香蕉",R.drawable.find);
            list.add(friut1);
            Stone friut2=new Stone("樱桃",R.drawable.find);
            list.add(friut2);
            Stone friut3=new Stone("葡萄",R.drawable.find);
            list.add(friut3);
            Stone friut4=new Stone("橘子",R.drawable.find);
            list.add(friut4);
            Stone friut5=new Stone("梨子",R.drawable.find);
            list.add(friut5);
            Stone friut6=new Stone("西瓜",R.drawable.find);
            list.add(friut6);
    }

}