package com.kechuang.www.kc.OrderView;

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

import com.kechuang.www.kc.HelpView.HelpActivity;
import com.kechuang.www.kc.HelpView.ItemActivity;
import com.kechuang.www.kc.HelpView.MyHelper;
import com.kechuang.www.kc.HelpView.Order;
import com.kechuang.www.kc.OrderView.OrderActivity;
import com.kechuang.www.kc.R;
import com.kechuang.www.kc.StarActivity;

import org.w3c.dom.Text;

import java.util.List;

public class Item1Activity extends AppCompatActivity {
    MyHelper myHelper;
    SQLiteDatabase db;

    private TextView mTvAcc,mTvId,mTvTitle,mTvDetail;
    private Button mBtnReturn,mbtnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item1);

        mTvAcc = findViewById(R.id.tv_item_acc);
        myHelper = new MyHelper(this);

        findItem();
        findAcc();

        mBtnReturn = findViewById(R.id.btn_cancel);
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

        mbtnConfirm = findViewById(R.id.btn_confirm);
        mbtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Item1Activity.this);
                builder.setMessage("确定此订单已完成？");
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
                            String getaccid = cursor.getString(cursor.getColumnIndex("accId"));
                            ContentValues cv = new ContentValues();
//                            cv.put("_id",getorderid);
                            cv.put("orderType",2);
//                            cv.put("orderTitle",gettitle);
//                            cv.put("orderDetail",getdetail);
//                            cv.put("pubId",strID);
                            cv.put("accId",getaccid);
                            db.update("tOrder",cv,"_id=?",new String[] {getorderid});

                            cursor.close();
                            Toast.makeText(Item1Activity.this,"确认成功！\n 可返回历史订单查看详情",Toast.LENGTH_SHORT).show();


                            Intent intent1 = new Intent(Item1Activity.this, StarActivity.class);

                         intent1.putExtra("dataRating",getaccid);
                           startActivity(intent1);


                        }else{
                            Toast.makeText(Item1Activity.this,"error..",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        db.close();
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

    public void findAcc(){
        Intent intent = getIntent();
        String getaccid = intent.getStringExtra("accid");
        db = myHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+getaccid+"'",null);
        if(cursor.moveToFirst()){
            String accname = cursor.getString(cursor.getColumnIndex("username"));
            String accphone = cursor.getString(cursor.getColumnIndex("userphone"));
            mTvAcc.setText("接单者信息："+"\n学号:"+getaccid+"\n昵称:"+accname+"\n联系方式:"+accphone);
        }
        db.close();
    }
}
