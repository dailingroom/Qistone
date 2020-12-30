package com.example.qistone.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qistone.MainActivity;
import com.example.qistone.R;
import com.example.qistone.Register.RegisterActivity;
import com.example.qistone.SQLlite.UserService;
import com.example.qistone.Utils.DBUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> usernamelList;

    private Button bt_login;
    private TextView bt_register;
    private TextView bt_phoneLogin;
    private ImageButton image_btn;
    private EditText edit_username;
    private EditText edit_password;
    private String phone_number;
    private ListPopupWindow listPopupWindow;
    private UserService uService = null;//用于登录账号的下选框选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    /**
    * 初始化函数，用来初始化页面布局上的内容
    * */
    private void initViews() {
        bt_login=(Button) findViewById(R.id.bt_login);
        bt_register=(TextView) findViewById(R.id.bt_register);
        bt_phoneLogin=(TextView) findViewById(R.id.bt_phoneLogin);
        image_btn=(ImageButton)findViewById(R.id.user_btn_img);
        edit_username=(EditText) findViewById(R.id.user_name);
        edit_password=(EditText) findViewById(R.id.user_pass);

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        image_btn.setOnClickListener(this);
        bt_phoneLogin.setOnClickListener(this);
        //用于登录账号的下选框选择
        uService = new UserService(LoginActivity.this);
        usernamelList = uService.getAll();

    }

    protected void onDestroy() {//销毁
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        usernamelList.clear();      //从注册返回时清除usernamelList
        usernamelList = uService.getAll(); //更新注册的内容
    }
    private boolean judPhone() {//判断手机号是否正确
        //不正确的情况
        if(TextUtils.isEmpty(edit_username.getText().toString().trim()))//对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(LoginActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            edit_username.requestFocus();//设置是否获得焦点。若有requestFocus()被调用时，后者优先处理。注意在表单中想设置某一个如EditText获取焦点，光设置这个是不行的，需要将这个EditText前面的focusable都设置为false才行。
            return false;
        }
        else if(edit_username.getText().toString().trim().length()!=11){
            Toast.makeText(LoginActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            edit_username.requestFocus();
            return false;
        }

        //正确的情况
        else{
            phone_number=edit_username.getText().toString().trim();
            String num="[1][3578]\\d{9}";
            if(phone_number.matches(num)) {
                return true;
            }
            else{
                Toast.makeText(LoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    /*
    * 用于显示登录时，本地账号的保存，并且利用弹出框进行显示
    * */
    private void showListPopulWindow() {

        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item, usernamelList));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(edit_username);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit_username.setText(usernamelList.get(i));//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });

        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login://登录监听
            {
                is_User_No();
                break;
            }
            case R.id.bt_register://注册监听
            {
                Intent intent0 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent0);
                break;
            }
            case R.id.user_btn_img://编辑框下拉监听
                showListPopulWindow(); //调用显示PopuWindow 函数
                break;
            case R.id.bt_phoneLogin:
                Toast.makeText(this, "what", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void is_User_No(){//判断用户是否存在
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                Message message = handler.obtainMessage();
                HashMap<String, Object> map = DBUtils.getInfoByName(edit_username.getText().toString());
                if(!map.isEmpty()){
                    message.what=0x11;
                    message.obj=map;
                }
                else { message.what=0x12;}
                // 发消息通知主线程更新UI
                handler.sendMessage(message);
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    HashMap<String, Object> map= (HashMap<String, Object>) msg.obj;
                    if (edit_password.getText().toString().equals(map.get("user_password").toString().trim())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("map",map);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    edit_password.setText("");}
                    break;
                case 0x12:
                    break;
            }
        }
    };
}