package com.sth.camerabackgroundrecorder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sth.camerabackgroundrecorder.R;
import com.sth.camerabackgroundrecorder.util.CameraSupportedParameters;
import com.sth.camerabackgroundrecorder.util.MySharedPreference;
import com.sth.camerabackgroundrecorder.util.Tools;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.initAppPara(this);

        boolean registered=new MySharedPreference(this).getBoolean(MySharedPreference.KEY_REGISTERED,false);
        String password=new MySharedPreference(this).getString(MySharedPreference.KEY_PASSWORD,"");
        Intent intent=new Intent();
        if (registered){
            //如果没有设置密码，直接进入;否则进入输入密码对话窗口
            if (password.equals("")) {
                intent.setClass(this, MainActivity.class);
            }else
            {
                intent.setClass(this,InputPasswordDialogActivity.class);
            }
        }
        else{
            intent.setClass(this,LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
