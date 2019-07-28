package com.kechuang.www.kc;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PayActivity extends AppCompatActivity {

    private Button mBtnAli, mBtnWx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mBtnAli = findViewById(R.id.btn_ali);
        mBtnWx = findViewById(R.id.btn_wx);

        mBtnAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                if (checkPackInfo(getString(R.string.Alipay))) {
                    Intent intent = packageManager.getLaunchIntentForPackage(getString(R.string.Alipay));
                    startActivity(intent);
                } else {
                    Toast.makeText(PayActivity.this, "本机未安装支付宝！\n未能成功打开..." + getString(R.string.Alipay), Toast.LENGTH_LONG).show();
                }
            }
        });

        mBtnWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                if (checkPackInfo(getString(R.string.Wx))) {
                    Intent intent = packageManager.getLaunchIntentForPackage(getString(R.string.Wx));
                    startActivity(intent);
                } else {
                    Toast.makeText(PayActivity.this, "本机未安装微信！\n未能成功打开..." + getString(R.string.Wx), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
