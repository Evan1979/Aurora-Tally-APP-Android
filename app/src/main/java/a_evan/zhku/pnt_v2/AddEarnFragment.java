package a_evan.zhku.pnt_v2;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEarnFragment extends Fragment implements View.OnClickListener{

    View v;
    private View ViewLinearTool = null;
    private View ViewLReaChooseType;

    EditText et_Date;
    EditText et_Type;
    EditText et_Note;
    EditText et_Money;


    ImageView img[] = new ImageView[3];
    WheelView wheelViewAddEarnType;


    Button btn_Sure;

    Button btntypeSure;
    Button btntypeQuit;


    private List<String> mTypeItems;
    Map<String,Object> mo ;


    int ids[] = new int[3];
    int uid = -1;
    int nid = -1;
    int IconType = 0;
    private String used_Type = "兼职";

    public AddEarnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_earn, container, false);
        initView();


        //初始化类型数据
        mTypeItems = new ArrayList<>();
        addTypeArrData();

        Intent inAddEarn = getActivity().getIntent();
        uid = inAddEarn.getIntExtra("uid",-1);
        Toast.makeText(getActivity(),"uid" + uid,Toast.LENGTH_SHORT ).show();

//        String doType = inAddEarn.getStringExtra("update");
//        if(doType.equals("1")) {
//            DisUpdateTool();
//            nid = Integer.valueOf(mo.get("nid").toString());
//            Log.w("更新传过来的nid：",mo.get("nid").toString());
//        }


        wheelViewAddEarnType.setCyclic(false);
        wheelViewAddEarnType.setAdapter(new ArrayWheelAdapter(mTypeItems));
        wheelViewAddEarnType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(getContext(), "" + mTypeItems.get(index), Toast.LENGTH_SHORT).show();
                used_Type = mTypeItems.get(index);
            }
        });


        return v;
    }




    private void initView() {

        ids = new int[]{R.id.btnEarnType_2, R.id.btnEarnType_7, R.id.btnEarnType_9};
//        R.id.btnType_4,  R.id.btnType_5, R.id.btnType_6, R.id.btnType_7, R.id.btnType_8, R.id.btnType_9, R.id.btnType_10, R.id.btnType_11, R.id.btnType_12

        et_Type = v.findViewById(R.id.etEarnType);
        et_Note = v.findViewById(R.id.etEarnNote);
        et_Date = v.findViewById(R.id.etEarnCalender);
        et_Money = v.findViewById(R.id.etEarnMoney);
        ViewLinearTool = v.findViewById(R.id.linearEarnAddTool);
        ViewLReaChooseType = v.findViewById(R.id.ReaLayoutEarnTypeView);

        wheelViewAddEarnType = v.findViewById(R.id.wheelviewaddEarntype);

        btn_Sure = v.findViewById(R.id.btnEarnSure);
        btntypeSure = v.findViewById(R.id.btnEarnTypeSure);
        btntypeQuit = v.findViewById(R.id.btnEarnTypeQuit);

        //初始化图片类型标签
        for (int i= 0; i < 3; i++){
            img[i] = v.findViewById(ids[i]);
            img[i].setOnClickListener(this);
        }

        et_Type.setOnClickListener(this);
        et_Date.setOnClickListener(this);
        btn_Sure.setOnClickListener(this);
        btntypeSure.setOnClickListener(this);
        btntypeQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        //监听按钮点击事件
        switch (v.getId()){
            case R.id.btnEarnType_2 :
                IconType = 2;
                DisplayLinearAddTool("兼职");
                break;

            case R.id.btnEarnType_7 :

                IconType = 7;
                DisplayLinearAddTool("工资");
                break;


            case R.id.etEarnType:
                ViewLReaChooseType.setVisibility(View.VISIBLE);
                break;

            case R.id.etEarnCalender:

                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Toast.makeText(getContext(), getTime(date), Toast.LENGTH_SHORT).show();
                        et_Date.setText(getTime(date));

                    }
                }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
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


                break;


            case R.id.btnEarnType_9 :
                IconType = 9;
                DisplayLinearAddTool("生活费");
                break;


            case R.id.btnEarnSure:

                //将账目数据写入数据库
                WriteInDB(uid);
                clearText();

                ViewLinearTool.setVisibility(View.GONE);

                break;


//            case R.id.btnUpdate:
//
//                //将账目数据写入数据库
//                UpdataDB();
//                Log.w("检查处-1：" , "更新成功-1");
//                ViewLinearTool.setVisibility(View.GONE);
//
//                break;


            case R.id.btnEarnTypeSure:
                ViewLReaChooseType.setVisibility(View.GONE);
                et_Type.setText(used_Type);
                break;

            case R.id.btnEarnTypeQuit:
                ViewLReaChooseType.setVisibility(View.GONE);
                break;



        }


    }

    private void clearText() {
        et_Type.setText("");
        et_Note.setText("");
        et_Date.setText("");
        et_Money.setText("");
    }


    private void WriteInDB(int uid) {
        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        final SQLiteDatabase db = dbhelper.getReadableDatabase();

        String usedType = et_Type.getText().toString() ;
        String Note =     et_Note.getText().toString();
        String Date =     et_Date.getText().toString();
        String Money =    et_Money.getText().toString();

        try{
            //记账信息表   账条编号nid，记录者编号userId，收支payment_type，
            //记录时间record_date，用途used_type，金额money，备注notes  图标类型 iconType
//            Log.w("检查处0：" , "数据读取成功");

            ContentValues values=new ContentValues();


            values.put("uid", uid);
            values.put("payment_type","收入");
            values.put("record_date", Date);
            values.put("used_type", usedType);
            values.put("money",Double.parseDouble(Money));
            values.put("notes", Note);
            values.put("iconType", IconType);

            Long insertSuccess =  db.insert("mainData",null,values);
//            Log.w("检查处1：" , String.valueOf(insertSuccess)+"  数据插入成功");

            if (insertSuccess == -1)
                Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "记录成功", Toast.LENGTH_SHORT).show();

//            Toast.makeText(getActivity(),"uid" + uid,Toast.LENGTH_SHORT ).show();

//            db.close();
        }catch (SQLiteException e){
            Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
        }



    }


    private void addTypeArrData(){
        //        兼职  工资  生活费
        mTypeItems.add("兼职");
        mTypeItems.add("工资");
        mTypeItems.add("生活费");
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

//    private void UpdataDB() {
//        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
//        final SQLiteDatabase db = dbhelper.getReadableDatabase();
//
//
//
//        //实例化内容值
//        ContentValues values = new ContentValues();
//        //在values中添加内容
//
//
//        String usedType = et_Type.getText().toString() ;
//        String Note =     et_Note.getText().toString();
//        String Date =     et_Date.getText().toString();
//        String Money =    et_Money.getText().toString();
//
//        if (!usedType.isEmpty())
//            values.put("used_type",usedType);
//
//        if (!Note.isEmpty())
//            values.put("notes", Note);
//
//        if (!Date.isEmpty())
//            values.put("record_date", Date);
//
//        if (!Money.isEmpty())
//            values.put("money", Money);
//
//        //修改条件
//        String whereClause = "nId=?";
//
//        //修改添加参数
//        String[] whereArgs={String.valueOf(nid)};
//
//        //修改
//        int returesult = db.update("mainData",values,whereClause,whereArgs);
//
//        Log.w("更新结果：" , String.valueOf(returesult));
//        if (returesult > 0)
//            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
//
////        //修改SQL语句
////        String sql = "update stu_table set snumber = 654321 where id = 1";
////        //执行SQL
////        db.execSQL(sql);
//
//    }

    private  void  DisplayLinearAddTool(String typeName){
        ViewLinearTool.setVisibility(View.VISIBLE);
        et_Type.setText(typeName);
        et_Date.setText(getCurrentDate());
    }

//    private void DisUpdateTool() {
//        ViewLinearTool.setVisibility(View.VISIBLE);
//        btn_Sure.setVisibility(View.GONE);
//        btn_Update.setVisibility(View.VISIBLE);
//
////      nid, uid, payment_type, record_date , used_type ,money,notes icontype
//
//        String ut = mo.get("used_type").toString();
//        String note = mo.get("notes").toString();
//        String date = mo.get("record_date").toString();
//        String money = mo.get("money").toString();
//
//        if( !ut.equals(null) )
//            et_Type.setText(ut);
//        if(!note.equals(null))
//            et_Note.setText(note);
//        if(!date.equals(null))
//            et_Date.setText(date);
//        if(!money.equals(null))
//            et_Money.setText(money);
//
//    }

    //得到当前的日期
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }



}
