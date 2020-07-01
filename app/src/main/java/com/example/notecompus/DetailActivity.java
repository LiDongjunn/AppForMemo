package com.example.notecompus;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notecompus.datepicker.CustomDatePicker;
import com.example.notecompus.datepicker.DateFormatUtils;
import com.example.notecompus.receiver.AlarmReceiver;

public class DetailActivity extends Activity {

    private static EditText editText;
    private ImageButton exit;
    private ImageButton delete;
    private ImageButton alarm;

    private TextView reminderTimeTextTextView;

    private String reminderTime = "";
    private CustomDatePicker mTimerPicker;
    private static String Id;

    public static final int resultCode = 2;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnClick onClick = new OnClick();
        setContentView(R.layout.activity_detail);
        //变量绑定
        exit = (ImageButton) findViewById(R.id.exit);
        delete = (ImageButton) findViewById(R.id.delete);
        editText = (EditText) findViewById(R.id.EditText);
        alarm = (ImageButton) findViewById(R.id.alarm);
        reminderTimeTextTextView = (TextView) findViewById(R.id.reminderTimeTextView);

        initTimePicker();
        //获取HomeActivity的数据
        Intent intent = getIntent();
        String touchContent = intent.getStringExtra("touchContent");
        reminderTime = intent.getStringExtra("reminderTime");
        Id = intent.getStringExtra("Id");
        if (reminderTime.equals("")) {
            reminderTimeTextTextView.setText("");
        } else {
            reminderTimeTextTextView.setText("提醒:" + reminderTime);
        }
        editText.setText(touchContent);

        //按键逻辑
        exit.setOnClickListener(onClick);
        delete.setOnClickListener(onClick);
        alarm.setOnClickListener(onClick);

    }

    private void initTimePicker() {
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis()-60*1000, true);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSelected(long timestamp) {
                reminderTime = DateFormatUtils.long2Str(timestamp, true);
                Toast.makeText(DetailActivity.this,reminderTime,Toast.LENGTH_LONG).show();
                reminderTimeTextTextView.setText("提醒："+reminderTime);
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

    @Override
    public void onBackPressed() {
        //获取编辑数据
        String s= editText.getText().toString();
        if(s.equals("")){
            delete();
        }
        else {
            edit();
        }
    }
    //删除
    void delete(){
        //创建Intent
        Intent intent= new Intent(DetailActivity.this,HomeActivity.class);

        Bundle bundle=new Bundle();
        bundle.putCharSequence("IdDelete",Id);
        //intent 绑定bundle并输出Id
        intent.putExtras(bundle);
        //开始回调
        DetailActivity.this.setResult(resultCode,intent);
        //结束此activity
        DetailActivity.this.finish();
    }
    //修改
    void edit(){
        //创建Intent
        Intent intent= new Intent(DetailActivity.this,HomeActivity.class);
        //获取编辑数据
        String s= editText.getText().toString();
        //新建bundle对象，通过Bundle对象传递数据
        Bundle bundle=new Bundle();
        bundle.putCharSequence("userEditStrings",s);
        bundle.putCharSequence("userSetReminderTime",reminderTime);
        bundle.putCharSequence("Id",Id);
        //intent 绑定bundle并输出
        intent.putExtras(bundle);
        //开始回调
        DetailActivity.this.setResult(resultCode,intent);
        //结束此activity
        DetailActivity.this.finish();
    }
    private void startAlarm(long time) {
        Log.e("startAlarm","startAlarm");
//创建Intent对象，action为ELITOR_CLOCK，附加信息为字符串“你该打酱油了”
        Intent intent = new Intent(DetailActivity.this,AlarmReceiver.class);
        intent.putExtra("msg","你该打酱油了");

//定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
//也就是发送了action 为"ELITOR_CLOCK"的intent
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);

//AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

//设置闹钟从当前时间开始，每隔5s执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
// 5秒后通过PendingIntent pi对象发送广播
        am.set(AlarmManager.RTC_WAKEUP,time,pi);
        Log.e("startAlarm1","startAlarm1");
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.exit:
                    //获取编辑数据
                    String s= editText.getText().toString();
                    if(s.equals("")){
                        delete();
                    }
                    else {
                        edit();
                    }
                    break;
                case R.id.delete:
                    //创建Intent
                    Intent intent= new Intent(DetailActivity.this,HomeActivity.class);

                    Bundle bundle=new Bundle();
                    bundle.putCharSequence("IdDelete",Id);
                    //intent 绑定bundle并输出Id
                    intent.putExtras(bundle);
                    //开始回调
                    DetailActivity.this.setResult(resultCode,intent);
                    //结束此activity
                    DetailActivity.this.finish();
                    break;
                case R.id.alarm:
                    String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
                    mTimerPicker.show(endTime);
                    break;

            }
        }
    }
}
