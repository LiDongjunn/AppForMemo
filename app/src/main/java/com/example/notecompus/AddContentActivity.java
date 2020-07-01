package com.example.notecompus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notecompus.Data.Data;
import com.example.notecompus.Data.DatabaseHelper;
import com.example.notecompus.datepicker.CustomDatePicker;
import com.example.notecompus.datepicker.DateFormatUtils;
import com.example.notecompus.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.notecompus.ui.home.HomeFragment.getSharedPreferenceAccount;
import static com.example.notecompus.ui.home.HomeFragment.mUserData;
import static com.example.notecompus.ui.theme.ThemeFragment.themeFlag;

public class AddContentActivity extends AppCompatActivity {
    private EditText addContentEditText,addTitleEditText;
    private TextView syncTime , alarmShow;
    private CustomDatePicker mTimerPicker;
    private String reminderTime = "";
    private String stick = "";


    private int position = -1;
    private SQLiteDatabase mSQLiteDatabase;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (themeFlag==1){
            setTheme(R.style.AppThemeNight);
        }else if (themeFlag ==2){
            setTheme(R.style.AppThemePaper);
        }else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        addTitleEditText = findViewById(R.id.addTitleEditText);
        addContentEditText = findViewById(R.id.addContentEditText);
        syncTime = (TextView) findViewById(R.id.sync_time);
        alarmShow = (TextView) findViewById(R.id.alarm_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initTimePicker();
        syncTime.setText(getTime());
        try {
            Intent getIntent = getIntent();
            position = getIntent.getIntExtra("position",-1);
            reminderTime = mUserData.mData.get(position).reminderTime;
            stick = mUserData.mData.get(position).stick;
            addTitleEditText.setText(mUserData.getUserContentTitle().get(position));
            addContentEditText.setText(mUserData.getUserContent().get(position));

            syncTime.setText(mUserData.getUserContentEditTime().get(position));
            if (!reminderTime.equals("")){
                alarmShow.setVisibility(View.VISIBLE);
                alarmShow.setText(reminderTime);
            }else {
                alarmShow.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }
        
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_content, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        String title = addTitleEditText.getText().toString();
        String content = addContentEditText.getText().toString();
        if (position == -1 && !addContentEditText.getText().toString().equals("")){
            mUserData.mData.add(new Data(title,content,getTime(),reminderTime));
        }else if (position != -1 && !addContentEditText.getText().toString().equals("")){
            mUserData.mData.set(position,new Data(title,content,getTime(),reminderTime));
        }else if (position != -1 && addContentEditText.getText().toString().equals("")){
            mUserData.mData.remove(position);
        }else {
            position=-1;
        }
        Intent MainActivity = new Intent(AddContentActivity.this,MainActivity.class);
        startActivity(MainActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String title = addTitleEditText.getText().toString();
                String content = addContentEditText.getText().toString();
                if (position == -1 && !addContentEditText.getText().toString().equals("")){
                    mUserData.mData.add(new Data(title,content,getTime(),reminderTime,stick));
                }else if (position != -1 && !addContentEditText.getText().toString().equals("")){
                    mUserData.mData.set(position,new Data(title,content,getTime(),reminderTime,stick));
                }else if (position != -1 && addContentEditText.getText().toString().equals("")){
                    mUserData.mData.remove(position);
                }else {
                    position=-1;
                }
                saveData(mUserData.mData);

                Intent MainActivity = new Intent(AddContentActivity.this,MainActivity.class);
                startActivity(MainActivity);
                return true;

            case R.id.action_setting_alarm:
                String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
                mTimerPicker.show(endTime);
                return true;
            case R.id.action_stick:
                if (stick.equals("")){
                    stick="1";
                }else {
                    stick="";
                }
                return true;
            case R.id.action_delete:
                if (position == -1){
                }else {
                    mUserData.mData.remove(position);
                    Intent MainActivity2 = new Intent(AddContentActivity.this,MainActivity.class);
                    startActivity(MainActivity2);
                }
                return true;
            case R.id.action_cancel_alarm:
                reminderTime="";
                alarmShow.setVisibility(View.INVISIBLE);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");// HH:mm:ss
        Date t = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(t);
        return  time;
    }

    private void initTimePicker() {
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis()-60*1000, true);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSelected(long timestamp) {
//                reminderTime = DateFormatUtils.long2Str(timestamp, true);
                SimpleDateFormat sp = new SimpleDateFormat("MM-dd HH:mm");
                reminderTime = sp.format(new Date(timestamp));
                alarmShow.setVisibility(View.VISIBLE);
                alarmShow.setText(reminderTime);
                startAlarm(timestamp);
            }
        }, beginTime, endTime);
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
    private void startAlarm(long time) {
        Log.e("startAlarm","startAlarm");
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg","你该打酱油了");
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        assert am != null;
        am.set(AlarmManager.RTC_WAKEUP,time,pi);
        Log.e("startAlarm1","startAlarm1");
    }

    public void saveData(List<Data> data) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        mSQLiteDatabase = databaseHelper.getReadableDatabase();
        mSQLiteDatabase.delete(getSharedPreferenceAccount,null,null);

        Cursor cursor = mSQLiteDatabase.query(getSharedPreferenceAccount, null, null, null, null, null, null);
        int count =cursor.getCount();
        long insetFlag = -1;
        ContentValues contentValues = new ContentValues();
        for (int i = 0 ; i<mUserData.mData.size();i++) {
            contentValues.put(DatabaseHelper.USER_CONTENT, data.get(i).Content);
            contentValues.put(DatabaseHelper.USER_CONTENT_EDIT_TIME, data.get(i).EditTime);
            contentValues.put(DatabaseHelper.USER_CONTENT_REMINDER_TIME, data.get(i).reminderTime);
            contentValues.put(DatabaseHelper.USER_CONTENT_TITLE, data.get(i).Title);
            contentValues.put(DatabaseHelper.USER_CONTENT_STICK, data.get(i).stick);
            insetFlag = mSQLiteDatabase.insert(getSharedPreferenceAccount, null, contentValues);
        }
//        if (insetFlag != -1){
//            Toast.makeText(this,"插入成功："+String.valueOf(insetFlag),Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this,"插入失败",Toast.LENGTH_SHORT).show();
//        }
    }

}
