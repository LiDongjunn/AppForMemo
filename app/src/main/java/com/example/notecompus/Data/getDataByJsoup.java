package com.example.notecompus.Data;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class getDataByJsoup {
    private static List<String> InternetUserId = new ArrayList<>();

    public static List<String> getDataID(){
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                //                // 网络加载HTML文档
                try {
                    InternetUserId.clear();

                    Document doc = Jsoup.parse(new URL("http://10.0.2.2:3000/list"),1000);
                    Elements element = doc.select("tbody>tr");
                    for (Element i : element){
                        Elements element2 = i.select("tr>td");
                        InternetUserId.add(element2.get(0).text());
                    }
                    InternetUserId.remove("ID");
                    Log.e("InternetUserId",InternetUserId.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2= new Thread(r2);
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return InternetUserId;
    }

    public static synchronized UserData getData() {
        // 开启一个新线程
        final UserData mUserData = new UserData();
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                //                // 网络加载HTML文档
                try {
                    Document doc = Jsoup.parse(new URL("http://10.0.2.2:3000/list"), 1000);
                    Elements element = doc.select("tbody>tr");
                    for (Element i : element) {
                        Elements element2 = i.select("tr>td");
                        mUserData.mData.add(new Data("",element2.get(1).text(),"","",""));
                    }
                    mUserData.mData.remove(0);
                    Log.e("UserId", mUserData.getUserContentId().toString());

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread t1= new Thread(r1);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mUserData;
    }

}
