package com.example.notecompus.receiver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.notecompus.AddContentActivity;
import com.example.notecompus.MainActivity;
import com.example.notecompus.R;

public class AlarmReceiver extends BroadcastReceiver{
    private Context mContext;
    @Override
    public void onReceive( Context context, Intent intent) {
        mContext = context;
        showNormalDialog();


    }



    public void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        Runnable r0 = new Runnable() {
            @Override
            public void run() {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(mContext, notification);
                r.play();
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r.stop();
            }
        };
        final Thread t0 = new Thread(r0);
        t0.setName("alarm");
        t0.start();
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setIcon(R.drawable.alarm);
        normalDialog.setTitle("Alarm start");
        normalDialog.setMessage("Keep alarming?");
        normalDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        t0.interrupt();
                    }
                });
        AlertDialog alertDialog = normalDialog.create();
        // 显示
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
    }

}
