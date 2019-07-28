package com.kechuang.www.kc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Button mBtnSave;
    private EditText mEtId,mEtName,mEtPwd,mEtpwd2,mEtPhone;

    private TextView tvShowInfo=null;

    private static final String DB_NAME="dbUser1.db";
    private SQLiteDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mEtId = findViewById(R.id.et_ID);
        mEtName = findViewById(R.id.et_name);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtpwd2 = findViewById(R.id.et_pwd2);
        mEtPhone=findViewById(R.id.et_phone);

        mBtnSave = findViewById(R.id.btn_save);

        OpenCreateDB();

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = mEtId.getText().toString();
                String name = mEtName.getText().toString();
                String Pwd = mEtPwd.getText().toString();
                String Pwd2 = mEtpwd2.getText().toString();
                String Phone=mEtPhone.getText().toString();

                if(isStrEmpty(ID)==false) {
                    if (isStrEmpty(name) == false) {
                        if (isStrEmpty(Pwd) == false) {
                            if (isStrEmpty(Pwd2) == false) {
                                if (isStrEmpty(Phone) == false) {
                                    if (isPwdSame(Pwd, Pwd2) == true) {
                                        insertUserInfo(ID, name, Phone, Pwd);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "手机号码不可为空！", Toast.LENGTH_LONG).show();
                                    mEtpwd2.setFocusable(true);
                                }
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "确认密码不可为空！", Toast.LENGTH_LONG).show();
                                mEtpwd2.setFocusable(true);
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "密码不可为空！", Toast.LENGTH_LONG).show();
                            mEtPwd.setFocusable(true);
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "昵称不可为空！", Toast.LENGTH_LONG).show();
                        mEtPwd.setFocusable(true);
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"学号不可为空！",Toast.LENGTH_LONG).show();
                    mEtName.setFocusable(true);
                }
            }

        });
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

    private boolean isExistAdmin()
    {
        Cursor cursor=db.rawQuery("select * from tuserinfo where usertype=1",null);

        if(cursor.getCount()> 0)
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }


//    private void insertAdminInfo()
//    {
//        String strUserName= "guanghezhang@163.com";
//
//        String strUserPwd="1";
//
//        int iUserType=1;
//
//        if(isExistAdmin()==false)
//        {
//
//            ContentValues cvUserInfo = new ContentValues();
//
//            cvUserInfo.put("username", strUserName);
//
//            cvUserInfo.put("userpwd", strUserPwd);
//
//            cvUserInfo.put("usertype", iUserType);
//            if(db!=null)
//            {
//                db.insert("tuserinfo", null, cvUserInfo);
//
//                Log.d("msg","插入结束");
//                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
//                tvShowInfo.setText("用户名："+strUserName+"\n"+"密码："+strUserPwd+"\n"+"类别："+iUserType);
//            }
//        }
//        else
//        {
//            tvShowInfo.setText("已存在系统管理员\n用户名："+strUserName+"\n"+"密码："+strUserPwd+"\n");
//        }
//    }

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

    private boolean isExistUserName(String strUserId)
    {

        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+strUserId+"'",null);

        if(cursor.getCount()> 0)
        {
            cursor.close();
            return true;
        }
        else
        {

            cursor.close();
            return false;
        }
    }


    private void insertUserInfo(String strUserId,String strUserName,String strUserPhone,String strUserPwd)
    {

        int iUserType=0;
        int UserCount=0;
        double UserCredit=100;

        if(isExistUserName(strUserId)==false)
        {
            ContentValues cvRUserInfo = new ContentValues();
            cvRUserInfo.put("userid",strUserId);
            cvRUserInfo.put("username", strUserName);
            cvRUserInfo.put("userphone", strUserPhone);
            cvRUserInfo.put("userpwd", strUserPwd);
            cvRUserInfo.put("usertype", iUserType);
            cvRUserInfo.put("usercredit",UserCredit);
            cvRUserInfo.put("usercount",UserCount);
            if(db!=null)
            {
                db.insert("tuserinfo", null, cvRUserInfo);
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "您要注册的用户名已经存在！", Toast.LENGTH_SHORT).show();
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



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
