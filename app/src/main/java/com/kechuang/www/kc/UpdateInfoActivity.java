package com.kechuang.www.kc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateInfoActivity extends AppCompatActivity {

    private Button mBtnSave;
    private EditText mEtName,mEtPwd,mEtpwd2,mEtPhone;
    private TextView mTvID;


    public static SQLiteDatabase db;
    private static final String DB_NAME="dbUser1.db";

    String StrID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        mTvID=findViewById(R.id.ID);
        mEtName = findViewById(R.id.name);
        mEtPwd = findViewById(R.id.password);
        mEtpwd2 = findViewById(R.id.password2);
        mEtPhone=findViewById(R.id.phone);

        Intent intent = getIntent();
        String strID = intent.getStringExtra("data2");
        StrID=strID;

        mTvID.setText(strID);

        OpenCreateDB();



        mBtnSave = findViewById(R.id.btn_save2);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEtName.getText().toString();
                String Pwd = mEtPwd.getText().toString();
                String Pwd2 = mEtpwd2.getText().toString();
                String Phone = mEtPhone.getText().toString();


                if (isStrEmpty(name) == false) {
                    if (isStrEmpty(Pwd) == false) {
                        if (isStrEmpty(Pwd2) == false) {
                            if (isStrEmpty(Phone) == false) {
                                if (isPwdSame(Pwd, Pwd2) == true) {
                                    UpdateUserInfo(name, Phone, Pwd);

                                } else {
                                    Toast.makeText(UpdateInfoActivity.this, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(UpdateInfoActivity.this, "手机号码不可为空！", Toast.LENGTH_LONG).show();
                                mEtpwd2.setFocusable(true);
                            }
                        } else {
                            Toast.makeText(UpdateInfoActivity.this, "确认密码不可为空！", Toast.LENGTH_LONG).show();
                            mEtpwd2.setFocusable(true);
                        }
                    } else {
                        Toast.makeText(UpdateInfoActivity.this, "密码不可为空！", Toast.LENGTH_LONG).show();
                        mEtPwd.setFocusable(true);
                    }
                } else {
                    Toast.makeText(UpdateInfoActivity.this, "昵称不可为空！", Toast.LENGTH_LONG).show();
                    mEtPwd.setFocusable(true);
                }
            }



        });

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
            db.execSQL("CREATE TABLE IF NOT EXISTS tuserinfo (_id INTEGER PRIMARY KEY AUTOINCREMENT,userid VARCHAR, username VARCHAR, userphone VARCHAR,userpwd VARCHAR, usertype INTEGER,usercount INTEGER,usercredit REAL)");
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

    private boolean isPwdSame(String strUserPwd,String strUserRePwd){

        if(strUserPwd.equals(strUserRePwd))
        {
            return true;
        }
        else
        {
            return false;
        }
    }



   private void UpdateUserInfo(String strUserName,String strUserPhone,String strUserPwd)
    {

        String sql = "update [tuserinfo] set userpwd = '"+strUserPwd+"' where userid='"+StrID+"'";

        String sql2 = "update [tuserinfo] set username = '"+strUserName+"' where userid='"+StrID+"'";
        String sql3 = "update [tuserinfo] set userphone = '"+strUserPhone+"' where userid='"+StrID+"'";
        try
        {
            db.execSQL(sql);
            db.execSQL(sql2);
            db.execSQL(sql3);
            Toast.makeText(UpdateInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();

        }
        catch (SQLException e)
        {
            Toast.makeText(UpdateInfoActivity.this, "您要修改的用户已经存在！", Toast.LENGTH_SHORT).show();

        }


    }
/*
    private void UpdateUserInfo(String strUserId,String strUserName,String strUserPhone,String strUserPwd)
    {

        int iUserType=0;

        if(isExistUserName(strUserId)==false)
        {
            ContentValues cv1 = new ContentValues();
            ContentValues cv2= new ContentValues();
            ContentValues cv3 = new ContentValues();
            ContentValues cv4 = new ContentValues();
            ContentValues cv5 = new ContentValues();
            cv1.put("userid",strUserId);
            cv2.put("username", strUserName);
            cv3.put("userphone", strUserPhone);
            cv4.put("userpwd", strUserPwd);
            cv5.put("usertype", iUserType);
            if(db!=null)
            {
                db.update("tuserinfo", cv1, "userid=StrID", null);
                db.update("tuserinfo", cv2, "userid=StrID", null);
                db.update("tuserinfo", cv3, "userid=StrID", null);
                db.update("tuserinfo", cv4, "userid=StrID", null);
                db.update("tuserinfo", cv5, "userid=StrID", null);
                Toast.makeText(UpdateInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(UpdateInfoActivity.this, "您要修改的用户已经存在！", Toast.LENGTH_SHORT).show();
        }
    }
*/
    protected void onDestroy()
    {
        super.onDestroy();
        if(db!=null)
        {
            db.close();
        }
    }

}
