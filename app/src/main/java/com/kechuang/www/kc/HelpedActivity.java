package com.kechuang.www.kc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kechuang.www.kc.HelpView.HelpActivity;

public class HelpedActivity extends AppCompatActivity {

    private Button mBtnPublish;
    private TextView mTvTtile,mTv1,mTv2,mTv3,mTv4;
    private EditText mEtTitle,mEtd1,mEtd2,mEtd3;

    private static final String DB_NAME="dbUser1.db";
    private SQLiteDatabase db1;

    String PubID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helped);

        Intent intent = getIntent();
        final String strID = intent.getStringExtra("datahelped");
        PubID=strID;

        mTvTtile = findViewById(R.id.tv_title);
        mTv1 = findViewById(R.id.textView1);
        mTv2 = findViewById(R.id.textView2);
        mTv3 = findViewById(R.id.textView3);
        mTv4 = findViewById(R.id.textView4);
        mTvTtile.setTypeface(Typeface.createFromAsset(getAssets(),"miaowu.ttf"));
        mTvTtile.setEnabled(false);
        mTv1.setTypeface(Typeface.createFromAsset(getAssets(),"calibri.ttf"));
        mTv1.setEnabled(false);
        mTv2.setTypeface(Typeface.createFromAsset(getAssets(),"calibri.ttf"));
        mTv2.setEnabled(false);
        mTv3.setTypeface(Typeface.createFromAsset(getAssets(),"calibri.ttf"));
        mTv3.setEnabled(false);
        mTv4.setTypeface(Typeface.createFromAsset(getAssets(),"calibri.ttf"));
        mTv4.setEnabled(false);

        mEtTitle=findViewById(R.id.helped_content);
        mEtd1=findViewById(R.id.helped_address);
        mEtd2=findViewById(R.id.helped_costs);
        mEtd3=findViewById(R.id.helped_timelimit);

        OpenCreateDB();



        mBtnPublish = findViewById(R.id.btn_publish);
        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mEtTitle.getText().toString();
                String address = mEtd1.getText().toString();
                String costs = mEtd2.getText().toString();
                String timelimit = mEtd3.getText().toString();

                String Detail ="";



                Detail=Detail+"地址:"+address+"\n";
                Detail=Detail+" 佣金:"+costs+"\n";
                Detail=Detail+" 时间限制:"+timelimit+"\n";


                if (isStrEmpty(title) == false) {
                    if (isStrEmpty(address) == false) {
                        if (isStrEmpty(costs) == false) {

                                insertOrder(title,Detail);
                        }
                        else {
                            Toast.makeText(HelpedActivity.this, "佣金不可为空！", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(HelpedActivity.this, "地址不可为空！", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(HelpedActivity.this, "内容不可为空！", Toast.LENGTH_LONG).show();
                    }


            }
        });










    }


    private void OpenCreateDB()
    {
        try
        {
            db1 = openOrCreateDatabase(DB_NAME, this.MODE_PRIVATE, null);
        }
        catch (Throwable e)
        {
            Log.e("tag","openDatabase error:" + e.getMessage());
            db1=null;
        }
        try
        {
            db1.execSQL("CREATE TABLE IF NOT EXISTS tOrder (_id INTEGER PRIMARY KEY AUTOINCREMENT,orderType INTEGER, orderTitle VARCHAR, orderDetail VARCHAR,pubId VARCHAR, accId VARCHAR)");
        }
        catch (SQLException se)
        {
            String msg = "doInstall.error:[%s].%s";
            Log.d("tag",String.format(msg, se.getClass(),se.getMessage()));
        }



    }

private boolean isStrEmpty(String strInput)
        {

        if(strInput.equals(""))
        {
        return true;
        }
        else
        {
        return false;
        }
        }


    private void insertOrder(String strTitle,String strDetail)
    {

        int oType=0;
        String AcId="null";
            Cursor cursor=db1.rawQuery("select * from tOrder",null);
            int cnt=2;
            while(cursor.moveToNext()){
                cnt++;
            }

            cursor.close();
            String strid = String.valueOf(cnt);
            ContentValues cvOrderInfo = new ContentValues();
            cvOrderInfo.put("_id",strid);
            cvOrderInfo.put("orderTitle",strTitle);
            cvOrderInfo.put("orderDetail", strDetail);
            cvOrderInfo.put("pubId", PubID);
            cvOrderInfo.put("orderType", oType);
            cvOrderInfo.put("accId",AcId);
            if(db1!=null)
            {
                db1.insert("tOrder", null, cvOrderInfo);
                Toast.makeText(HelpedActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }


    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(db1!=null)
        {
            db1.close();
        }
    }
}
