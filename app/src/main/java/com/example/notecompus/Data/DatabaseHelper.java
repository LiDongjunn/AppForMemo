package com.example.notecompus.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.notecompus.SignUp.USER_ACCOUNT;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "UserData";
//    public static final String DATABASE_NAME0 = "mUserData";
//    public static final String USER_ACCOUNT = "userAccount";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_ID = "ID";
    public static final String USER_CONTENT = "userContent";
    public static final String USER_CONTENT_EDIT_TIME = "userContentEditTime";
    public static final String USER_CONTENT_REMINDER_TIME = "userContentReminderTime";
    public static final String USER_CONTENT_TITLE = "userContentTitle";
    public static final String USER_CONTENT_STICK = "userContentStick";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "test.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
