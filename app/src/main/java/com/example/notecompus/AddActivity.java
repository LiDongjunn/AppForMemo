package com.example.notecompus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddActivity extends Activity {

    private static EditText editText;

    public static final int resultCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //变量绑定
        ImageButton exit = (ImageButton) findViewById(R.id.exit);
        editText = (EditText) findViewById(R.id.EditText);

//        //保存事件函数
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建Intent
                Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                //获取编辑数据
                String s = editText.getText().toString();

                if (s.equals("")) {
                    AddActivity.this.setResult(0, intent);
                    AddActivity.this.finish();
                } else {

                    //新建bundle对象，通过Bundle对象传递数据
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("userEditStrings", s);
                    //intent 绑定bundle并输出
                    intent.putExtras(bundle);
                    //开始回调
                    AddActivity.this.setResult(resultCode, intent);
                    //结束此activity
                    AddActivity.this.finish();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //创建Intent
        Intent intent = new Intent(AddActivity.this, HomeActivity.class);
        //获取编辑数据
        String s = editText.getText().toString();

        if (s.equals("")) {
            AddActivity.this.setResult(0, intent);
            AddActivity.this.finish();
        } else {

            //新建bundle对象，通过Bundle对象传递数据
            Bundle bundle = new Bundle();
            bundle.putCharSequence("userEditStrings", s);
            //intent 绑定bundle并输出
            intent.putExtras(bundle);
            //开始回调
            AddActivity.this.setResult(resultCode, intent);
            //结束此activity
            AddActivity.this.finish();
        }
    }
}
