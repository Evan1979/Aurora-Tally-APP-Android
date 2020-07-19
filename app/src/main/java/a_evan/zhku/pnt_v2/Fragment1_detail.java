package a_evan.zhku.pnt_v2;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;


/**
 * Fragment1_detail class
 *
 * @author evan1997
 * @date 2019/07/01
 */
public class Fragment1_detail extends Fragment {


    private TextView mTvSelectedMonth;
    private TextView mTvSelectedYear;
    private View linerDate;

    private TextView tvEarn;
    private TextView tvCost;

    private Button btnSarch;

    RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter;
    RecyclerView mReView;
    DatabaseHelper dbHelper;
    String LastSameDisDate = " ";
    String DisType = " ";
    String[] cDate;
    List<Map<String, Object>> data;
    int Dis = 0;
    Double Dearn = 0.0;
    Double Dcost = 0.0;
    int iDisPartCalcute = 0;
    int uid = -1;


    public Fragment1_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_frag1, container, false);


        dbHelper = new DatabaseHelper(v.getContext());
        Intent getId = getActivity().getIntent();
        uid = getId.getIntExtra("UserId",-1);

        //测试
        Log.w("用户ID：", String.valueOf(uid));

        tvEarn = v.findViewById(R.id.TvMoneyEarn);
        tvCost = v.findViewById(R.id.TvMoneyCost);
        mTvSelectedYear = v.findViewById(R.id.TvYearTitle);
        mTvSelectedMonth = v.findViewById(R.id.TvMonthTitle);
        linerDate = v.findViewById(R.id.linerDate);




        //初始化日期栏  2019‐06‐29
        if (Dis == 0) {
            cDate = getCurrentDate().split("‐");
//        Log.w("警告处", getCurrentDate());
//        Log.w("数组长度", String.valueOf(cDate.length));
            data = getData(cDate[0], cDate[1]);  //getData(dbHelper);

        }


        linerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        Toast.makeText(getContext(),getTime(date) , Toast.LENGTH_SHORT).show();
                        //2019-04-05

                        String[] d = getTime(date).split("-");


                        mTvSelectedYear.setText(d[0]+"年");
                        mTvSelectedMonth.setText(d[1]+ "月");
//                        et_Date.setText(getTime(date));
                        refreshList();

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
//                .setRangDate(startDate,endDate)//起始终止年月日设定
//                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true)//是否显示为对话框样式

                        .build();

                pvTime.show();

            }
        });



//        //第二栏日期处理


//        mTvSelectedYear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v, R.menu.year, mTvSelectedYear);
//
////                Log.w("更新后的年：",mTvSelectedYear.getText().toString().split("年")[0]);
////                Log.w("更新后的月：",mTvSelectedMonth.getText().toString().split("月")[0]);
//
//
//
//            }
//        });
//
//        mTvSelectedMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v, R.menu.month, mTvSelectedMonth);
//
//            }
//        });
//        mTvSelectedYear.setText(cDate[0] + "年");
//        mTvSelectedMonth.setText(cDate[1] + "月");


        //浮动按钮
        FloatingActionButton fab = v.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //处理添加项目
                Intent it = new Intent(getActivity(),AddBookkeepingAct.class);
//                    it.setClass(MainUserActivity.this, AddBookkeepingAct.class);
                it.putExtra("update","0");
                it.putExtra("uid",uid);


                startActivity(it);
//                getActivity().finish();
//                Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_LONG).show();

            }
        });


        //搜索按钮
        btnSarch = v.findViewById(R.id.btnSearch);
        btnSarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SearchIntent = new Intent(getActivity(), SearchActivity.class);
                SearchIntent.putExtra("uid",uid);
                startActivity(SearchIntent);

            }
        });


        //        列表操作
        mReView = v.findViewById(R.id.recycler);
        mReView.setHasFixedSize(true);                  //设置每行的固定大小

        //下面可能有问题
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());//定义线性布局设置
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直方向放置每一项

        mReView.setLayoutManager(mLayoutManager);
        mAdapter = new Fragment1_detail.MyAdapter(data);
        mReView.setAdapter(mAdapter);

        return v;
    }



    //RecyclerView适配器
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        List<Map<String, Object>> mData;

        public MyAdapter(List<Map<String, Object>> data) {
            mData = data;    //构造函数
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {

            Context context = parent.getContext();
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.list_item, parent, false);
            //上面通过布局文件生成ViewHolder

            ViewHolder vh = new ViewHolder(view);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(getContext(),"你长按了该行",Toast.LENGTH_SHORT);

                    Log.w("你长按了该行:","1");
                    return false;
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //处理点击选中行的事件

//                    Log.i("提示：", "你点击了该行");
                    Map<String, Object> Iitem = (Map<String, Object>)v.getTag();
                    Intent it = new Intent(getActivity(),AddBookkeepingAct.class);
//                    it.setClass(MainUserActivity.this, AddBookkeepingAct.class);


//                    int paymentType = -1;
//                    if (Iitem.get("payment_type").toString().equals("支出"))
//                        paymentType = 0;
//                    else
//                        paymentType = 1;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("updateObject",(Serializable)Iitem);
//                    bundle.putInt("Ptype",paymentType);
                    bundle.putString("update","1");
//                    bundle.putString("uid","1");
                    it.putExtras(bundle);

                    startActivity(it);
//                    getActivity().finish();


                }
            });

            return vh;   //生成ViewHolder对象
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder vh, int position) {  //设置现实数据

            Map<String, Object> item = mData.get(position);

//          payment_type, record_date , used_type ,money,notes
            String CurrentDate = item.get("record_date").toString();


            //测试
//            Log.w("每一行的日期：",CurrentDate);

            String used_money = null;
            String money = item.get("money").toString();
            String pmt = item.get("payment_type").toString();
            if (pmt.equals("收入")) {
                used_money = "+" + money;
                Dearn += Double.valueOf(money);
            }
            else {
                used_money = "-" + money;
                Dcost += Double.valueOf(money);

            }
////            设置相同日期的不要显示日期栏
            if (Dis == 0) {
                LastSameDisDate = CurrentDate;
                Dis = 1;

                DisType = pmt;
//                Log.w("DisDate第一次:", LastSameDisDate);
                vh.View_linerDis_DateMoney.setVisibility(View.VISIBLE);

            } else {


                if ( LastSameDisDate.equals(CurrentDate) ){
                    if (DisType.equals(pmt)){
                        //d1 = d2, p1= p1  隐藏
//                        Dearn += Double.valueOf(money);

                        if (iDisPartCalcute == 0)
                            iDisPartCalcute = position;


                        vh.View_linerDis_DateMoney.setVisibility(View.GONE);
                    }else{


//                        if (pmt.equals("收入")) {
//                            removeData(iDisPartCalcute);
//                            Map<String,Object> mo = new HashMap<>();
//                            mo.put("money",Dearn);
//                            addData(iDisPartCalcute,mo);
//                        }
//                        else {
//                            removeData(iDisPartCalcute);
//                            Map<String,Object> mo = new HashMap<>();
//                            mo.put("money",Dearn);
//                            addData(iDisPartCalcute,mo);
//                        }
//

                        //d1 = d2, p1 ！= p1  显示
                        vh.View_linerDis_DateMoney.setVisibility(View.VISIBLE);
//                        Dearn = Double.valueOf(money);

                        DisType = pmt;
                    }
                }
                else {
                    //d1 ！= d2, p1= p1  显示

                    if (!DisType.equals(pmt))
                        //d1 ！= d2, p1 ！= p1  显示
                        DisType = pmt;

                    vh.View_linerDis_DateMoney.setVisibility(View.VISIBLE);
                    LastSameDisDate = CurrentDate;
                }



//                if (! (LastSameDisDate.equals(CurrentDate)  &&  DisType.equals(pmt) )) {
//                    vh.View_linerDis_DateMoney.setVisibility(View.VISIBLE);
//                    LastSameDisDate = CurrentDate;
//                }

            }

            //数据绑定，设置position所在行的数据项
            vh.tvTotalMoney.setText(pmt + ":" + money);
            vh.tvDate.setText(CurrentDate);
            vh.tvInfo.setText(item.get("used_type").toString());
            vh.tvUsedMoney.setText(used_money);


//            Log.w("payment_type", Iitem.get("payment_type").toString());


            //设置类型图标
            String imaTypeName = item.get("icontype").toString();
            switch (imaTypeName) {

                case "0":
                    //使用默认图片
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.lifemoney);
                    break;
                case "eat":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.eat);
                    break;
//
                case "shop":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.shop);
                    break;

                case "1":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.eat);
                    break;

                case "2":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.store);
                    break;

                case "3":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.dinner);
                    break;

                case "4":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.fruit);
                    break;

                case "5":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.t3);
                    break;

                case "6":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.shop);
                    break;

                case "7":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.take_out);
                    break;

                case "8":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.protect);
                    break;


                case "9":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.lifemoney);
                    break;

                case "10":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.healt);
                    break;

                case "11":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.phone);
                    break;

                case "12":
                    vh.iv_imaTypeIcon.setImageResource(R.drawable.photo);
                    break;


            }

//            Log.w("检查position :" , String.valueOf(position));
//            Log.w("检查Iitem :" , String.valueOf(Iitem));
//            Log.w("检查Iearn :" , String.valueOf(Dearn));
//            Log.w("检查Icost :" , String.valueOf(Dcost));

            //更新第二栏该月的收入与支出金额
            tvEarn.setText(String.valueOf(Dearn));
            tvCost.setText(String.valueOf(Dcost));

            vh.itemView.setTag(item);
            //设置ViewHolder的保存对象，用作点击事件触发后提取点击行所对应的数据项

        }


        public void addData(int position,Map<String,Object> oj) {
            mData.add(position, oj);
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //内部类ViewHolder,方便独立封装
            public ImageView iv_imaTypeIcon;
            public TextView tvTotalMoney;
            public TextView tvDate;
            public TextView tvInfo;
            public TextView tvUsedMoney;
            public View View_linerDis_DateMoney = null;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                //构造函数，保存item_list.xml中的控件对象h
                tvTotalMoney = itemView.findViewById(R.id.tv_total_money);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvInfo = itemView.findViewById(R.id.tv_info);
                iv_imaTypeIcon = itemView.findViewById(R.id.ima_type_icon);
                tvUsedMoney = itemView.findViewById(R.id.tv_used_money);
                View_linerDis_DateMoney = itemView.findViewById(R.id.linerDate_TMoney);

            }
        }

    }


    //获取列表数据
    private List<Map<String, Object>> getData(String year, String month) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

//            String sql = "SELECT * FROM mainData WHERE year(record_date) =" + year + "and month(record_date) =" + month;
//            Cursor cursor = db.rawQuery(sql, new String[]{year, month});

        String YYMM = year + "-" + month;

        Cursor cursor = db.query("mainData", null, "uid = ? and record_date like ? ", new String[]{String.valueOf(uid), "%" +  YYMM + "%"},
                null, null, "record_date", null);


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

    @Override
    public void onResume() {
        super.onResume();
    }


    private void showPopupMenu(View view, int menuResour, TextView v) {

        int menuRes = menuResour;
        final TextView tvDisChoose = v;


        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                tvDisChoose.setText(item.getTitle());

                refreshList();

                return false;
            }
        });


        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getActivity(), "选中:" + tvDisChoose.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }

    private void refreshList() {

        tvEarn.setText("0");
        tvCost.setText("0");

        Dearn = 0.0;
        Dcost = 0.0;

        data =  getData(mTvSelectedYear.getText().toString().split("年")[0]  ,  mTvSelectedMonth.getText().toString().split("月")[0]);
        MyAdapter adapter = (MyAdapter) mReView.getAdapter();
        adapter.mData = data;
        adapter.notifyDataSetChanged();

    }


    public String getCurrentDate() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy‐MM‐dd");
        String CurrDate = df.format(new Date());
        return CurrDate;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }



}
