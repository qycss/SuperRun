package com.kechuang.www.kc.HelpView;

import android.app.Activity;
import android.content.ContentValues;
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

import com.kechuang.www.kc.OrderView.OrderActivity;
import com.kechuang.www.kc.R;

public class ItemActivity extends AppCompatActivity {

    MyHelper myHelper;
    SQLiteDatabase db;

    private TextView mTvId,mTvTitle,mTvDetail;
    private Button mBtnAcc,mBtnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item);
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

        mBtnAcc = findViewById(R.id.btn_acc);
        mBtnReturn = findViewById(R.id.btn_cancel);

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mBtnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                builder.setMessage("确定接下此订单？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = getIntent();
                        String getorderid = intent.getStringExtra("orderid");
                        String strID = intent.getStringExtra("userid");
                        db = myHelper.getWritableDatabase();
                        Cursor cursor=db.rawQuery("select * from tOrder where _id='"+getorderid+"'",null);
                        if(cursor.moveToFirst()){
                            String gettitle = cursor.getString(cursor.getColumnIndex("orderTitle"));
                            String getdetail = cursor.getString(cursor.getColumnIndex("orderDetail"));
                            String getpubid = cursor.getString(cursor.getColumnIndex("pubId"));
                            ContentValues cv = new ContentValues();
                            cv.put("_id",getorderid);
                            cv.put("orderType",1);
                     //       cv.put("orderTitle",gettitle);
                       //     cv.put("orderDetail",getdetail);
                         //   cv.put("pubId",getpubid);
                            cv.put("accId",strID);
                            db.update("tOrder",cv,"_id=?",new String[] {getorderid});
                        }else{
                            Toast.makeText(ItemActivity.this,"error..",Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                        Toast.makeText(ItemActivity.this,"接受订单成功！\n 可返回个人订单查看详情",Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(ItemActivity.this, HelpActivity.class);
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
