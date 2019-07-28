package com.kechuang.www.kc.HelpView;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by lyq on 2018/9/20.
 */

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"dbUser1.db",null,2);
        System.out.println("-------MyHelper--------");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("--------onCreate--------");
        db.execSQL("Create table IF NOT EXISTS tOrder(_id  PRIMARY KEY AUTOINCREMENT,orderType VARCHAR(20),orderDetail VARCHAR(100),orderTitle VARCHAR(100),pubId VARCHAR(20),accId VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("--------onUpgrade-------");
    }
}
