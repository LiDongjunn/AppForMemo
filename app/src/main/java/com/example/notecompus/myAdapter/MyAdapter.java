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

import java.util.ArrayList;
import java.util.List;

//import static com.example.notecompus.HomeActivity.isAllSelectHome;
import static com.example.notecompus.PlanActivity.isAllSelectPlan;
import static com.example.notecompus.ui.home.HomeFragment.isAllSelectHome;

public class MyAdapter extends BaseAdapter {
    
    private List<String> list;
    //用位置来存储要删除的数据好像不行。。。
    public static List<String> listDelete = new ArrayList<String>();
    private Context mContext;
    private static final int SELECTALL_STATE = -1;


    public MyAdapter(Context mContext, List<String> list) {
        super();
        this.mContext = mContext;
        //其中this.data = data;的意思是将传入的形参赋值给我们的私有变量以供使用
        this.list = list;

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
            convertView = inflater.inflate(R.layout.item_delete, null);
            holder =new ViewHolder();
            holder.buttonDelete = (CheckBox) convertView.findViewById(R.id.buttonDelete);
            holder.textViewDelete = (TextView) convertView.findViewById(R.id.textDelete);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.textViewDelete.setTag(position);
        }
        holder.textViewDelete.setText(list.get(position));
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
        public TextView textViewDelete;
        public CheckBox buttonDelete;
    }

}
