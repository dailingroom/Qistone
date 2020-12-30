package com.example.qistone.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qistone.Dialog.PictureDialog;
import com.example.qistone.Login.LoginActivity;
import com.example.qistone.R;
import com.example.qistone.Utils.DBUtils;
import com.mob.MobSDK;

import java.sql.SQLException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    String APPKEY = "31aba01891494";
    String APPSECRET = "59c79eb06621d9f57be5c57f24ca1c68";
    //用户名
    private  EditText account;
    //密码输入框
    private EditText password;
    //再次输入密码框
    private EditText re_password;
    // 手机号输入框
    private EditText inputPhoneEt;
    // 验证码输入框
    private EditText inputCodeEt;
    //年龄输入框
    private EditText old;
    // 获取验证码按钮
    private Button requestCodeBtn;
    // 注册按钮
    private Button resigner_button;
   // 选择头像
    private CircleImageView head_image;
    //倒计时显示   可以手动更改。
    int i = 30;
   //用于记录用户选择的头像
    byte[] user_drawable=null;
    Bitmap user_bitmap;
    // 返回按钮
    private ImageView resigner_back;
    //显示用户的框
    final PictureDialog pictureDialog=new PictureDialog(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        inputPhoneEt = (EditText) findViewById(R.id.phone_Edit);//手机号输入
        inputCodeEt = (EditText) findViewById(R.id.core_edit);//验证码输入
        account=(EditText) findViewById(R.id.resigner_accountEdit);//用户名输入
        password=(EditText) findViewById(R.id.resigner_password_Edit);//密码输入
        re_password=(EditText)findViewById(R.id.resigner_password_Edit1);//再次输入密码输入
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);//验证码输入按钮
        old=(EditText)findViewById(R.id.old_Edit);//年龄输入
        resigner_back=(ImageView)findViewById(R.id.resigner_back);//返回登录
        resigner_button = (Button) findViewById(R.id.resigner_button);
        head_image=(CircleImageView )findViewById(R.id.head_image);//选择用户头像
        requestCodeBtn.setOnClickListener(this);//获取验证码按钮
        resigner_button.setOnClickListener(this);//注册按钮
        head_image.setOnClickListener(this);//头像选择
        resigner_back.setOnClickListener(this);//返回按钮
        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();  //取出咱们输入的手机号
        switch (v.getId()) {
            case R.id.login_request_code_btn:
                // 1. 判断手机号是不是11位并且看格式是否合理
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.resigner_button:
                if (inputCodeEt.getText().toString().equals("")&&inputCodeEt.getText().toString().equals("")&&account.getText().toString().equals("")&&
                password.getText().toString().equals("")&&re_password.getText().toString().equals("")){
                    Toast.makeText(this,"请完善好信息再注册！",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.getText().toString().equals(re_password.getText())) {
                        //将收到的验证码和手机号提交再次核对
                        if (!judgePhoneNums(phoneNums)) {
                            return;
                        }
                        is_User_No();
                        // 2. 验证
                        SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
                                .getText().toString());
                    }else {
                        Toast.makeText(this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                    }
                }
                    break;
            case R.id.head_image:
                pictureDialog.builder().show();
                    break;
            case R.id.resigner_back:
                finish();
        }

    }

    /**
     *Handle主线程和子线程之间的通信
     */

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                        Toast.makeText(getApplicationContext(), "该账号已经注册过了，注册失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 0x12:
                   break;
            }
            if (msg.what == -9) {
                requestCodeBtn.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        if (user_drawable==null){
                            //使用默认头像
                            Resources res = getResources();
                            Bitmap    bmp = BitmapFactory.decodeResource(res,R.drawable.user_name);
                            user_drawable=DBUtils.img(bmp);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DBUtils.insertDate(account.getText().toString(), inputPhoneEt.getText().toString(),
                                            password.getText().toString(),"",
                                            old.getText().toString(), "",user_drawable);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Toast.makeText(getApplicationContext(), "注册成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userName",inputPhoneEt.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"验证码不正确",Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(final String phoneNums) {

            if (isMatchLength(phoneNums, 11)
                    && isMobileNO(phoneNums)) {
            return true;
            }
            Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
            return false;
    }
    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
    private void is_User_No(){//判断用户是否存在
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        Message message = handler.obtainMessage();
                        HashMap<String, Object> map = DBUtils.getInfoByName(inputPhoneEt.getText().toString());
                        //Log.e("?????????",map.toString());
                        if(!map.isEmpty()){
                            message.what=0x11;
                        }
                        else { message.what=0x12;}

                        // 发消息通知主线程更新UI
                       // Log.e("????", String.valueOf(message.what));
                        handler.sendMessage(message);
                    }
                }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        pictureDialog.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    /**
     * 通过这个 activity 启动的其他 Activity 返回的结果在这个方法进行处理
     * 我们在这里对拍照、相册选择图片、裁剪图片的返回结果进行处理
     * @param requestCode 返回码，用于确定是哪个 Activity 返回的数据
     * @param resultCode 返回结果，一般如果操作成功返回的是 RESULT_OK
     * @param data 返回对应 activity 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = pictureDialog.onActivityResult(requestCode, resultCode, data, RESULT_OK);
        if (bitmap != null) {
            user_bitmap = bitmap;
            user_drawable = DBUtils.img(bitmap);
            head_image.setImageBitmap(bitmap);
        }
    }
}