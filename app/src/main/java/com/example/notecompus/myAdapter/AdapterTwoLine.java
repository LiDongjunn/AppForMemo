package com.example.notecompus.myAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.notecompus.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.notecompus.HomeActivity.isAllSelectHome;
import static com.example.notecompus.PlanActivity.isAllSelectPlan;

public class AdapterTwoLine extends BaseAdapter {

    private List<String> list;
    private List<String> time;
    private List<String> reminderTime;
    //用位置来存储要删除的数据好像不行。。。
    public   static List<String> listDelete = new ArrayList<String>();
    private Context mContext;


    public AdapterTwoLine(Context mContext, List<String> list,List<String> time,List<String> reminderTime) {
        super();
        this.mContext = mContext;
        this.list = list;
        this.time = time;
        this.reminderTime = reminderTime;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        //加 final holder 才能在 public void onClick 使用
        final ViewHolder holder;
        

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_delete2,null);
            holder = new ViewHolder();
            holder.textViewLine1 = (TextView) convertView.findViewById(R.id.adapterText1);
            holder.textViewLine2 = (TextView) convertView.findViewById(R.id.adapterText2);
            holder.reminderTimeTextView = (TextView) convertView.findViewById(R.id.reminderTimeTextView);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.textViewLine1.setTag(position);
            holder.textViewLine2.setTag(position);
            holder.reminderTimeTextView.setTag(position);
        }

        if (!list.get(position).equals("")){
            holder.textViewLine1.setText(list.get(position));
            holder.textViewLine2.setText(time.get(position));
            holder.reminderTimeTextView.setText(reminderTime.get(position));
        }

        return convertView;

    }

    static class ViewHolder  {
        public TextView textViewLine1;
        public TextView textViewLine2;
        public TextView reminderTimeTextView;
    }

}
