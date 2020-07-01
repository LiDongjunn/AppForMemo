package com.example.notecompus.Data;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SynchronizeData {
    private com.example.notecompus.Data.getDataByJsoup getDataByJsoup = new getDataByJsoup();

    private void add_to_html(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String target = "http://10.0.2.2:3000/add00?name="+s;
                try {
                    Document doc = Jsoup.parse(new URL(target),1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void delete_to_html(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String target = "http://10.0.2.2:3000/remove?id="+id;
                try {
                    Document doc = Jsoup.parse(new URL(target),1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void edit_to_html(final String id,final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String target = "http://10.0.2.2:3000/modify00?id="+id+"&name="+content;
                try {
                    Document doc = Jsoup.parse(new URL(target),1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void upLoad_to_html(List<String> userText) {
        List<String> userId = getDataByJsoup.getDataID();
        for (String id : userId){
            delete_to_html(id);
            Log.e("userText111111111111111",id);

        }
        for (String content : userText){
            add_to_html(content);
            Log.e("userText111111111111111",userText.toString());
        }
    }
}
