package com.kechuang.www.kc;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin,mBtnRegister,mBtnForget;
    private EditText mEtUser,mEtPwd;

    private String strUserName="";
    private String strPwd="";
    private int iUT=-1;

    public static SQLiteDatabase db;
    private static final String DB_NAME="dbUser1.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtUser = findViewById(R.id.login_user);
        mEtPwd = findViewById(R.id.login_password);

        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
        mBtnForget = findViewById(R.id.btn_forget_login);

        OpenCreateDB();

        Button.OnClickListener listener = new Button.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(v.getId()==R.id.btn_login)
                {
                    strUserName=mEtUser.getText().toString();
                    strPwd=mEtPwd.getText().toString();

                    if(isStrEmpty(strUserName)==false)
                    {
                        if(isStrEmpty(strPwd)==false)
                        {
                            if(isValidUser(strUserName,strPwd)==true)
                            {
                                Toast.makeText(LoginActivity.this, "用户登录成功！", Toast.LENGTH_SHORT).show();

                                showUserType(strUserName,strPwd);

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("data",strUserName);
                                startActivityForResult(intent,0);

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "用户登录失败！", Toast.LENGTH_SHORT).show();


                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();
                            mEtPwd.setFocusable(true);
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "用户名不可为空！", Toast.LENGTH_SHORT).show();
                        mEtUser.setFocusable(true);
                    }
                }
                else if(v.getId()==R.id.btn_register)
                {

                    Intent intent=new Intent();

                    intent.setClass(LoginActivity.this,RegisterActivity.class);

                    startActivity(intent);

                }

                else if(v.getId()==R.id.btn_forget_login)
                {

                    Intent intent=new Intent();

                    intent.setClass(LoginActivity.this,ForgetActivity.class);

                    startActivity(intent);

                }
            }
        };

        mBtnLogin.setOnClickListener(listener);
        mBtnRegister.setOnClickListener(listener);
        mBtnForget.setOnClickListener(listener);

        //注册
//        mBtnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivityForResult(intent,0);
//            }
//        });
//        mBtnLogin.setOnClickListener(new LoginListener());
//
//        忘记密码
//        mBtnForget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
//                startActivityForResult(intent,0);
//            }
//        });
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


    private boolean isValidUser(String strUserName,String strUserPwd)
    {

        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+strUserName+"' and userpwd='"+strUserPwd+"'",null);


        if(cursor.getCount()== 1)
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


    private int getUserType(String strUserName,String strUserPwd)
    {
        int igut=-1;

        Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+strUserName+"' and userpwd='"+strUserPwd+"'",null);


        if(cursor.getCount()== 1)
        {

            if(cursor.moveToFirst())
            {

                igut =cursor.getInt(cursor.getColumnIndex("usertype"));
                cursor.close();
            }
        }
        else
        {

            cursor.close();
            igut =99;
        }
        return igut;
    }

    private void showUserType(String strUserName,String strUserPwd)
    {
        iUT= getUserType(strUserName,strUserPwd);
        if(iUT==1)
        {
            Toast.makeText(LoginActivity.this, "您是系统管理员！", Toast.LENGTH_SHORT).show();
        }
        else if(iUT==0)
        {
            Toast.makeText(LoginActivity.this, "您是普通用户！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "异常情况！", Toast.LENGTH_SHORT).show();
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
