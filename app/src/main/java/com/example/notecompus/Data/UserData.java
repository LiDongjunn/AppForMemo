package com.example.notecompus.Data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserData {
    private String userAccount = "";
    private String userPassword = "";

    public   List<Data> mData = new ArrayList<Data>();
    private  List<String> userContent = new ArrayList<String>();
    private  List<String> userContentId = new ArrayList<String>();
    private  List<String> userContentTitle = new ArrayList<String>();
    private  List<String> userContentEditTime = new ArrayList<String>();
    private  List<String> userContentReminderTime = new ArrayList<String>();
    private  List<String> userContentStick = new ArrayList<String>();

    public UserData() {
    }


    public UserData(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public List<String> getUserContentTitle() {
        userContentTitle.clear();
        for (int i = 0; i<mData.size();i++){
            userContentTitle.add(mData.get(i).Title);
        }
        return userContentTitle;
    }

    public void setUserContentTitle(List<String> userContentTitle) {
        this.userContentTitle = userContentTitle;
    }

    public List<String> getUserContent() {
        userContent.clear();
        for (int i = 0; i<mData.size();i++){
            userContent.add(mData.get(i).Content);
        }
        return userContent;
    }

    public void setUserContent(List<String> userContent) {
        this.userContent = userContent;
    }

    public List<String> getUserContentId() {
        return userContentId;
    }

    public void setUserContentId(List<String> userContentId) {
        this.userContentId = userContentId;
    }

    public List<String> getUserContentEditTime() {
        userContentEditTime.clear();
        for (int i = 0; i<mData.size();i++){
            userContentEditTime.add(mData.get(i).EditTime);
        }
        return userContentEditTime;
    }

    public List<String> getUserContentReminderTime() {
        userContentReminderTime.clear();
        for (int i = 0; i<mData.size();i++){
            userContentReminderTime.add(mData.get(i).reminderTime);
        }
        return userContentReminderTime;
    }

    public void setUserContentEditTime(List<String> userContentEditTime) {
        this.userContentEditTime = userContentEditTime;
    }

    public List<String> getUserContentStick() {
        userContentStick.clear();
        for (int i = 0; i<mData.size();i++){
            userContentStick.add(mData.get(i).stick);
        }
        return userContentStick;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(userAccount, userData.userAccount) &&
                Objects.equals(userPassword, userData.userPassword) &&
                Objects.equals(userContent, userData.userContent) &&
                Objects.equals(userContentId, userData.userContentId) &&
                Objects.equals(userContentEditTime, userData.userContentEditTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(userAccount, userPassword, userContent, userContentId, userContentEditTime);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", mData=" + mData +
                ", userContent=" + userContent +
                ", userContentId=" + userContentId +
                ", userContentEditTime=" + userContentEditTime +
                ", userContentReminderTime=" + userContentReminderTime +
                '}';
    }

    public void mData(String content, String editTime) {
        mData.add(new Data(content,editTime));
    }
}
