package com.example.notecompus.myAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notecompus.Data.UserData;
import com.example.notecompus.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.notecompus.PlanActivity.isAllSelectPlan;
import static com.example.notecompus.ui.home.HomeFragment.isAllSelectHome;

//import static com.example.notecompus.HomeActivity.isAllSelectHome;

public class AdapterDelete extends BaseAdapter {
    private List<String> title;
    private List<String> list;
    private List<String> time;
    private List<String> reminderTime;
    private List<String> stick;
    public static List<String> listDelete = new ArrayList<String>();
    private Context mContext;


    public AdapterDelete(Context mContext, UserData mUserData) {
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
            convertView = inflater.inflate(R.layout.item_delete2, null);
            holder =new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.stick);
            holder.buttonDelete = (CheckBox) convertView.findViewById(R.id.buttonDelete);
            holder.buttonDelete.setVisibility(View.VISIBLE);
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

        final String str = list.get(position);
        if (isAllSelectHome == 1||isAllSelectPlan == 1){
            holder.buttonDelete.setChecked(true);
            listDelete.add(str);
        }
        else if (isAllSelectHome == 0||isAllSelectPlan == 0){
            holder.buttonDelete.setChecked(false);
            listDelete.clear();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.buttonDelete.isChecked()) {
                    holder.buttonDelete.setChecked(false);
                    listDelete.remove(str);
                    Log.i("POSITION************","POSITION:blank");
                }
                else {
                    holder.buttonDelete.setChecked(true);
                    listDelete.add(str);
                    Log.i("POSITION************","POSITION:right");
                }
                Log.i("*****************",listDelete.toString());
            }
        });

        return convertView;

    }

    class ViewHolder{
        public ImageView mImageView;
        public TextView textViewLine0;
        public TextView textViewLine1;
        public TextView textViewLine2;
        public TextView reminderTimeTextView;
        public CheckBox buttonDelete;
    }

}
