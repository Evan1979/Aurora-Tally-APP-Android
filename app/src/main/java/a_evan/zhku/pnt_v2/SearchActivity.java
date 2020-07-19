package a_evan.zhku.pnt_v2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter;
    private RecyclerView mReView;

    private View vChooseType;

    private WheelView wheelView;

    private ImageView imgClearType;
    private ImageView imgClearDate;

    private Button btnTypeSure;
    private Button btnTypeQuit;

//    private EditText etPayType;
    private EditText etUsedType;
    //    private EditText etNote;
    private EditText etDate;
    private ImageView btnSearch;

    //    private String PayType = "payment_type";
    private String UsedType;  //UsedType = "used_type"  Date = "record_date"
    //    private String Note = "notes";
    private String Date;

    private List<String> mOptionsItems;
    private List<Map<String, Object>> data;

    private int uid = -1;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        UsedType = "used_type";
        Date = "record_date";


        //初始化类型数据
        mOptionsItems = new ArrayList<>();
        addTypeArrData();

        Intent getData = getIntent();
        uid = getData.getIntExtra("uid",0);
        Toast.makeText(this, String.valueOf(uid),Toast.LENGTH_SHORT).show();

        mReView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//定义线性布局设置
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直方向放置每一项

//        data = getData(PayType, UsedType, Note, Date);
        data = getData(UsedType, Date);
        mReView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(data);
        mReView.setAdapter(mAdapter);


        wheelView.setCyclic(false);
        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(SearchActivity.this, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();
                UsedType = mOptionsItems.get(index);
            }
        });

    }

    private void initView() {

//        etPayType = findViewById(R.id.etSeekPayType);
        etUsedType = findViewById(R.id.etSeekType);
//        etNote = findViewById(R.id.etSeekNote);
        etDate = findViewById(R.id.etSeekCalender);
        mReView = findViewById(R.id.SearchRecycler);
        wheelView = findViewById(R.id.wheelview);
        vChooseType = findViewById(R.id.ReaLayoutTypeView);

        imgClearType = findViewById(R.id.imgClearType);
        imgClearDate = findViewById(R.id.imgClearDate);

        btnSearch = findViewById(R.id.btnSearCh);
        btnTypeSure = findViewById(R.id.btnSeaSure);
        btnTypeQuit = findViewById(R.id.btnSeaQuit);

        imgClearType.setOnClickListener(this);
        imgClearDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        etUsedType.setOnClickListener(this);
        etDate.setOnClickListener(this);
        btnTypeSure.setOnClickListener(this);
        btnTypeQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.etSeekType:
                vChooseType.setVisibility(View.VISIBLE);

                break;

            case R.id.etSeekCalender:

                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(SearchActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        Toast.makeText(SearchActivity.this, getTime(date) , Toast.LENGTH_SHORT).show();
                        //2019-04-05
                        String d = getTime(date).substring(0,7);
                        etDate.setText(d);
                    }
                }).setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
                        .setContentTextSize(18)
                        .setTitleSize(15)//标题文字大小
                        .setTitleText("选择日期")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(Calendar.s,endDate)//起始终止年月日设定
//                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.show();

                break;


            case R.id.imgClearType:
                etUsedType.setText("");
                break;

            case R.id.imgClearDate:
                etDate.setText("");
                break;

            case R.id.btnSearCh:

//                if (!etPayType.getText().toString().equals(null))
//                    PayType = etPayType.getText().toString();

                if (! (etUsedType.getText().toString().equals(null) || etUsedType.getText().toString().equals("")  ) )
                    UsedType  = etUsedType.getText().toString();


//                if (!etNote.getText().toString().equals(null))
//                    Note = etNote.getText().toString();

                if (! (etDate.getText().toString().equals(null) || etDate.getText().toString().equals("")) )
                    Date = etDate.getText().toString();

//                data = getData(PayType, UsedType, Note, Date);
//                Log.w("进入判断-2:", UsedType );
//                Log.w("进入判断-1:", Date);

                data = getData( UsedType, Date);
                UsedType = "used_type";
                Date = "record_date";

                SearchActivity.MyAdapter adapter = (SearchActivity.MyAdapter) mReView.getAdapter();
                adapter.mData = data;
                adapter.notifyDataSetChanged();

                break;

            case R.id.btnSeaSure:
                vChooseType.setVisibility(View.GONE);
                etUsedType.setText(UsedType);
                break;

            case R.id.btnSeaQuit:
                vChooseType.setVisibility(View.GONE);
                break;
        }

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        List<Map<String, Object>> mData;
        public MyAdapter(List<Map<String, Object>> data) {
            mData = data;    //构造函数
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            Context context = parent.getContext();
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.search_list_item,parent,false);
            //上面通过布局文件生成ViewHolder


            ViewHolder vh = new ViewHolder(view);
            return vh;   //生成ViewHolder对象
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder vh, int position) {  //设置现实数据

            Map<String, Object> item = mData.get(position);

//          img; tvDate; tvPayment; tvUsedType; tvMoney;
//            vh.img.setImageResource(R.drawable.stylebtntype);


//          nid, uid, payment_type, record_date , used_type ,money,notes icontype

            //数据绑定，设置position所在行的数据项
            //tvDate; tvPayment; tvUsedType; tvMoney; tvNote;

            if (!item.get("record_date").equals(null))
                vh.tvDate.setText(item.get("record_date").toString());

            if (!item.get("payment_type").equals(null))
                vh.tvPayment.setText(item.get("payment_type").toString()) ;

            if (!item.get("used_type").equals(null))
                vh.tvUsedType.setText(item.get("used_type").toString());


            if (!item.get("money").equals(null))
                vh.tvMoney.setText(item.get("money").toString());

            if (!item.get("notes").equals(null))
                vh.tvNote.setText(item.get("notes").toString());


            vh.itemView.setTag(item);
            //设置ViewHolder的保存对象，用作点击事件触发后提取点击行所对应的数据项

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            //内部类ViewHolder,方便独立封装
//            public ImageView img;
            public TextView tvDate;
            public TextView tvPayment;
            public TextView tvUsedType;
            public TextView tvMoney;
            public TextView tvNote;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                //构造函数，保存item_list.xml中的控件对象

//                img = itemView.findViewById(R.id.ima_seek_type_icon);
                tvDate = itemView.findViewById(R.id.tv_seek_date);
                tvPayment = itemView.findViewById(R.id.tv_seek_payment_type);
                tvUsedType = itemView.findViewById(R.id.tv_seek_usedType);
                tvMoney = itemView.findViewById(R.id.tv_seek_used_money);
                tvNote = itemView.findViewById(R.id.tvDisTip);



            }

        }

    }


    private void addTypeArrData() {
        //      吃饭  超市  娱乐  水果  礼物  购物 外卖  护理  献爱心   医疗   通讯费用   拍照相片  兼职  工资  生活费
        mOptionsItems.add("吃饭");
        mOptionsItems.add("超市");
        mOptionsItems.add("娱乐");
        mOptionsItems.add("水果");
        mOptionsItems.add("礼物");
        mOptionsItems.add("购物");
        mOptionsItems.add("外卖");
        mOptionsItems.add("护理");
        mOptionsItems.add("献爱心");
        mOptionsItems.add("医疗");
        mOptionsItems.add("通讯费用");
        mOptionsItems.add("拍照相片");
        mOptionsItems.add("兼职");
        mOptionsItems.add("工资");
        mOptionsItems.add("生活费");
    }


    private List<Map<String, Object>> getData(String usedType,  String date) {  //String payType, String usedType, String note, String date


        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseHelper dbhelper = new DatabaseHelper(SearchActivity.this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();

//            String sql = "SELECT * FROM mainData WHERE year(record_date) =" + year + "and month(record_date) =" + month;
//            Cursor cursor = db.rawQuery(sql, new String[]{year, month});

//        payment_type   record_date used_type
//        String selection = "payment_type =? and record_date =? and used_type =? and notes like ?";
//        String []selectionArgs = new String[]{payType, date, usedType, "%" + note  + "%" };
//        " record_date like ? ", new String[]{ "%" +  YYMM + "%"}

//        Cursor cursor = db.query("mainData", null, selection, selectionArgs,
//                null, null, "record_date", null);


//        String []selectionArgs = {payType, date, usedType, note};
//        String []selectionArgs = {date, usedType};
//        Log.w("payType：",payType);
//        Log.w("date：",date);
//        Log.w("usedType：",usedType);
//        Log.w("note：",note);
//
//        Log.w("selectionArgs0：",selectionArgs[0]);
//        Log.w("selectionArgs1：",selectionArgs[1]);
//        Log.w("selectionArgs2：",selectionArgs[2]);
//        Log.w("selectionArgs3：",selectionArgs[3]);

        String sql ;//= "select * from mainData";
        String []selectionArgs =  new String[]{};

//        Log.w("进入判断0:", usedType + "   " + date);

        if (i == 0) {
            sql = "select * from mainData where payment_type = payment_type and record_date =record_date and used_type =used_type and notes = notes order by record_date";
            i++;
        }
        else{
//            sql = "select * from mainData where payment_type = ? and record_date =? and used_type =? and notes = ?";
            if (usedType.equals("used_type")) {
                if (date.equals("record_date")){
                    sql = "select * from mainData order by record_date";
                    selectionArgs =null;
//                    Log.w("进入判断1:", usedType + "   " + date);
                }
                else{
                    sql = "select * from mainData where  record_date like ?   order by record_date";
                    selectionArgs = new String[]{"%" + date  + "%"};
//                    Log.w("进入判断2:", usedType + "   " + date);

                }

            }
            else{

                if (date.equals("record_date")){
                    sql = "select * from mainData where used_type =?  order by record_date";
                    selectionArgs = new String[]{ usedType};
//                    Log.w("进入判断3:", usedType + "   " + date);

                }
                else{
                    sql = "select * from mainData where  record_date like ? and used_type =?  order by record_date";
                    selectionArgs = new String[]{"%" + date  + "%", usedType};
//                    Log.w("进入判断4:", usedType + "   " + date);
                }
            }
        }


//        String sql = "select * from mainData where payment_type = ? and record_date =? and used_type =? and notes = ?";
        Cursor cursor = db.rawQuery(sql, selectionArgs );

//        Toast.makeText(SearchActivity.this, cursor.getCount(),Toast.LENGTH_SHORT).show();
        Log.w("数据大小：",String.valueOf(cursor.getCount()));


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

//              nid, uid, payment_type, record_date , used_type ,money,notes icontype
                Map<String, Object> m = new HashMap<>();
                m.put("nid",cursor.getString(0));
                m.put("uid",cursor.getString(1));
                m.put("payment_type", cursor.getString(2));
                m.put("record_date", cursor.getString(3));
                m.put("used_type", cursor.getString(4));
                m.put("money", cursor.getString(5));
                m.put("notes", cursor.getString(6));

//              加载使用类型图标
                m.put("icontype", cursor.getString(7));

                list.add(m);
                cursor.moveToNext();

            }
        }

        cursor.close();
        db.close();

        return list;

    }


    private String getTime(java.util.Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


}
