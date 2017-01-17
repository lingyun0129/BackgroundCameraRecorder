package com.sth.camerabackgroundrecorder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.R;
import com.sth.camerabackgroundrecorder.util.MySharedPreference;

public class InputPasswordDialogActivity extends Activity {
private EditText et_password;
    private Button btn_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password_dialog);
        et_password=(EditText)findViewById(R.id.et_pwd);
        btn_signin=(Button)findViewById(R.id.btn_sigin);
        final String password= new MySharedPreference(this).getString(MySharedPreference.KEY_PASSWORD,"");
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.equals(et_password.getText().toString())){
                    Intent intent=new Intent(InputPasswordDialogActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(InputPasswordDialogActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                    et_password.setText("");
                }
            }
        });
    }
}
