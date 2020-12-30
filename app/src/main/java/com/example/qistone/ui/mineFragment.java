package com.example.qistone.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qistone.Dialog.PictureDialog;
import com.example.qistone.Login.LoginActivity;
import com.example.qistone.R;
import com.example.qistone.Utils.DBUtils;
import com.leon.lib.settingview.LSettingItem;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mineFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //初始化组件
    private LSettingItem item_one;
    private LSettingItem collect;
    private LSettingItem Photo_box;
    private LSettingItem item_five;
    private LSettingItem out;

    public mineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mineFragment newInstance(String param1, String param2) {
        mineFragment fragment = new mineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //图片和姓名加载上去
        HashMap<String, Object> map= (HashMap<String, Object>) requireActivity().getIntent().getSerializableExtra("map");
        Bitmap bitmap= DBUtils.getBitmapFromByte(DBUtils.strToByteArray((String) map.get("headImage")));
        CircleImageView headimage=getActivity().findViewById(R.id.profile_image);
        TextView name=getActivity().findViewById(R.id.name);
        name.setText((String)map.get("user_name"));
        if (bitmap!=null) {
            headimage.setImageBitmap(bitmap);
        }
       /**
        * 初始化
        * **/
        item_one=(LSettingItem)getActivity().findViewById(R.id.item_one);
        collect=(LSettingItem)getActivity().findViewById(R.id.collect);
        Photo_box=(LSettingItem)getActivity().findViewById(R.id.Photo_box);
        item_five=(LSettingItem)getActivity().findViewById(R.id.item_five);
        out=(LSettingItem)getActivity().findViewById(R.id.out);

        /**
         * 监听
         * */
        item_one.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {

            }
        });
        collect.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {

            }
        });

        Photo_box.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                // 打开系统图库的 Action，等同于: "android.intent.action.GET_CONTENT"
                Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                // 设置数据类型为图片类型
                choiceFromAlbumIntent.setType("image/*");
                getActivity().startActivity(choiceFromAlbumIntent);
            }
        });

        item_five.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        out.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }
}