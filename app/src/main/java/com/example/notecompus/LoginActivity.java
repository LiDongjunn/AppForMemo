package com.example.notecompus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.notecompus.animation.OnSwipeTouchListener;

public class LoginActivity extends AppCompatActivity {

    private static final int SIGN_UP_CODE = 10;
    private ImageView imageView;
    private TextView textView;
    private Button mButtonLogin, mButtonSignUp;
    private CheckBox rememberAccount, autoLogin;
    private String recentAccount;

    int count = 0;
    private EditText LoginAccount, LoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        OnClick onClick = new OnClick();

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        mButtonLogin = findViewById(R.id.buttonLogin);
        mButtonSignUp = findViewById(R.id.buttonSignUp);
        LoginAccount = (EditText) findViewById(R.id.LoginAccount);
        LoginPassword = (EditText) findViewById(R.id.LoginPassword);
        rememberAccount = findViewById(R.id.rememberAccount);
        autoLogin = findViewById(R.id.autoLogin);

        mButtonLogin.setOnClickListener(onClick);
        mButtonSignUp.setOnClickListener(onClick);

        try {
            SharedPreferences userAccount = getSharedPreferences("userAccount", 0);
            recentAccount = userAccount.getString("recentAccount", "");
            if (userAccount.getString("rememberAccount", "").equals("1")) {
                LoginAccount.setText(recentAccount);
                LoginPassword.setText(userAccount.getString(recentAccount, ""));
                rememberAccount.setChecked(true);
            } else {
                rememberAccount.setChecked(false);
            }
            if (userAccount.getString("autoLogin", "").equals("1")) {
                LoginAccount.setText(recentAccount);
                LoginPassword.setText(userAccount.getString(recentAccount, ""));
                checkThePassword();
                autoLogin.setChecked(true);
            } else {
                autoLogin.setChecked(false);
            }

        } catch (Exception e) {

        }


        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_CODE && resultCode == SIGN_UP_CODE) {
            //通过Intent获取bundle对象
            assert data != null;
            //取出数据
            String LoginAccountString = data.getStringExtra("SignUpAccountString");
            String LoginPasswordString = data.getStringExtra("SignUpPasswordString");
            LoginAccount.setText(LoginAccountString);
            LoginPassword.setText(LoginPasswordString);
        }

    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSignUp:
                    Intent SignUpActivity = new Intent(LoginActivity.this, SignUp.class);
                    startActivityForResult(SignUpActivity, SIGN_UP_CODE);
                    break;
                case R.id.buttonLogin:
                    checkThePassword();
                    break;
            }
        }
    }

    private void checkThePassword() {
        SharedPreferences userAccount = getSharedPreferences("userAccount", 0);
        if (userAccount.getAll().containsKey(LoginAccount.getText().toString())) {
            if (userAccount.getAll().containsValue(LoginPassword.getText().toString())) {
                SharedPreferences.Editor editor = userAccount.edit();
                editor.putString("recentAccount", LoginAccount.getText().toString());
                if (rememberAccount.isChecked()) {
                    editor.putString("rememberAccount", "1");
                } else {
                    editor.putString("rememberAccount", "0");
                }
                if (autoLogin.isChecked()) {
                    editor.putString("autoLogin", "1");
                } else {
                    editor.putString("autoLogin", "0");
                }
                editor.apply();

//                Intent HomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
//                HomeActivity.putExtra("getSharedPreferenceAccount", LoginAccount.getText().toString());
//                startActivity(HomeActivity);
                Intent MainActivity = new Intent(LoginActivity.this, com.example.notecompus.MainActivity.class);
                MainActivity.putExtra("getSharedPreferenceAccount", LoginAccount.getText().toString());
                startActivity(MainActivity);
            } else {
                LoginAccount.setText("");
                LoginPassword.setText("");
                Toast.makeText(LoginActivity.this, "The password is error", Toast.LENGTH_SHORT).show();
            }

        } else {
            LoginAccount.setText("");
            LoginPassword.setText("");
            Toast.makeText(LoginActivity.this, "The account hasn't exist", Toast.LENGTH_SHORT).show();
        }
    }

}
