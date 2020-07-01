package com.example.notecompus.Data;

public class Data {
    public String Title = "";
    public String Content = "";
    public String EditTime = "";
    public String reminderTime = "";
    public String stick = "";
    public Data() {
    }

    public Data(String content) {
        Content = content;
    }

    public Data(String content, String editTime) {
        Content = content;
        EditTime = editTime;

    }



    public Data(String title, String content, String editTime) {
        Title = title;
        Content = content;
        EditTime = editTime;

    }
    public Data(String title, String content, String editTime, String reminderTime) {
        Title = title;
        Content = content;
        EditTime = editTime;
        this.reminderTime = reminderTime;
    }

    public Data(String title, String content, String editTime, String reminderTime, String stick) {
        Title = title;
        Content = content;
        EditTime = editTime;
        this.reminderTime = reminderTime;
        this.stick = stick;
    }

    @Override
    public String toString() {
        return "Data{" +
                "Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", EditTime='" + EditTime + '\'' +
                ", reminderTime='" + reminderTime + '\'' +
                ", stick='" + stick + '\'' +
                '}';
    }
}
