package com.example.notecompus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notecompus.myAdapter.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.notecompus.myAdapter.MyAdapter.listDelete;

public class PlanActivity extends ListActivity{
    //私有
    private static final int ADD_CODE = 1;
    private static final int DETAIL_CODE = 2;
    private MyAdapter myAdapter ;
    private ArrayAdapter<String> adapter;
    private ImageButton plan;
    private ImageButton note;
    private ImageButton selectDelete;
    private ImageButton selectAll;
    private ImageButton repeal;

    private List<String> userPlan = new ArrayList<String>();
    //公有
    public static int isAllSelectPlan = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnClick onClick = new OnClick();
        OnItemLongClick onItemLongClick = new OnItemLongClick();
        setContentView(R.layout.activity_plan);
        //绑定按键
        plan = (ImageButton) findViewById(R.id.plan);
        note = (ImageButton) findViewById(R.id.note);
        selectAll = (ImageButton) findViewById(R.id.selectAll);
        repeal = (ImageButton) findViewById(R.id.repeal);
        selectDelete = (ImageButton) findViewById(R.id.selectDelete);

        openData();
        //点击事件
        //未隐藏
        plan.setOnClickListener(onClick);
        note.setOnClickListener(onClick);

        //隐藏
        selectDelete.setOnClickListener(onClick);
        selectAll.setOnClickListener(onClick);
        repeal.setOnClickListener(onClick);
        //长按事件
        getListView().setOnItemLongClickListener(onItemLongClick);

        Log.e("userplan",userPlan.toString());
    }
    //ListView点击事件
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(PlanActivity.this,DetailActivity.class);
        String Id = String.valueOf(position);
        intent.putExtra("touchContent",userPlan.get(position));
        intent.putExtra("Id",Id);
        startActivityForResult(intent,DETAIL_CODE);
    }
    //ListView长点击事件
    private class OnItemLongClick implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            selectDelete.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            repeal.setVisibility(View.VISIBLE);
            //多选显示
            myAdapter = new MyAdapter(PlanActivity.this, userPlan);
            setListAdapter(myAdapter);
            return true;
        }
    }
    //A --> B --> A返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //收到编辑界面的字符串数据，并将数据存储和输出到ListView
        if (requestCode == ADD_CODE && resultCode == ADD_CODE) {
            //通过Intent获取bundle对象
            assert data != null;
            Bundle bundle = data.getExtras();
            //取出数据
            String s = data.getStringExtra("userEditStrings");
            userPlan.add(s);
            //adapter 动态添加listview
            loadingListView();
            Toast.makeText(PlanActivity.this, "Add Success", Toast.LENGTH_LONG).show();
        }
        if (requestCode ==DETAIL_CODE && resultCode == DETAIL_CODE){
            //通过Intent获取bundle对象
            assert data != null;
            Bundle bundle=data.getExtras();
            //改动数据
            if (data.getStringExtra("IdDelete") == null){
                String s = data.getStringExtra("userEditStrings");
                String Id = data.getStringExtra("Id");
                assert Id != null;
                int id = Integer.parseInt(Id);
                userPlan.remove(id);
                userPlan.add(id,s);
            }
            //删除数据
            else{
                String IdDelete = data.getStringExtra("IdDelete");
                assert IdDelete != null;
                int idDelete = Integer.parseInt(IdDelete);
                userPlan.remove(idDelete);

            }
            //adapter 动态添加listview
            loadingListView();

            Toast.makeText(PlanActivity.this,"Edit Success",Toast.LENGTH_LONG).show();
        }
        ////****数据库保存数据****
        saveData();
    }
    private void loadingListView(){
        adapter = new ArrayAdapter<String>(PlanActivity.this,android.R.layout.simple_list_item_single_choice,userPlan);
        setListAdapter(adapter);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.plan:
                    Intent intent = new Intent(PlanActivity.this,AddActivity.class);
                    startActivityForResult(intent,ADD_CODE);
                    break;
                case R.id.note:
                    Intent intentPlan = new Intent(PlanActivity.this,HomeActivity.class);
                    startActivity(intentPlan);
                    break;
                case R.id.selectDelete:
                    selectDelete.setVisibility(View.INVISIBLE);
                    selectAll.setVisibility(View.INVISIBLE);
                    repeal.setVisibility(View.INVISIBLE);
                    int find = 0;
                    for (int i = 0; i< userPlan.size(); i++) {
                        for (int j = 0; j < listDelete.size(); j++) {
                            if (userPlan.get(i).equals(listDelete.get(j))) {
                                userPlan.remove(i);
                                listDelete.remove(j);
                                Log.i("iiiiiiiiiiiiiiii",String.valueOf(i));
                                Log.i("jjjjjjjjjjjjjjjj",String.valueOf(j));
                                Log.i("listDelete",String.valueOf(listDelete.size()));
                                Log.i("userText",String.valueOf(userPlan.size()));
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
                    saveData();
                    isAllSelectPlan = 1;
                    break;
                case R.id.repeal:
                    selectDelete.setVisibility(View.INVISIBLE);
                    selectAll.setVisibility(View.INVISIBLE);
                    repeal.setVisibility(View.INVISIBLE);
                    loadingListView();
                    isAllSelectPlan = -1;
                    break;
                case R.id.selectAll:
                    if(isAllSelectPlan == -1){
                        isAllSelectPlan = 1;
                    }
                    else if (isAllSelectPlan == 1){
                        isAllSelectPlan = 0;
                    }
                    else if (isAllSelectPlan == 0){
                        isAllSelectPlan = 1;
                    }
                    Log.i("---------------------",String.valueOf(isAllSelectPlan));
                    myAdapter = new MyAdapter(PlanActivity.this,userPlan);
                    setListAdapter(myAdapter);
                    break;
            }
        }
    }
    private void saveData(){

        //创建userTextData 对象
        SharedPreferences userPlanData = getSharedPreferences("userPlanData",MODE_PRIVATE);
        //创建Editor对象 用于写入数据
        SharedPreferences.Editor editor = userPlanData.edit();
        // 写入数据
        editor.putString("LONG",String.valueOf(userPlan.size()));
        for (int i = 0; i < userPlan.size();i++){
            String s = String.valueOf(i);
            editor.putString(s,userPlan.get(i));
//            Toast.makeText(HomeActivity.this,"Keep success"+s,Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }
    private void openData(){
        try {
            //打开数据库
            SharedPreferences sp = getSharedPreferences("userPlanData",MODE_PRIVATE);

            String LONG = sp.getString("LONG","0");
            for(int i = 0; i < Integer.valueOf(LONG);i++)
            {
                userPlan.add(sp.getString(String.valueOf(i),"*****无*****"));
            }
            //将userText放入ListView
            loadingListView();
        }catch (Exception e){
            Toast.makeText(PlanActivity.this,"Get local data failed",Toast.LENGTH_SHORT).show();
        }
    }
}
