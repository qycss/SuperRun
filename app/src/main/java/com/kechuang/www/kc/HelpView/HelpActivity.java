package com.kechuang.www.kc.HelpView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kechuang.www.kc.HelpedActivity;
import com.kechuang.www.kc.MainActivity;
import com.kechuang.www.kc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelpActivity extends AppCompatActivity {

    private ListView mLv;
    private TextView mTv;
    private Button mBtnHelped,mBtnPersonal;

    List<Order> list;
    MyHelper myHelper;
    SQLiteDatabase db;
    MyAdapter myAdapter;
    String strID;

    //

    private SwipeRefreshLayout refreshLayout;



//
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        mBtnHelped = findViewById(R.id.btn_main_2);
        mBtnPersonal = findViewById(R.id.btn_main_3);
        mBtnHelped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, HelpedActivity.class);
                startActivity(intent);
            }
        });
        mBtnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mTv = findViewById(R.id.tv_helped);
        mTv.setTypeface(Typeface.createFromAsset(getAssets(), "miaowu.ttf"));
        mTv.setEnabled(false);

        ///////////////////////////////////////
        mLv = findViewById(R.id.lv_help);
        list = new ArrayList<Order>();
        //创建MyHelper实例
        myHelper = new MyHelper(this);
        //得到数据库
        db = myHelper.getWritableDatabase();

        //查询数据
        Query();
        //创建MyAdapter实例
        myAdapter = new MyAdapter(this);
        mLv.setAdapter(myAdapter);


     //

     refreshLayout =  findViewById(R.id.refresh);
     refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
             android.R.color.holo_red_light, android.R.color.holo_orange_light);
     refreshLayout.setSize(SwipeRefreshLayout.LARGE);

     refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {

             // 开始刷新，设置当前为刷新状态
             refreshLayout.setRefreshing(true);

             // 这里是主线程
             // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
             // TODO 获取数据
             final Random random = new Random();
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                        list.clear();
                     //查询数据
                     Query();
                     //创建MyAdapter实例

                     myAdapter.notifyDataSetChanged();


                     Toast.makeText(HelpActivity.this, "刷新", Toast.LENGTH_SHORT).show();

                     // 加载完数据设置为不刷新状态，将下拉进度收起来
                     refreshLayout.setRefreshing(false);
                 }
             }, 1200);

              System.out.println(Thread.currentThread().getName());

             // 这个不能写在外边，不然会直接收起来
             refreshLayout.setRefreshing(false);
         }
     });




     //



     mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //方法1
                /*db = myHelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select * from tOrder",null);
                cursor.moveToPosition(position);
                String getid = cursor.getString(cursor.getColumnIndex("_id"));
                Toast.makeText(HelpActivity.this,getid,Toast.LENGTH_LONG).show();
*/
                //方法2：
                Intent intent1 = getIntent();
                String strID = intent1.getStringExtra("userid");
                String getorderid = list.get(position).getOrderId();
                Intent intent = new Intent(HelpActivity.this,ItemActivity.class);
                intent.putExtra("orderid",getorderid);
                intent.putExtra("userid",strID);
                startActivityForResult(intent,0);

            }
        });

    }

        ////////////////////////////////////////////////////////////////////////////////////


    class MyAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //从list中读取出order
            Order order = list.get(position);
            ViewHolder viewHolder= null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.layout_list_item_order,null);
                viewHolder.tvId = convertView.findViewById(R.id.tv_id);
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
                viewHolder.tvDetail = convertView.findViewById(R.id.tv_detail);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //向text中插入数据
            viewHolder.tvId.setText("订单号:"+order.getOrderId());
            viewHolder.tvTitle.setText(order.getOrderTitle());
            viewHolder.tvDetail.setText(order.getOrderDetail());
            return convertView;
        }
    }


    class ViewHolder{
        private TextView tvTitle;
        private TextView tvDetail;
        private TextView tvId;
    }

    //查询数据
    public void Query(){
        Cursor cursor = db.query("tOrder",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            Integer type = cursor.getInt(cursor.getColumnIndex("orderType"));
            String title = cursor.getString(cursor.getColumnIndex("orderTitle"));
            String detail = cursor.getString(cursor.getColumnIndex("orderDetail"));
            String pubid = cursor.getString(cursor.getColumnIndex("pubId"));
            String accid = cursor.getString(cursor.getColumnIndex("accId"));

            Order order = new Order(id,type,title,detail,pubid,accid);
            if(type==0){
                list.add(order);
            }
        }
        cursor.close();
    }



}

