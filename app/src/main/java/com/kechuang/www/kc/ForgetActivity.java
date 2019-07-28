package com.kechuang.www.kc;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetActivity extends AppCompatActivity {

    private Button mBtnCheck;
    private EditText mEtId,mEtPhone,mEtPwd,mEtPwd2;

    public static SQLiteDatabase db;
    private static final String DB_NAME="dbUser1.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        mBtnCheck = findViewById(R.id.btn_check);
        mEtId = findViewById(R.id.et1_forget);
        mEtPhone = findViewById(R.id.et2_forget);
        mEtPwd = findViewById(R.id.et3_forget);
        mEtPwd2=findViewById(R.id.et4_forget);

        OpenCreateDB();

        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pwd = mEtPwd.getText().toString();
                String Phone=mEtPhone.getText().toString();
                String ID=mEtId.getText().toString();
                String Pwd2=mEtPwd2.getText().toString();
                
                String PHONE = null;

                //判断是否相等
                if (isStrEmpty(ID) == false) {
                    if (isStrEmpty(Pwd) == false) {
                        if (isStrEmpty(Pwd2) == false) {
                            if (isStrEmpty(Phone) == false) {
                                if (isStrSame(Pwd, Pwd2) == true) {
                                    //
                                    Cursor cursor=db.rawQuery("select * from tuserinfo where userid='"+ID+"'",null);

                                    if(cursor.moveToFirst())
                                    {
                                        String strPHONE = cursor.getString(cursor.getColumnIndex("userphone"));
                                        PHONE=strPHONE;

                                    }
                                    cursor.close();
                                    if(isStrSame(PHONE,Phone)==true)
                                    {
                                        String sql = "update [tuserinfo] set userpwd = '"+Pwd+"' where userid='"+ID+"'";
                                        try
                                        {
                                            db.execSQL(sql);
                                            
                                            Toast.makeText(ForgetActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();

                                        }
                                        catch (SQLException e)
                                        {
                                            Toast.makeText(ForgetActivity.this, "密码找回失败！", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(ForgetActivity.this, "ID与手机号不匹配！", Toast.LENGTH_LONG).show();
                                    }
                                    

                                } else {
                                    Toast.makeText(ForgetActivity.this, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ForgetActivity.this, "手机号码不可为空！", Toast.LENGTH_LONG).show();
                                mEtPwd2.setFocusable(true);
                            }
                        } else {
                            Toast.makeText(ForgetActivity.this, "确认密码不可为空！", Toast.LENGTH_LONG).show();
                            mEtPwd2.setFocusable(true);
                        }
                    } else {
                        Toast.makeText(ForgetActivity.this, "密码不可为空！", Toast.LENGTH_LONG).show();
                        mEtPwd.setFocusable(true);
                    }
                } else {
                    Toast.makeText(ForgetActivity.this, "ID不可为空！", Toast.LENGTH_LONG).show();
                    mEtPwd.setFocusable(true);
                }




                Toast.makeText(ForgetActivity.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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

    private boolean isStrSame(String str1,String str2){

        if(str1.equals(str2))
        {
            return true;
        }
        else
        {
            return false;
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
