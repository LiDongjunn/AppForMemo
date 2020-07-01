package com.example.notecompus.ui.theme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.notecompus.AddContentActivity;
import com.example.notecompus.Data.Data;
import com.example.notecompus.Data.UserData;
import com.example.notecompus.MainActivity;
import com.example.notecompus.R;
import com.example.notecompus.myAdapter.AdapterDelete;
import com.example.notecompus.myAdapter.AdapterShow;
import com.example.notecompus.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.notecompus.myAdapter.AdapterDelete.listDelete;


public class ThemeFragment extends Fragment {



    public static UserData mUserData = new UserData();
    public static int isAllSelectHome = -1;
    public static int themeFlag = 0;
    public int searchPattern = -1;

    private View multiDeleteView;
    private ListView listView;
    private Button theme_night, theme_light, theme_paper;
    private SearchView mSearchView;
    private List<Integer> searchResultPos ;
    private HomeViewModel homeViewModel;
    private com.example.notecompus.myAdapter.AdapterShow AdapterShow;
    private AdapterDelete AdapterDelete;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_theme, container, false);

        OnClick onClick = new OnClick();
        theme_night = (Button) root.findViewById(R.id.theme_night);
        theme_night.setOnClickListener(onClick);

        theme_light = (Button) root.findViewById(R.id.theme_light);
        theme_light.setOnClickListener(onClick);

        theme_paper = (Button) root.findViewById(R.id.theme_paper);
        theme_paper.setOnClickListener(onClick);

        if (themeFlag ==1){
            theme_night.setText("Using");
            theme_light.setText("Use");
            theme_paper.setText("Use");
        }else if (themeFlag ==2){
            theme_paper.setText("Using");
            theme_night.setText("Use");
            theme_light.setText("Use");
        }else {
            theme_light.setText("Using");
            theme_night.setText("Use");
            theme_paper.setText("Use");
        }


        return root;
    }



    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.theme_night:

                    themeFlag = 1;
                    Intent MainActivity = new Intent(getActivity(),MainActivity.class);
                    startActivity(MainActivity);
                    break;
                case R.id.theme_light:

                    themeFlag = 0;
                    Intent MainActivity1 = new Intent(getActivity(),MainActivity.class);
                    startActivity(MainActivity1);
                    break;
                case R.id.theme_paper:

                    themeFlag = 2;
                    Intent MainActivity2 = new Intent(getActivity(),MainActivity.class);
                    startActivity(MainActivity2);
                    break;
            }
        }

    }


}
