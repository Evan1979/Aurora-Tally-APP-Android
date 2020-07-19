package a_evan.zhku.pnt_v2;


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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class YearFragment extends Fragment implements View.OnClickListener {

    private View v ;

    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    private String mXaxisDaysValue[];
    private String mYaxisDaysValue[];

    private View lineChooseYear;
    private TextView mTvYFrag_SelectedYear;


    public YearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_year, container, false);

        lineChooseYear = v.findViewById(R.id.linerYDateChoose);
        lineChooseYear.setOnClickListener(this);

        mTvYFrag_SelectedYear = v.findViewById(R.id.TvYEARFragYearTitle);

        barChart = v.findViewById(R.id.barChart);
        initBarChart(barChart);
//        BarChartBean barChartBean = LocalJsonAnalyzeUtil.JsonToObject(this,
//                "bar_chart.json", BarChartBean.class);

        List<Map<String,Object>> dataValueList = getData("收入");

        Log.w("dataValueList长度:" , String.valueOf(dataValueList.size()));   //Daysdata.get(i).get("payment_type").toString());

//        int i =0;
//        for (Map<String, Object> datemap : dataValueList ){
//
//            //payment_type, record_date ,money,
//            Log.w("payment_type_" + i +":",  datemap.get("payment_type").toString());   //Daysdata.get(i).get("payment_type").toString());
//            Log.w("record_date_" + i +":",   datemap.get("record_date").toString());   //Daysdata.get(i).get("record_date").toString());
//            Log.w("money_"  + i +":",  datemap.get("money").toString());   // Daysdata.get(i).get("money").toString());
//            Log.w("------------","---------------------------");
//            i++;
//
//        }

        mXaxisDaysValue = new String[dataValueList.size()];
        mYaxisDaysValue = new String[dataValueList.size()];
        for (int i = 0; i < dataValueList.size() ; i++) {
            mXaxisDaysValue[i] = dataValueList.get(i).get("record_date").toString();
            mYaxisDaysValue[i] = dataValueList.get(i).get("money").toString();

            Log.w(mXaxisDaysValue[i] + ": ",mYaxisDaysValue[i]);


        }
        showBarChart(dataValueList, "收入（元）", getResources().getColor(R.color.blue));

        return v;
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linerYDateChoose){
            showPopupMenu(v, R.menu.year, mTvYFrag_SelectedYear);
        }
    }


    public void showBarChart(List<Map<String,Object>> dataValueList, String name, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
//        for (int i = 0; i < dataValueList.size(); i++) {

        for (int i = 0; i < mXaxisDaysValue.length; i++) {

            /* 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            BarEntry barEntry = new BarEntry(i, Float.parseFloat(mYaxisDaysValue[i]));
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);   //#E11446F0

//        // 添加多个BarDataSet时
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//        BarData data = new BarData(dataSets);


    }


    //初始化BarChart图表
    private void initBarChart(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);
//        //设置动画效果
//        barChart.animateY(1000, Easing.Linear);
//        barChart.animateX(1000, Easing.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();

        //解决柱状图显示不完整
//        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }


    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);


        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
//        barDataSet.setValueTextSize(10f);
//        barDataSet.setValueTextColor(color);


        barChart.setBackgroundResource(R.mipmap.chartbg);

        barChart.setDrawBorders(false);

        //不显示右下角描述文字
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //禁用图表触摸事件
        barChart.setDoubleTapToZoomEnabled(false);
        //禁止拖拽
        barChart.setDragEnabled(false);
        //X轴或Y轴禁止缩放
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setScaleEnabled(false);
        //禁止所有事件
        barChart.setTouchEnabled(false);

//        mChart.setDrawValueAboveBar(true);//将Y数据显示在点的上方


        //设置单条柱状图宽度
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.2f);
        barChart.setData(data);



        //不显示X轴 Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);


        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        //右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f);


        //不显示左侧Y轴
        leftAxis.setEnabled(false);


//        xAxis.setAxisMinimum(0f);
//        xAxis.setAxisMaximum(xValues.size());
//        //将X轴的值显示在中央
//        xAxis.setCenterAxisLabels(true);

        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //设置x轴的各个x值标签文字
                return mXaxisDaysValue[(int) value % mXaxisDaysValue.length];
            }
        });


        //右侧Y轴自定义值
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //value就是原本要显示的值
                return ((int) (value)) + "(￥)" ;
            }

            // ValueFormatter

        });




    }



    //获取列表数据
    private List<Map<String, Object>> getData(String paymentType) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

//        对应sqlite语句
//        select strftime('%Y',record_date), sum(money),payment_type
//        from mainData
//        group by strftime('%Y',  record_date),payment_type
//        order by strftime('%Y',record_date)
//        String[] selectionArgs = new String[]{ "收入" };    //new String[]{ "%" +  "19-06" + "%"};
        String col[] = new String[]{  "strftime('%Y',record_date)", "sum(money)","payment_type"};

        Cursor cursor = db.query("mainData", col, null, null,
                "strftime('%Y',record_date),payment_type", null, "strftime('%Y',record_date)", null);  //, payment_type

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if(cursor.getString(2).equals(paymentType)){
                    //              nid, uid, payment_type, record_date , used_type ,money,notes icontype
                    Map<String, Object> m = new HashMap<>();

                    //"record_date", "payment_type","group_concat(money)"
                    m.put("record_date", cursor.getString(0));
                    m.put("money", cursor.getString(1));
                    m.put("payment_type", cursor.getString(2));

                    list.add(m);
                }
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
//                refreshChart();

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

//        clearChart();
//
//        //2019年
//        //07月
//        Daysdata = getData(mTvDFrag_SelectedYear.getText().toString().substring(0,4),
//                mTvDFrag_SelectedMonth.getText().toString().substring(0,2));      //每一条账目的集合
//
//        //测试
//        //Log.w("mDays.length:  ",String.valueOf(Daysdata.size()));
//
//        if (Daysdata.size() == 0){
//            clearChart();
//            mChart.setNoDataText("当前日期没有任何记录");  //没有数据时显示的文字
//
////            更新第三栏金额
//            tvMonCost.setText("0");
//            tvMonEarn.setText("0");
//
//
////            Toast.makeText(getActivity(), "当前日期没有任何记录",Toast.LENGTH_SHORT).show();
//        }else{
//
//            mDays = new String[Daysdata.size()];        //多少天 --用于设置x轴
//
//            //刷新x轴数据
//            makeXdata();
//
//            //刷新y轴数据
//            makeYdata();
//
//            refreshTvEarnCost();
//
//            setEmptyLineData();

//        }
    }


}
