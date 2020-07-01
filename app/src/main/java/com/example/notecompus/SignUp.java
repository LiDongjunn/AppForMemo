package com.example.notecompus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notecompus.Data.DatabaseHelper;
import com.example.notecompus.Data.UserData;

public class SignUp extends AppCompatActivity {
    public static String USER_ACCOUNT ;
    private Button buttonSignUp;
    private EditText SignUpAccount,SignUpPassword,SignUpPasswordAgain;

    public static final String USER_CONTENT = "userContent";
    public static final String USER_CONTENT_EDIT_TIME = "userContentEditTime";
    public static final String USER_CONTENT_REMINDER_TIME = "userContentReminderTime";
    public static final String USER_CONTENT_TITLE = "userContentTitle";
    public static final String USER_CONTENT_STICK = "userContentStick";


    public static final int resultCode = 10;
    private SQLiteDatabase mSQLiteDatabase,mCreateTable;
    public static UserData mUserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        OnClick onClick = new OnClick();

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        SignUpAccount = (EditText) findViewById(R.id.SignUpAccount);
        SignUpPassword = (EditText) findViewById(R.id.SignUpPassword);
        SignUpPasswordAgain = (EditText) findViewById(R.id.SignUpPasswordAgain);
        buttonSignUp.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSignUp:
                    String SignUpAccountString = SignUpAccount.getText().toString();
                    String SignUpPasswordString = SignUpPassword.getText().toString();
                    String SignUpPasswordAgainString = SignUpPasswordAgain.getText().toString();
                    if (!SignUpPasswordAgainString.equals(SignUpPasswordString)){
                        SignUpPassword.setText("");
                        SignUpPasswordAgain.setText("");
                        Toast.makeText(SignUp.this,"Password is different",Toast.LENGTH_SHORT).show();
                    }
                    else if (SignUpAccountString.length()<6||SignUpAccountString.length()>20){
                        SignUpAccount.setText("");
                        SignUpPassword.setText("");
                        SignUpPasswordAgain.setText("");
                        Toast.makeText(SignUp.this,"Account length range is 6~20",Toast.LENGTH_SHORT).show();
                    }
                    else if (SignUpPasswordString.length()<6||SignUpPasswordString.length()>20){
                        SignUpAccount.setText("");
                        SignUpPassword.setText("");
                        SignUpPasswordAgain.setText("");
                        Toast.makeText(SignUp.this,"Password length range is 6~20",Toast.LENGTH_SHORT).show();
                    }
                    else if (SignUpPasswordAgainString.contains(" ")){
                        SignUpAccount.setText("");
                        SignUpPassword.setText("");
                        SignUpPasswordAgain.setText("");
                        Toast.makeText(SignUp.this,"Account contains blank space",Toast.LENGTH_SHORT).show();
                    }else if (!(SignUpAccountString.charAt(0)>='a'&&SignUpAccountString.charAt(0)<='z'||SignUpAccountString.charAt(0)>='A'&&SignUpAccountString.charAt(0)<='Z')){
                        SignUpAccount.setText("");
                        SignUpPassword.setText("");
                        SignUpPasswordAgain.setText("");
                        Toast.makeText(SignUp.this,"Account initial must contains a char(a-z or A-z)",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            mUserData = new UserData(SignUpAccountString,SignUpPasswordString);
                            createUser(mUserData);
                        }catch (Exception e){
                            Toast.makeText(SignUp.this,"Account or Password is legal",Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
            }
        }
    }
    private void createUser(UserData data) {
        SharedPreferences userAccountSharedPreferences = getSharedPreferences("userAccount",0);
        if (userAccountSharedPreferences.getAll().containsKey(data.getUserAccount())){
            SignUpAccount.setText("");
            SignUpPassword.setText("");
            SignUpPasswordAgain.setText("");
            Toast.makeText(SignUp.this,"This account has exist",Toast.LENGTH_SHORT).show();
        }else{
            //1.create sharedPreference
            SharedPreferences.Editor editor = userAccountSharedPreferences.edit();
            editor.putString(data.getUserAccount(),data.getUserPassword());
            editor.apply();
            //2.create sql database
            USER_ACCOUNT=data.getUserAccount();
            DatabaseHelper databaseHelper = new DatabaseHelper(SignUp.this);
            mSQLiteDatabase = databaseHelper.getReadableDatabase();
            mSQLiteDatabase.execSQL("CREATE TABLE " + USER_ACCOUNT + "(" +
                    "   " + USER_CONTENT + "           TEXT NOT NULL," +
                    "   " + USER_CONTENT_EDIT_TIME + " TEXT NOT NULL," +
                    "   " + USER_CONTENT_REMINDER_TIME + " TEXT ," +
                    "   " + USER_CONTENT_TITLE + " TEXT ," +
                    "   " + USER_CONTENT_STICK + " TEXT " +
                    ");");
            putToLogin(data);
        }
    }

    private void putToLogin(UserData data) {
        Intent LoginActivity = new Intent(SignUp.this, LoginActivity.class);
        //新建bundle对象，通过Bundle对象传递数据
        Bundle bundle = new Bundle();
        bundle.putCharSequence("SignUpAccountString", data.getUserAccount());
        bundle.putCharSequence("SignUpPasswordString", data.getUserPassword());
        //intent 绑定bundle并输出
        LoginActivity.putExtras(bundle);
        SignUp.this.setResult(resultCode, LoginActivity);
        //结束此activity
        SignUp.this.finish();
    }
}
