package com.example.qistone.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.qistone.R;
/*
* 分享菜单的实践实现，主要分享有分享到微信、微博、QQ、QQ空间，利用SDK实现，即监控dialog_share布局，实现dialog_share布局上的组件点击事件
* */
public class ShareDiaog implements View.OnClickListener {
    private Context context;
    private AlertDialog alertDialog;
    private LinearLayout ll_share_wechat;
    private LinearLayout ll_share_webo;
    private LinearLayout ll_share_qq;
    private LinearLayout ll_share_qzone;
    private RelativeLayout rl_menu_cancle;
    public ShareDiaog(Context context) {
        this.context = context;
    }
    public ShareDiaog builder() {
        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.bottom_activity_style)).create();
        alertDialog.show();
        Window win = alertDialog.getWindow();
        win.setWindowAnimations(R.style.bottom_activity_style);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        win.setAttributes(lp);
        win.setContentView(R.layout.dialog_share);
        rl_menu_cancle=win.findViewById(R.id.rl_menu_cancle);
        ll_share_wechat=win.findViewById(R.id.ll_share_wechat);
        ll_share_webo=win.findViewById(R.id.ll_share_webo);
        ll_share_qq=win.findViewById(R.id.ll_share_qq);
        ll_share_qzone=win.findViewById(R.id.ll_share_qzone);
        rl_menu_cancle.setOnClickListener(this);
        ll_share_wechat.setOnClickListener(this);
        ll_share_webo.setOnClickListener(this);
        ll_share_qq.setOnClickListener(this);
        ll_share_qzone.setOnClickListener(this);
        return this;
    }
    public void show(){
        alertDialog.show();
    }
    public void cancle(){
        alertDialog.cancel();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_menu_cancle:
                cancle();
                break;
            case R.id.ll_share_wechat:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareWechat();
                break;
            case R.id.ll_share_webo:
                cancle();
                if(shareClickListener!=null)shareClickListener.sharewebo();
                break;
            case R.id.ll_share_qq:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareQQ();
                break;
            case R.id.ll_share_qzone:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareQzone();
                break;
        }
    }
    public ShareClickListener shareClickListener;
    public interface ShareClickListener{
        void shareWechat();
        void sharewebo();
        void shareQQ();
        void shareQzone();
    }
    public void setShareClickListener(ShareClickListener shareClickListener){
        this.shareClickListener=shareClickListener;
    }
}
