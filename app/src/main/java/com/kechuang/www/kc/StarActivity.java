package com.kechuang.www.kc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class StarActivity extends AppCompatActivity implements
        OnCheckedChangeListener, OnRatingBarChangeListener {

    private static final String DB_NAME="dbUser1.db";
    SQLiteDatabase db1;

    private CheckBox ck_whole;
    private RatingBar rb_score;
    private TextView tv_rating;

    String AccID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        Intent intent = getIntent();
        String strID = intent.getStringExtra("dataRating");
        AccID=strID;

        ck_whole = findViewById(R.id.ck_whole);
        rb_score =  findViewById(R.id.rb_score);
        tv_rating =  findViewById(R.id.tv_rating);
        ck_whole.setOnCheckedChangeListener(this);
        rb_score.setIsIndicator(false);
        rb_score.setNumStars(5);
        rb_score.setRating(3);
        rb_score.setStepSize(1);
        rb_score.setOnRatingBarChangeListener(this);

        OpenCreateDB();




    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.ck_whole) {
            rb_score.setStepSize(ck_whole.isChecked()?1:rb_score.getNumStars()/10.0f);
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String desc = String.format("当前选中的是%s颗星", formatDecimal(rating,1));

        UpdateCredit(AccID,rating);

        tv_rating.setText(desc);
    }

    public static String formatDecimal(double value, int digit) {
        String format = String.format("%%.%df", digit);
        return String.format(format, value).toString();
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
            db1.execSQL("CREATE TABLE IF NOT EXISTS tuserinfo (_id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR, username VARCHAR, userphone VARCHAR,userpwd VARCHAR, usertype INTEGER)");
        }
        catch (SQLException se)
        {
            String msg = "doInstall.error:[%s].%s";
            Log.d("tag",String.format(msg, se.getClass(),se.getMessage()));
        }
    }

    public void UpdateCredit(String strAccID,float digit)
    {
       double Credit1 =0;
       int count1 = 0;


        Cursor cursor=db1.rawQuery("select * from tuserinfo where userid='"+strAccID+"'",null);

        if(cursor.moveToFirst())
        {
            Credit1=cursor.getDouble(cursor.getColumnIndex("usercredit"));
            count1=cursor.getInt(cursor.getColumnIndex("usercount"));

        }
        cursor.close();

       //计算信誉值
        Credit1=(Credit1*count1+digit*20)/(count1+1);
        count1++;

        ContentValues cv =new ContentValues();
        cv.put("usercredit",Credit1);
        cv.put("usercount",count1);
    if(db1!=null) {
        db1.update("tuserinfo", cv, "userid=?", new String[] {strAccID});

        Toast.makeText(StarActivity.this, "信誉修改成功！", Toast.LENGTH_SHORT).show();
    }
    else
    {
        Toast.makeText(StarActivity.this, "信誉修改失败！", Toast.LENGTH_SHORT).show();
    }


//        String sql = "update [tuserinfo] set usercredit = '"+Credit1+"' where userid='"+strAccID+"'";
 //       String sql2="update [tuserinfo] set usercount='"+count1+"'where userid'="+strAccID+"'";


//        try
//        {
   //         db1.execSQL(sql);
//            db1.execSQL(sql2);


//        }
//        catch (SQLException e)
//        {
//            Toast.makeText(StarActivity.this, "信誉修改失败！", Toast.LENGTH_SHORT).show();
//
//        }

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
