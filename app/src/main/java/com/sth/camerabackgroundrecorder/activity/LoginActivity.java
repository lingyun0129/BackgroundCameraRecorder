package com.sth.camerabackgroundrecorder.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.R;
import com.sth.camerabackgroundrecorder.net.HttpHelper;
import com.sth.camerabackgroundrecorder.util.DeviceUuidFactory;
import com.sth.camerabackgroundrecorder.util.MySharedPreference;
import com.sth.camerabackgroundrecorder.util.Tools;

import java.util.List;

public class LoginActivity extends Activity {
    private EditText regCode;
    private Button signIn,register;
    private ProgressDialog progressDialog;
    private String imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFinishOnTouchOutside(false);
        //get imei
        imei=Tools.getIMEI(this);
        setTitle(R.string.sigin_in_title);
        regCode=(EditText)findViewById(R.id.et_reg_code);
        final String uuid = new DeviceUuidFactory(LoginActivity.this).getDeviceUuid().toString();//设备uuid
        //signin
        signIn =(Button)findViewById(R.id.btn_sigin_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                String registrationCode = regCode.getEditableText().toString().trim();//注册码
                //String uuid = new DeviceUuidFactory(LoginActivity.this).getDeviceUuid().toString();//设备uuid
                Log.i("cai", "uuid=" + uuid);
                if (!Tools.detectionNetwork(LoginActivity.this) || registrationCode == null || registrationCode.equals("")) {
                    Toast.makeText(LoginActivity.this, "注册码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("正在登录...");
                progressDialog.show();
                // signIn(MD5.getMD5String(registrationCode + uuid));
                signIn(registrationCode, uuid);*/
                if (imei!=null){
                    Log.i("cai","IMEI="+imei);
                    String inputCode = regCode.getEditableText().toString().trim();//用户输入的注册码
                    if(inputCode == null || inputCode.equals("")){
                        Toast.makeText(LoginActivity.this, "注册码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //由IMEI生成注册码
                    if (inputCode.equals(generateRegisterCode(imei))){
                        Log.i("cai","注册码匹配");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        new MySharedPreference(LoginActivity.this).putBooleanAndCommit(MySharedPreference.KEY_REGISTERED, true);
                        finish();

                    }else {
                        Toast.makeText(LoginActivity.this, "注册码错误，登录失败！", Toast.LENGTH_SHORT).show();
                        regCode.setText("");
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"无效IMEI",Toast.LENGTH_LONG).show();
                }
            }
        });
        //register
        register=(Button)findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送注册短信到指定号码
                if (imei!=null) {
  /*                  sendSms("15118128311", imei);
                    register.setEnabled(false);
                    new MyCountDownTimer(60 * 1000, 1000).start();*/
                    send(imei);
                }else {
                    Toast.makeText(LoginActivity.this,"无效IMEI",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            register.setEnabled(true);
            register.setText("重新获取注册码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            register.setText(millisUntilFinished / 1000 + "秒");  //设置倒计时时间
        }
    }

    /**
     * 调用系统发送短信程序
     *
     * @param message
     */
    private void send(String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }


    /**
     * 后台发送短信到指定号码
     * @param number
     * @param msg
     */
    private void sendSms(String number,String msg){
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, testSms.class), 0);

        SmsManager smsManager = SmsManager.getDefault();

        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            smsManager.sendTextMessage(number, null, text, null, null);
        }
    }

    /**
     * 由imei生成注册码
     * @param imei
     * @return
     */
    private String generateRegisterCode(String imei){
        StringBuffer result = new StringBuffer();
        for (int i = 12; i > 0; i-=2) {
            char c = imei.charAt(i);//从第13位开始向前取，共取6位数字作为验证码
            Log.i("cai", "转换前的字符" + c);
            result.append(c);
        }
        Log.i("cai", "转换后的验证码为：" + result.toString());
        return result.toString();
    }
    private void signIn(final String registrationCode,final String uuid) {
        new Thread(){
            @Override
            public void run() {
                boolean result=HttpHelper.LoginByGet(registrationCode,uuid);
                if(result){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            new MySharedPreference(LoginActivity.this).putBooleanAndCommit(MySharedPreference.KEY_REGISTERED, true);
                        }
                    });

                }else{
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "注册码错误，登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }.start();
    }
}
