package com.example.notecompus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notecompus.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import static com.example.notecompus.ui.theme.ThemeFragment.themeFlag;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private TextView headerAccount;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.theme)
                .setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerLayout=navigationView.getHeaderView(0);
        headerAccount = headerLayout.findViewById(R.id.header_account);
        SharedPreferences userAccount = this.getSharedPreferences("userAccount", 0);
        String getSharedPreferenceAccount = userAccount.getString("recentAccount", "");
        headerAccount.setText(getSharedPreferenceAccount);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_delete:
//
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.addContent:
//                Intent AddContentActivity = new Intent(MainActivity.this, com.example.notecompus.AddContentActivity.class);
//                startActivity(AddContentActivity);
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void exit(View view) {
        Intent LoginActivity = new Intent(this, LoginActivity.class);
        startActivity(LoginActivity);
    }
}
