package a_evan.zhku.pnt_v2;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;

import static com.github.mikephil.charting.components.YAxis.YAxisLabelPosition.INSIDE_CHART;


public class DayFragment extends Fragment implements View.OnClickListener{

    private View v;
    private LineChart mChart;

    private int InitDate = 0, ChartDataSet = 0;
    private int[] mColors ;

    private List<Map<String, Object>> Daysdata;

    private String mDays[], cDate[],  y1[],  y2[];
    private String chooseYear = "2019";

    private TextView mTvDFrag_SelectedMonth, mTvDFrag_SelectedYear, tvMonEarn, tvMonCost;
    private ArrayList<Entry> yValsEarn, yValsCost;//  = new ArrayList<Entry>();
    private int uid;

    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_day, container, false);

        initView();

        Intent getId = getActivity().getIntent();
        uid = getId.getIntExtra("UserId",-1);


        //初始化时间显示栏
        firstChooseDate();

        //数据获取
        mColors = new int[]{  Color.parseColor("#E11446F0"),    //蓝色收入
                              Color.parseColor("#E4B388FF")  };   //紫色支出
        Daysdata = getData(cDate[0], cDate[1]);      //每一条账目的集合
        mDays = new String[Daysdata.size()];        //多少天 --用于设置x轴

        //确定x轴数据
        makeXdata();
        
        //确定y轴数据
        makeYdata();

        //刷新第三栏
        refreshTvEarnCost();

//        int i =0;
//        for(Map <String,Object> mday : Daysdata){
//            //payment_type, record_date ,money,
//            Log.w("payment_type_" + i +":",  mday
//                    .get("payment_type").toString());   //Daysdata.get(i).get("payment_type").toString());
//            Log.w("record_date_" + i +":",   mday.get("record_date").toString());   //Daysdata.get(i).get("record_date").toString());
//            Log.w("money_"  + i +":",  mday.get("money").toString());   // Daysdata.get(i).get("money").toString());
//            Log.w("------------","---------------------------");
//            i++;
//        }


        //插入xy数据并绘制图表
        setEmptyLineData();

        return v;
    }

    private void initView() {

        mTvDFrag_SelectedYear = v.findViewById(R.id.TvDayFragYearTitle);
        mTvDFrag_SelectedMonth = v.findViewById(R.id.TvDayFragMonthTitle);
        tvMonEarn = v.findViewById(R.id.TvMoneyDayEarn);
        tvMonCost = v.findViewById(R.id.TvMoneyDayCost);
        mChart = (LineChart) v.findViewById(R.id.linechart);

        mTvDFrag_SelectedYear.setOnClickListener(this);
        mTvDFrag_SelectedMonth.setOnClickListener(this);
//        tvMonEarn.setOnClickListener(this);
//        tvMonCost.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //第二栏月份选择处理
            case R.id.TvDayFragMonthTitle:
                showPopupMenu(v, R.menu.month, mTvDFrag_SelectedMonth);

                break;

            //第二栏年份选择处理
            case R.id.TvDayFragYearTitle:

                showPopupMenu(v, R.menu.year, mTvDFrag_SelectedYear);
//                Log.w("更新后的年：",mTvDFrag_SelectedYear.getText().toString().split("年")[0]);
//                Log.w("更新后的月：",mTvDFrag_SelectedMonth.getText().toString().split("月")[0]);
                break;

//            case R.id.TvMoneyEarn:
//
//                break;
//
//            case R.id.TvMoneyCost:
//
//                break;


        }

    }


    private void makeYdata() {

        y1 = new String[mDays.length];
        y2 = new String[mDays.length];

        for(int i = 0 ; i < mDays.length; i++){

            for(Map <String,Object> mday : Daysdata){
                //第一天的 xi= 0
                if (mday.get("record_date").toString().equals(chooseYear+ "-" + mDays[i])){

                    //200.0,20.0,10.0
                    String sntp[] = mday.get("money").toString().split(",");
                    double dmoney = 0.0;
                    for(String s : sntp)
                        dmoney += Double.valueOf(s);

                    if(mday.get("payment_type").toString().equals("收入")){ //y值为收入类型

                        y1[i] = String.valueOf(dmoney);
                        y2[i] = "0.0";


                    }else{  //y值为支出类型
                        y1[i] = "0.0";
                        y2[i] = String.valueOf(dmoney);
                    }
                }
                else
                    continue;//进入下一天

            }
        }
//        Log.w("y1轴数据：",Arrays.toString(y1));
//        Log.w("y2轴数据：",Arrays.toString(y2));

    }

    private void makeXdata() {

        String arrdays[] = new String[Daysdata.size()];
        int ida = 0;
        for(Map <String,Object> mday : Daysdata){
            arrdays[ida] = mday.get("record_date").toString();
            ida++;
        }

        //日期去重
        Map<String, Object> map = new HashMap<String, Object>();
        for (String str : arrdays) {
            map.put(str, str);
        }

        //返回不含有重复元素的数组
        mDays =  map.keySet().toArray(new String[1]);
//        System.out.println(Arrays.toString(mDays));
//        Log.w("x轴数据：",Arrays.toString(mDays));


        Log.w("mDaysleng", String.valueOf(mDays.length));
        Log.w("mDays[0]",mDays[0]);



        //对得到的日期字符串数组进行排序
        if(mDays[0].equals("") || mDays[0].equals(null)){
            mDays = new String[]{getCurrentDate()};
            Log.w("mDaysleng", String.valueOf(mDays.length));
//            mDays[0] = getCurrentDate();

        }

        Log.w("mDaysleng1", String.valueOf(mDays.length));
        Log.w("mDays[0]1",mDays[0]);


        Date dates[] = new Date[mDays.length];
        Log.w("datesleng", String.valueOf(dates.length));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");




        try {

            for (int i = 0; i < dates.length; i++)
                dates[i] = sdf.parse(mDays[i]);

            Arrays.sort(dates);

            //将排好序的日期赋值给原days数组
            for (int i = 0; i < mDays.length; i++) {
                String sds[] = sdf.format(dates[i]).split("-");
                mDays[i] = sds[1] + "-" + sds[2];

                if (i == 0)
                    chooseYear = sds[0];

            }
//            Log.w("x轴数据：",sdf.format(dates[0]));
//            Log.w("x轴数据：",sdf.format(dates[1]));
//            Log.w("x轴数据：",sdf.format(dates[2]));
//        Log.w("x轴数据：",Arrays.toString(mDays));

        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void firstChooseDate() {

        //初始化日期栏  2019‐06‐29
        if (InitDate == 0) {
            cDate = getCurrentDate().split("‐");
        }

        mTvDFrag_SelectedYear.setText(cDate[0] + "年");
        mTvDFrag_SelectedMonth.setText(cDate[1] + "月");
        
        
    }


    private void addDataSet(ArrayList<Entry> entryList, String dataSetName) {

        LineData data = mChart.getData();

        if (data != null) {
            int count = data.getDataSetCount();

            LineDataSet set = new LineDataSet(entryList, dataSetName);
            set.setLineWidth(1.5f);
            set.setCircleRadius(3.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawValues(true);    //节点显示具体数值
            set.setValueTextColor(color);
            set.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set.setAxisDependency(YAxis.AxisDependency.LEFT);    //如果使用的是右坐标轴必须设置这行
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);

            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart.setVisibleXRangeMaximum(6);
            mChart.invalidate();
        }


    }



    private void initChartView() {


        mChart.setDrawGridBackground(false);
        mChart.setDescription(null);    //右下角说明文字
        mChart.setDrawBorders(true);    //四周是不是有边框
        mChart.setBorderWidth(0.5f);
        mChart.setBorderColor(Color.parseColor("#b3b3b3"));    //边框颜色，默认黑色？
//        mChart.setVisibleXRangeMaximum(4);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        //禁止x轴y轴同时进行缩放
        mChart.setPinchZoom(false);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);



        mChart.setBackgroundResource(R.mipmap.chartbg);

        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
//        xAxis.enableGridDashedLine(0f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.disableGridDashedLine();  //不显示背景虚线

//        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
//        xAxis.setAxisLineWidth(0f);


        xAxis.setGranularityEnabled(true);    //粒度
        xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //设置x轴的各个x值标签文字
                return mDays[(int) value % mDays.length];
            }


//            @Override
//            public int getDecimalDigits() {
//                return 0;
//            }
        });


//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(true);    //是不是显示轴上的刻度
        xAxis.setLabelCount(mDays.length);    //强制有多少个刻度
        xAxis.setTextColor(Color.parseColor("#b3b3b3"));   //纵轴末端提示字体颜色


        //隐藏左侧坐标轴显示右侧坐标轴，并对右侧的轴进行配置
//        mChart.getAxisLeft().setEnabled(false);

        mChart.getAxisLeft().setTextColor(Color.parseColor("#b3b3b3"));

        YAxis leftAxis = mChart.getAxisRight();
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisLineWidth(0f);
        leftAxis.setDrawAxisLine(false);
        //坐标轴绘制在图表的内侧
        leftAxis.setPosition(INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b3b3b3"));

        //好像有坐标轴enable的时候是不可用的
        leftAxis.setSpaceBottom(10f);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart.setData(new LineData());


    }



    //获取列表数据
    private List<Map<String, Object>> getData(String year, String month) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

//            String sql = "SELECT * FROM mainData WHERE year(record_date) =" + year + "and month(record_date) =" + month;
//            Cursor cursor = db.rawQuery(sql, new String[]{year, month});

        String YYMM = year + "-" + month;


        String[] selectionArgs = new String[]{String.valueOf(uid),  "%" +  YYMM + "%"};//new String[]{ "%" +  "19-06" + "%"};
        String col[] = new String[]{"record_date", "payment_type","group_concat(money)"};

        Cursor cursor = db.query("mainData", col, " uid = ? and record_date like ? ", selectionArgs,
                "record_date, payment_type", null, "record_date", null);


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

//              nid, uid, payment_type, record_date , used_type ,money,notes icontype
                Map<String, Object> m = new HashMap<>();

//                m.put("payment_type", cursor.getString(2));
//                m.put("record_date", cursor.getString(3));
//                m.put("money", cursor.getString(5));

                //"record_date", "payment_type","group_concat(money)"
                m.put("record_date", cursor.getString(0));
                m.put("payment_type", cursor.getString(1));
                m.put("money", cursor.getString(2));

                list.add(m);
                cursor.moveToNext();

            }
        }

        cursor.close();
        db.close();

        return list;

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

                //更新图表
                refreshChart();

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

    private void refreshChart() {

        clearChart();

        //2019年
        //07月
        Daysdata = getData(mTvDFrag_SelectedYear.getText().toString().substring(0,4),
                           mTvDFrag_SelectedMonth.getText().toString().substring(0,2));      //每一条账目的集合

        //测试
        //Log.w("mDays.length:  ",String.valueOf(Daysdata.size()));

        if (Daysdata.size() == 0){
            clearChart();
            mChart.setNoDataText("当前日期没有任何记录");  //没有数据时显示的文字

//            更新第三栏金额
            tvMonCost.setText("0");
            tvMonEarn.setText("0");


//            Toast.makeText(getActivity(), "当前日期没有任何记录",Toast.LENGTH_SHORT).show();
        }else{

            mDays = new String[Daysdata.size()];        //多少天 --用于设置x轴

            //刷新x轴数据
            makeXdata();

            //刷新y轴数据
            makeYdata();

            refreshTvEarnCost();

            setEmptyLineData();

        }
    }

    private void refreshTvEarnCost() {
        //            更新第三栏金额
        double MonthEarn = 0;
        double MonthCost = 0;

        for (int j = 0; j < y1.length; j++){
            MonthEarn += Double.valueOf(y1[j]);
            MonthCost += Double.valueOf(y2[j]);

        }
        tvMonCost.setText(String.valueOf(MonthCost));
        tvMonEarn.setText(String.valueOf(MonthEarn));
    }


    // 清空图表
    public void clearChart() {
        mChart.clear();
    }


    // 为图表设置一个LineData
    public void setEmptyLineData() {

        //绘制图表
        initChartView();

        //      在这里处理数据
        //生成各个y的值
        yValsEarn = new ArrayList<>();
        for (int a = 0; a < mDays.length; a++) {
            yValsEarn.add(new Entry(a, Float.parseFloat(y1[a])));
        }

        yValsCost  = new ArrayList<>();
        for (int b = 0; b < mDays.length; b++) {
            yValsCost.add(new Entry(b, Float.parseFloat(y2[b])));
        }

        //插入点
        addDataSet(yValsEarn, "收入");
        addDataSet(yValsCost, "支出");

        //图标的下边的指示块  图例
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setXEntrySpace(40);

    }


    @Override public void onDetach() {
        super.onDetach();
        try {Field childFragmentManager = Fragment.class .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null); }
        catch (NoSuchFieldException e) { throw new RuntimeException(e); }
        catch (IllegalAccessException e) { throw new RuntimeException(e); }
    }


    public String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy‐MM‐dd");
        String CurrDate = df.format(new Date());
        return CurrDate;
    }



    //获取选中月份的天数
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


}
