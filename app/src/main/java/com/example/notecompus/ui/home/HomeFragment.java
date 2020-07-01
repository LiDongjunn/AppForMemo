package com.example.notecompus.ui.home;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.notecompus.AddContentActivity;
import com.example.notecompus.Data.Data;
import com.example.notecompus.Data.DatabaseHelper;
import com.example.notecompus.Data.SynchronizeData;
import com.example.notecompus.Data.UserData;
import com.example.notecompus.Data.getDataByJsoup;
import com.example.notecompus.HomeActivity;
import com.example.notecompus.MainActivity;
import com.example.notecompus.myAdapter.AdapterShow;
import com.example.notecompus.myAdapter.AdapterTwoLine;
import com.example.notecompus.R;
import com.example.notecompus.myAdapter.AdapterDelete;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.notecompus.myAdapter.AdapterDelete.listDelete;


public class HomeFragment extends Fragment {



    public static UserData mUserData = new UserData();
    public static int isAllSelectHome = -1;
    public static String getSharedPreferenceAccount = "";
    public int searchPattern = -1;


    private View multiDeleteView;
    private ListView listView;
    private Button selectAll, delete, cancel;
    private SearchView mSearchView;
    private List<Integer> searchResultPos ;
    private HomeViewModel homeViewModel;
    private AdapterShow AdapterShow;
    private AdapterDelete AdapterDelete;
    private SynchronizeData mSynchronizeData = new SynchronizeData();
    private SQLiteDatabase mSQLiteDatabase;

    public HomeFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        SharedPreferences userAccount = getActivity().getSharedPreferences("userAccount", 0);
        getSharedPreferenceAccount = userAccount.getString("recentAccount", "");
        setHasOptionsMenu(true);
        OnClick onClick = new OnClick();

        listView = root.findViewById(R.id.listView);
        multiDeleteView = root.findViewById(R.id.multi_delete_view);
        selectAll = (Button) root.findViewById(R.id.select_all);
        delete = (Button) root.findViewById(R.id.delete);
        cancel = (Button) root.findViewById(R.id.cancel);
        mSearchView = (SearchView)root.findViewById(R.id.search_button);


        selectAll.setOnClickListener(onClick);
        delete.setOnClickListener(onClick);
        cancel.setOnClickListener(onClick);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchPattern == -1){
                    Intent intent = new Intent(getContext(), AddContentActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), AddContentActivity.class);
                    intent.putExtra("position", searchResultPos.get(position));
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchPattern==-1){
                    multiDeleteView.setVisibility(View.VISIBLE);
                    loadingMyAdapterListView();
                }
                return true;
            }
        });


        mSearchView.setQueryHint("Press Enter");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int count = mUserData.getUserContent().size();
                UserData search = new UserData();
                searchResultPos = new ArrayList<Integer>();
                for (int i = 0; i < count; i++ ){
                    if (mUserData.getUserContent().get(i).contains(query)||mUserData.getUserContentTitle().get(i).contains(query)){
                        search.mData.add(mUserData.mData.get(i));
                        searchResultPos.add(i);
                    }
                }
                searchPattern = 1;
                AdapterShow = new AdapterShow(getActivity(),search);
                listView.setAdapter(AdapterShow);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    searchPattern = -1;
                    loadingListView();
                }
                return false;
            }
        });
        getLocalData();
        return root;
    }


    private void loadingListView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUserData.mData.sort(new Comparator<Data>() {
                @Override
                public int compare(Data o1, Data o2) {
                    String i1 = o1.EditTime;
                    String i2 = o2.EditTime;
                    String j1 = o1.stick;
                    String j2 = o2.stick;
                    if (j2.length()>0||j1.length()>0){
                        return j2.compareTo(j1);
                    }else {
                        return i2.compareTo(i1);
                    }
                }
            });
        }
        AdapterShow = new AdapterShow(getContext(), mUserData);
        listView.setAdapter(AdapterShow);
    }

    private void loadingMyAdapterListView() {
        AdapterDelete = new AdapterDelete(getActivity(),mUserData);
        listView.setAdapter(AdapterDelete);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.select_all:
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

                case R.id.delete:
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
                    isAllSelectHome = -1;
                    multiDeleteView.setVisibility(View.INVISIBLE);
                    loadingListView();
                    saveData(mUserData.mData);
                    break;
                case  R.id.cancel:
                    isAllSelectHome = -1;
                    multiDeleteView.setVisibility(View.INVISIBLE);
                    loadingListView();
                    break;
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                multiDeleteView.setVisibility(View.VISIBLE);
                loadingMyAdapterListView();
                return true;
            case R.id.addContent:
                Intent AddContentActivity = new Intent(getActivity(), com.example.notecompus.AddContentActivity.class);
                startActivity(AddContentActivity);
                return true;
            case R.id.menu_upload:
                Log.e("R.id.upLoad",mUserData.getUserContent().toString());
                if (getDataByJsoup.getDataID().size()!=0){
                    mSynchronizeData.upLoad_to_html(mUserData.getUserContent());
                    Toast.makeText(getContext(),"连接到网络",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"未连接到网络",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_download:
                if (getDataByJsoup.getData().mData.size()!=0){
                    mUserData=getDataByJsoup.getData();
                    Log.e("R.id.downLoad",getDataByJsoup.getData().toString());
                    loadingListView();
                    saveData(mUserData.mData);
                    Toast.makeText(getContext(),"连接到网络",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(),"未能连接网络",Toast.LENGTH_SHORT).show();
                    Log.e("R.id.downLoad",getDataByJsoup.getData().toString());
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void getLocalData() {
        mUserData.mData.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        mSQLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.query(getSharedPreferenceAccount, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int count = cursor.getCount();
            for (int j = 0; j< count; j++){
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT));
                String editTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_EDIT_TIME));
                String remindTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_REMINDER_TIME));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_TITLE));
                String stick = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_CONTENT_STICK));
                mUserData.mData.add(new Data(title,content,editTime,remindTime,stick));
                cursor.moveToNext();
            }
        }
        Log.e("mUserData",mUserData.mData.toString());

        loadingListView();
    }

    ////****数据库保存数据****
    public void saveData(List<Data> data) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
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
//            Toast.makeText(getContext(),"插入成功："+String.valueOf(insetFlag),Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(getContext(),"插入失败",Toast.LENGTH_SHORT).show();
//        }
    }
}
