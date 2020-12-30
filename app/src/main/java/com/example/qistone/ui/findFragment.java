package com.example.qistone.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qistone.Dialog.ShareDiaog;
import com.example.qistone.R;
import com.example.qistone.Utils.ShareUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link findFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class findFragment extends Fragment {
    ShareDiaog shareDiaog;
    //分享标题
    private String share_title="奇石鉴赏";
    //分享链接
    private String share_url="myapp://jp.app/openwith?name=zhangsan&age=26";
    //分享封面图片
    private String share_img="";
    //分享描述
    private String share_desc="您的好友发现了新的奇石，快来看看吧！";
    //进入朋友圈
    private LinearLayout ll_friend_zone;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public findFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment findFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static findFragment newInstance(String param1, String param2) {
        findFragment fragment = new findFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll_friend_zone=getActivity().findViewById(R.id.ll_friend_zone);
        ll_friend_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ZoneActivity.class);
                startActivity(intent);
            }
        });
        getActivity().findViewById(R.id.ll_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDiaog = new ShareDiaog(getContext());
                shareDiaog.builder().show();
                shareDiaog.setShareClickListener(shareClickListener);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

        /**
     * 各平台分享按钮点击事件
     */
    private ShareDiaog.ShareClickListener shareClickListener=new ShareDiaog.ShareClickListener() {
        @Override
        public void shareWechat() {
            ShareUtils.shareWechat(share_title,share_url,share_desc,share_img,platformActionListener);
        }
        @Override
        public void sharewebo() {
            ShareUtils.shareWeibo(share_title,share_url,share_desc,share_img,platformActionListener);
        }
        @Override
        public void shareQQ() {
            ShareUtils.shareQQ(share_title,share_url,share_desc,share_img,platformActionListener);
        }
        @Override
        public void shareQzone() {
            ShareUtils.shareQQzone(share_title,share_url,share_desc,share_img,platformActionListener);
        }
    };
    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("kid","分享成功");
        }
        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("kid","分享失败");
        }
        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("kid","分享取消");
        }
    };

}