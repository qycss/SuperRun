package com.kechuang.www.kc.OrderView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kechuang.www.kc.HelpView.MyHelper;
import com.kechuang.www.kc.R;

public class Item12Activity extends AppCompatActivity {
    SQLiteDatabase db;
    MyHelper myHelper;
    private TextView mTvId,mTvTitle,mTvDetail;
    private Button mBtnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item12);

        myHelper = new MyHelper(this);
        findItem();

        mBtnReturn = findViewById(R.id.btn_cancel);
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                setResult(RESULT_OK,intent1);
                finish();
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
}
