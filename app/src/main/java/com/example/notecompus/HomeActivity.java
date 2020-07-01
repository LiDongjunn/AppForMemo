package com.example.notecompus;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.notecompus.Data.Data;
import com.example.notecompus.Data.DatabaseHelper;
import com.example.notecompus.Data.SynchronizeData;
import com.example.notecompus.Data.UserData;
import com.example.notecompus.Data.getDataByJsoup;
import com.example.notecompus.myAdapter.AdapterTwoLine;
import com.example.notecompus.myAdapter.MyAdapter;

import static com.example.notecompus.myAdapter.MyAdapter.listDelete;

public class HomeActivity extends ListActivity {
    //私有
    private static final int ADD_CODE = 1;
    private static final int DETAIL_CODE = 2;
    private String getSharedPreferenceAccount = "";
    private MyAdapter myAdapter ;
    private com.example.notecompus.myAdapter.AdapterTwoLine AdapterTwoLine ;
    private com.example.notecompus.Data.getDataByJsoup getDataByJsoup = new getDataByJsoup();
    private SynchronizeData mSynchronizeData = new SynchronizeData();
    private SQLiteDatabase mSQLiteDatabase;
    //ImageButton
    private ImageButton add,plan,selectDelete,selectAll,repeal,downLoad,upLoad;
    //公有
    public static int isAllSelectHome = -1;
    public static UserData mUserData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        OnClick onClick = new OnClick();
        OnItemLongClick onItemLongClick = new OnItemLongClick();

        //变量绑定
        add = (ImageButton) findViewById(R.id.add);
        plan = (ImageButton) findViewById(R.id.plan);
        selectAll = (ImageButton) findViewById(R.id.selectAll);
        repeal = (ImageButton) findViewById(R.id.repeal);
        selectDelete = (ImageButton) findViewById(R.id.selectDelete);
        downLoad = (ImageButton) findViewById(R.id.downLoad);
        upLoad = (ImageButton) findViewById(R.id.upLoad);


        //打开本地数据库并显示
        getLocalData();
        ////按键事件
        //主
        add.setOnClickListener(onClick);
        plan.setOnClickListener(onClick);
        downLoad.setOnClickListener(onClick);
        upLoad.setOnClickListener(onClick);
        //隐藏
        selectDelete.setOnClickListener(onClick);
        selectAll.setOnClickListener(onClick);
        repeal.setOnClickListener(onClick);

        //ListView长按事件
        getListView().setOnItemLongClickListener(onItemLongClick);


    }




    //ListView点击事件
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
        String Id = String.valueOf(position);
        intent.putExtra("touchContent",mUserData.getUserContent().get(position));
        intent.putExtra("reminderTime",mUserData.mData.get(position).reminderTime);
        intent.putExtra("Id",Id);
        startActivityForResult(intent,DETAIL_CODE);
    }
    //ListView长点击事件
    private class OnItemLongClick implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            selectDelete.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            repeal.setVisibility(View.VISIBLE);
            downLoad.setVisibility(View.INVISIBLE);
            upLoad.setVisibility(View.INVISIBLE);
            //多选显示
            loadingMyAdapterListView();
            return true;
        }
    }
    //A --> B --> A返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //收到编辑界面的字符串数据，并将数据存储和输出到ListView
        if (requestCode ==ADD_CODE && resultCode == ADD_CODE){
            //通过Intent获取bundle对象
            assert data != null;
            Bundle bundle=data.getExtras();
            //取出数据
            String s=data.getStringExtra("userEditStrings");
            mUserData.mData.add(new Data(s,getTime()));
        }
        //是否删除或改动
        if (requestCode ==DETAIL_CODE && resultCode == DETAIL_CODE){
            //通过Intent获取bundle对象
            assert data != null;
            Bundle bundle=data.getExtras();
            //改动数据
            if (data.getStringExtra("IdDelete") == null){
                String s = data.getStringExtra("userEditStrings");
                String userSetReminderTime = data.getStringExtra("userSetReminderTime");
                String Id = data.getStringExtra("Id");
                assert Id != null;
                int id = Integer.parseInt(Id);
                mUserData.mData.set(id,new Data(s,getTime(),userSetReminderTime));
            }
            //删除数据
            else{
                String IdDelete = data.getStringExtra("IdDelete");
                assert IdDelete != null;
                int idDelete = Integer.parseInt(IdDelete);
                mUserData.mData.remove(idDelete);
            }
        }
        Log.e("mUserData",mUserData.mData.toString());
        loadingListView();
        ////****数据库保存数据****
        saveData(mUserData.mData);
    }



    ////****数据库保存数据****
    private void saveData(List<Data> data) {
        DatabaseHelper databaseHelper = new DatabaseHelper(HomeActivity.this);
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
            insetFlag = mSQLiteDatabase.insert(getSharedPreferenceAccount, null, contentValues);
        }
        if (insetFlag != -1){
            Toast.makeText(HomeActivity.this,"插入成功："+String.valueOf(insetFlag),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(HomeActivity.this,"插入失败",Toast.LENGTH_SHORT).show();
        }
    }
    ////打开数据库
    private void getLocalData() {
        getAccount();
        mUserData.mData.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(HomeActivity.this);
        mSQLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.query(getSharedPreferenceAccount, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int count = cursor.getCount();
            for (int j = 0; j< count; j++){
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT));
                String editTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_EDIT_TIME));
                String remindTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_REMINDER_TIME));
                mUserData.mData.add(new Data(content,editTime,remindTime));
                cursor.moveToNext();
            }
        }
        Log.e("mUserData",mUserData.mData.toString());

        loadingListView();
    }

    private void getAccount() {
        Intent getIntent = getIntent();
        getSharedPreferenceAccount = getIntent.getStringExtra("getSharedPreferenceAccount");
    }

    private void loadingListView(){
        AdapterTwoLine = new AdapterTwoLine(HomeActivity.this,mUserData.getUserContent(),mUserData.getUserContentEditTime(),mUserData.getUserContentReminderTime());
        setListAdapter(AdapterTwoLine);
    }
    private void loadingMyAdapterListView(){
        myAdapter = new MyAdapter(HomeActivity.this,mUserData.getUserContent());
        setListAdapter(myAdapter);
    }
    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date t = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(t);
        return  time;
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add:
                    Intent intent = new Intent(HomeActivity.this,AddActivity.class);
                    startActivityForResult(intent,ADD_CODE);
                    break;
                case R.id.plan:
                    Intent intentPlan = new Intent(HomeActivity.this,PlanActivity.class);
                    startActivity(intentPlan);
                    break;
                case R.id.selectDelete:
                    selectDelete.setVisibility(View.INVISIBLE);
                    selectAll.setVisibility(View.INVISIBLE);
                    repeal.setVisibility(View.INVISIBLE);
                    downLoad.setVisibility(View.VISIBLE);
                    upLoad.setVisibility(View.VISIBLE);
                    int find = 0;
                    for (int i = 0; i< mUserData.getUserContent().size(); i++) {
                        for (int j = 0; j < listDelete.size(); j++) {
                            if (mUserData.mData.get(i).Content.equals(listDelete.get(j))) {
                                mUserData.mData.remove(i);
                                listDelete.remove(j);
                                find = 1;
                                break;
                            }

                        }
                        if (find == 1){
                            find = 0 ;
                            i=-1;
                        }
                    }
                    listDelete.clear();
                    loadingListView();
                    isAllSelectHome = -1;
                    saveData(mUserData.mData);
                    break;
                case R.id.repeal:
                    downLoad.setVisibility(View.VISIBLE);
                    upLoad.setVisibility(View.VISIBLE);
                    selectDelete.setVisibility(View.INVISIBLE);
                    selectAll.setVisibility(View.INVISIBLE);
                    repeal.setVisibility(View.INVISIBLE);
                    loadingListView();
                    isAllSelectHome = -1;
                    break;
                case R.id.selectAll:
                    if(isAllSelectHome == -1){
                        isAllSelectHome = 1;
                    }
                    else if (isAllSelectHome == 1){
                        isAllSelectHome = 0;
                    }
                    else {

                        isAllSelectHome = 1;
                    }
                    loadingMyAdapterListView();
                    break;
                case R.id.upLoad:
                    if (getDataByJsoup.getData().mData.size()!=0){
                        mUserData=getDataByJsoup.getData();
                        Log.e("R.id.downLoad",getDataByJsoup.getData().toString());
                        loadingListView();
                        saveData(mUserData.mData);
                        Toast.makeText(HomeActivity.this,"连接到网络",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(HomeActivity.this,"未能连接网络",Toast.LENGTH_SHORT).show();
                        Log.e("R.id.downLoad",getDataByJsoup.getData().toString());
                    }
                    break;
                case R.id.downLoad:
                    getLocalData();
                    Log.e("R.id.upLoad",mUserData.getUserContent().toString());
                    if (getDataByJsoup.getDataID().size()!=0){
                        mSynchronizeData.upLoad_to_html(mUserData.getUserContent());
                        Toast.makeText(HomeActivity.this,"连接到网络",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(HomeActivity.this,"未连接到网络",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

}
