package com.kechuang.www.kc;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoActivity extends AppCompatActivity {

    private Button mBtnEdit;
    private TextView mTvID,mTvName,mTvPhone,mTvCredit;

    private static final String DB_NAME="dbUser1.db";
    SQLiteDatabase db;

    String StrID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);


        mTvID=findViewById(R.id.textView1);
        mTvName=findViewById(R.id.textView2);
        mTvPhone=findViewById(R.id.textView3);
        mTvCredit=findViewById(R.id.textView4);

        Intent intent = getIntent();
        String strID = intent.getStringExtra("userid");
        StrID=strID;

        OpenCreateDB();
        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+strID+"'",null);

        if(cursor.moveToFirst())
        {
            String strNAME = cursor.getString(cursor.getColumnIndex("username"));
            String strPHONE = cursor.getString(cursor.getColumnIndex("userphone"));
            String strCREDIT = cursor.getString(cursor.getColumnIndex("usercredit"));

            mTvID.setText("学号: "+strID);
            mTvName.setText("昵称: "+strNAME);
            mTvPhone.setText("手机号: "+strPHONE);
            mTvCredit.setText("信誉："+strCREDIT);
        }
        cursor.close();

        mBtnEdit = findViewById(R.id.btn_edit);
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this,UpdateInfoActivity.class);
                intent.putExtra("data2",StrID);
                startActivityForResult(intent,0);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(PersonalInfoActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
//    }

    private void OpenCreateDB()
    {
        try
        {
            db = openOrCreateDatabase(DB_NAME, this.MODE_PRIVATE, null);
        }
        catch (Throwable e)
        {
            Log.e("tag","openDatabase error:" + e.getMessage());
            db=null;
        }
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS tuserinfo (_id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR, username VARCHAR, userphone VARCHAR,userpwd VARCHAR, usertype INTEGER,usercount INTEGER,usercredit REAL)");
        }
        catch (SQLException se)
        {
            String msg = "doInstall.error:[%s].%s";
            Log.d("tag",String.format(msg, se.getClass(),se.getMessage()));
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(db!=null)
        {
            db.close();
        }
    }

}
