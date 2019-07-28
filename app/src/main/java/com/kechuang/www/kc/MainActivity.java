package com.kechuang.www.kc;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kechuang.www.kc.FinishedView.FinishedActivity;
import com.kechuang.www.kc.HelpView.HelpActivity;
import com.kechuang.www.kc.OrderView.OrderActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnHelp,mBtnHelped,mBtnPerInfo,mBtnOrder1,mBtnOrder2,mBtnMoney,mBtnComment,mBtnLogout;
    private TextView mTvUser,mTvUserID;
    private static final String DB_NAME="dbUser1.db";
    SQLiteDatabase db;

    String StrID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBtnHelp = findViewById(R.id.btn_main_1);
        mBtnHelped = findViewById(R.id.btn_main_2);

        mTvUser = findViewById(R.id.tv_user);
        mBtnPerInfo = findViewById(R.id.btn_personal);
        mBtnOrder1 = findViewById(R.id.btn_order1);
        mBtnMoney = findViewById(R.id.btn_money);
        mBtnOrder2 = findViewById(R.id.btn_order2);

        /*mBtnComment = findViewById(R.id.btn_comment);*/
        mBtnLogout = findViewById(R.id.btn_logout);

        Intent intent = getIntent();
        String strID = intent.getStringExtra("data");
        StrID=strID;
        mTvUserID = findViewById(R.id.tv_userID);
        mTvUserID.setText(strID);

        OpenCreateDB();
        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+strID+"'",null);

        if(cursor.moveToFirst()){
            String strNAME = cursor.getString(cursor.getColumnIndex("username"));
            mTvUser.setText(strNAME);
        }
        cursor.close();

        mTvUser.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        setListeners();
    }


    private void setListeners(){
        OnClick onClick = new OnClick();
        mBtnHelp.setOnClickListener(onClick);
        mBtnHelped.setOnClickListener(onClick);
        mBtnPerInfo.setOnClickListener(onClick);
        mBtnOrder1.setOnClickListener(onClick);
        mBtnMoney.setOnClickListener(onClick);
        mBtnOrder2.setOnClickListener(onClick);
        /*mBtnComment.setOnClickListener(onClick);*/
        mBtnLogout.setOnClickListener(onClick);
        mTvUser.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.btn_main_1:
                    intent = new Intent(MainActivity.this, HelpActivity.class);
                    intent.putExtra("datahelp",StrID);
                    startActivityForResult(intent,0);
                    break;
                case R.id.btn_main_2:
                    intent = new Intent(MainActivity.this,HelpedActivity.class);
                    intent.putExtra("datahelped",StrID);
                    startActivityForResult(intent,0);
                    break;
              /*case R.id.btn_main_3:
                    break;*/

                case R.id.tv_user:
                    intent = new Intent(MainActivity.this,PersonalInfoActivity.class);
                    intent.putExtra("userid",StrID);
                    startActivity(intent);
                    break;

                case R.id.btn_personal:
                    intent = new Intent(MainActivity.this,PersonalInfoActivity.class);
                    intent.putExtra("userid",StrID);
                    startActivity(intent);
                    break;
                case R.id.btn_order1:
                    intent = new Intent(MainActivity.this, OrderActivity.class);
                    intent.putExtra("userid",StrID);
                    startActivityForResult(intent,0);
                    break;
                case R.id.btn_order2:
                    intent = new Intent(MainActivity.this,FinishedActivity.class);
                    intent.putExtra("userid",StrID);
                    startActivity(intent);
                    break;

                case R.id.btn_money:
                    intent = new Intent(MainActivity.this, PayActivity.class);

                    startActivity(intent);
                    break;
                /*case R.id.btn_comment:
                    break;*/
                case R.id.btn_logout:
                    //退出登陆
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("确定登出当前账户？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                    /*new AlertDialog.Builder(MainActivity.this).setTitle("尊敬的用户")
                            .setMessage("确定登出当前账户?").setPositiveButton("确定",null)
                            .setNegativeButton("取消",null).show();*/

                    break;
            }

        }
    }


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
            db.execSQL("CREATE TABLE IF NOT EXISTS tuserinfo (_id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR, username VARCHAR, userphone VARCHAR,userpwd VARCHAR, usertype INTEGER,usercredit REAL,usercount INTEGER)");
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
