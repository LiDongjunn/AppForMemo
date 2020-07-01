package com.example.notecompus.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notecompus.Data.UserData;
import com.example.notecompus.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterShow extends BaseAdapter {

    private List<String> title;
    private List<String> list;
    private List<String> time;
    private List<String> reminderTime;
    private List<String> stick;
    //用位置来存储要删除的数据好像不行。。。
    public   static List<String> listDelete = new ArrayList<String>();
    private Context mContext;


    public AdapterShow(Context mContext, UserData mUserData) {
        super();
        this.mContext = mContext;
        this.title = mUserData.getUserContentTitle();
        this.list = mUserData.getUserContent();
        this.time = mUserData.getUserContentEditTime();
        this.reminderTime = mUserData.getUserContentReminderTime();
        this.stick=mUserData.getUserContentStick();
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
            holder.mImageView = (ImageView) convertView.findViewById(R.id.stick);
            holder.textViewLine0 = (TextView) convertView.findViewById(R.id.adapterText0);
            holder.textViewLine1 = (TextView) convertView.findViewById(R.id.adapterText1);
            holder.textViewLine2 = (TextView) convertView.findViewById(R.id.adapterText2);
            holder.reminderTimeTextView = (TextView) convertView.findViewById(R.id.reminderTimeTextView);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.textViewLine0.setTag(position);
            holder.textViewLine1.setTag(position);
            holder.textViewLine2.setTag(position);
            holder.reminderTimeTextView.setTag(position);
        }

        if (!list.get(position).equals("")){
            holder.textViewLine0.setText(title.get(position).equals("")?"无标题":title.get(position));
            holder.textViewLine1.setText(list.get(position));
            holder.textViewLine2.setText(time.get(position));
            holder.reminderTimeTextView.setText(reminderTime.get(position));

            if (reminderTime.get(position).length()>0){
                holder.mImageView.setImageResource(R.drawable.alarm_alert);
                if (stick.get(position).equals("1")){
                    holder.mImageView.setImageResource(R.drawable.book_mark);
                }
            }else if (stick.get(position).equals("1")){
                holder.mImageView.setImageResource(R.drawable.book_mark);
            }
        }

        return convertView;

    }

    static class ViewHolder  {
        public ImageView mImageView;
        public TextView textViewLine0;
        public TextView textViewLine1;
        public TextView textViewLine2;
        public TextView reminderTimeTextView;
    }

}
