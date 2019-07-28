package com.kechuang.www.kc.OrderView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kechuang.www.kc.HelpView.MyHelper;
import com.kechuang.www.kc.R;

public class Item0Activity extends AppCompatActivity {
    MyHelper myHelper;
    SQLiteDatabase db;

    private Button mBtnCancel,mBtnReturn;
    private TextView mTvId,mTvTitle,mTvDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item0);
        myHelper = new MyHelper(this);

        findItem();
        Selection();
    }

    public void findItem(){

        Intent intent = getIntent();
        String getorderid = intent.getStringExtra("orderid");

        mTvId = findViewById(R.id.tv_item_id);
        mTvTitle = findViewById(R.id.tv_item_title);
        mTvDetail = findViewById(R.id.tv_item_detail);

        mTvId.setText("订单编号: "+getorderid);
        db = myHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tOrder where _id='"+getorderid+"'",null);
        if(cursor.moveToFirst()){
            String gettitle = " "+cursor.getString(cursor.getColumnIndex("orderTitle"));
            String getdetail ="  "+cursor.getString(cursor.getColumnIndex("orderDetail"));
            mTvTitle.setText(gettitle);
            mTvDetail.setText(getdetail);
        }
        db.close();
    }

    public void Selection(){

        mBtnCancel = findViewById(R.id.btn_cancel);
        mBtnReturn = findViewById(R.id.btn_return);

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Item0Activity.this);
                builder.setMessage("确定取消此订单？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = getIntent();
                        String getorderid = intent.getStringExtra("orderid");
                        db = myHelper.getWritableDatabase();
                        db.execSQL("delete from tOrder where _id='"+getorderid+"'");
//                        db.delete("tOrder","_id=getorderid",null);
                        Toast.makeText(Item0Activity.this,"取消订单成功！\n 可返回个人订单查看详情",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        db.close();
    }
}
