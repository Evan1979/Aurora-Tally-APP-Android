package a_evan.zhku.pnt_v2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;


@SuppressWarnings("ALL")
public class AddCostFragment extends Fragment implements View.OnClickListener{

    View v;
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    private View ViewLinearTool = null;
    private View ViewLReaChooseType;
    EditText et_Date;
    EditText et_Type;
    EditText et_Note;
    EditText et_Money;
    ImageView[] img = new ImageView[12];
    Button btn_Sure;
    Button btn_Update;

    Button btntypeSure;
    Button btntypeQuit;


    private List<String> mTypeItems;

    String used_Type = "吃饭";
    WheelView wheelViewAddCostType;

    int ids[] = new int[12];
    int uid = -1;
    int nid = -1;
    int IconType = 0;

    Map<String,Object>mo ;

    public AddCostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_cost, container, false);
        initView();


        //初始化类型数据
        mTypeItems = new ArrayList<>();
        addTypeArrData();

        Intent inAddCost = getActivity().getIntent();
        uid = inAddCost.getIntExtra("uid",-1);
//        测试
//        Toast.makeText(getActivity(), "uid" + String.valueOf(uid) , Toast.LENGTH_SHORT).show();
//        Log.w("添加界面的uid：",String.valueOf(uid));


        mo = (Map<String, Object>) inAddCost.getSerializableExtra("updateObject");


        String doType = inAddCost.getStringExtra("update");
        if("1".equals(doType)) {
            DisUpdateTool();
            nid = Integer.valueOf(mo.get("nid").toString());
            Log.w("更新传过来的nid：",mo.get("nid").toString());
            Log.w("更新传过来的note：",mo.get("notes").toString());
        }





        wheelViewAddCostType.setCyclic(false);
        wheelViewAddCostType.setAdapter(new ArrayWheelAdapter(mTypeItems));
        wheelViewAddCostType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(getContext(), "" + mTypeItems.get(index), Toast.LENGTH_SHORT).show();
                used_Type = mTypeItems.get(index);

                ViewLReaChooseType.setFocusable(true);


            }
        });



//        wheelViewAddCostType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    //获得焦点
//                    Log.w("失去焦点","false");
//                    ViewLReaChooseType.setVisibility(View.VISIBLE);
//
//                }
//                else{//失去焦点
//                    Log.w("失去焦点","true");
//                    Toast.makeText(getContext(),"失去焦点",Toast.LENGTH_SHORT);
//                    ViewLReaChooseType.setVisibility(View.GONE);
//
//                }
//            }
//        });


        return v;

    }

    private void addTypeArrData() {
        //      吃饭  超市  娱乐  水果  礼物  购物 外卖  护理  献爱心   医疗   通讯费用   拍照相片
        mTypeItems.add("吃饭");
        mTypeItems.add("超市");
        mTypeItems.add("娱乐");
        mTypeItems.add("水果");
        mTypeItems.add("礼物");
        mTypeItems.add("购物");
        mTypeItems.add("外卖");
        mTypeItems.add("护理");
        mTypeItems.add("献爱心");
        mTypeItems.add("医疗");
        mTypeItems.add("通讯费用");
        mTypeItems.add("拍照相片");

    }



    private void initView() {

        ids = new int[]{R.id.btnType_1, R.id.btnType_2, R.id.btnType_3, R.id.btnType_4,
                        R.id.btnType_5, R.id.btnType_6, R.id.btnType_7, R.id.btnType_8,
                        R.id.btnType_9, R.id.btnType_10, R.id.btnType_11, R.id.btnType_12};

        et_Type = v.findViewById(R.id.etType);
        et_Note = v.findViewById(R.id.etNote);
        et_Date = v.findViewById(R.id.etCalender);
        et_Money = v.findViewById(R.id.etMoney);
        ViewLinearTool = v.findViewById(R.id.linearAddTool);
        ViewLReaChooseType = v.findViewById(R.id.ReaLayoutCostTypeView);


//        ViewLReaChooseType.getOnFocusChangeListener();
//        if (!ViewLReaChooseType.hasWindowFocus()) {
//            Log.w("获得焦点", "hasWindowFocus");
//            ViewLReaChooseType.setVisibility(View.GONE);
//        }
//        if (ViewLReaChooseType.hasFocus())
//            Log.w("获得焦点","hasFocus");
//
//        if (!ViewLReaChooseType.hasFocusable()) {
//            Log.w("获得焦点", "hasFocusable");
//            ViewLReaChooseType.setVisibility(View.GONE);
//        }


//        if (! {
//            Log.w("失去焦点", "hasFocusable");
//            ViewLReaChooseType.setVisibility(View.GONE);
//        }




//        ViewLReaChooseType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    //获得焦点
//                    Log.w("失去焦点","false");
//                    ViewLReaChooseType.setVisibility(View.VISIBLE);
//
//                }
//                else{//失去焦点
//                    Log.w("失去焦点","true");
//                    Toast.makeText(getContext(),"失去焦点",Toast.LENGTH_SHORT);
//                    ViewLReaChooseType.setVisibility(View.GONE);
//
//                }
//            }
//        });

//        ViewLReaChooseType.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.w("获得焦点","true");
//
//                if (ViewLReaChooseType.isShown())
//                    Log.w("获得焦点","true");
//                else
//                    Log.w("失去焦点","true");
//
//                return false;
//            }
//
//
//        });



        wheelViewAddCostType = v.findViewById(R.id.wheelviewaddcosttype);

        btn_Sure = v.findViewById(R.id.btnSure);
        btn_Update = v.findViewById(R.id.btnUpdate);
        btntypeSure = v.findViewById(R.id.btnCostTypeSure);
        btntypeQuit = v.findViewById(R.id.btnCostTypeQuit);


        //初始化图片类型标签
        for (int i= 0; i < 12; i++){
            img[i] = v.findViewById(ids[i]);
            img[i].setOnClickListener(this);
        }

//        et_Date.setOnClickListener(this);
        et_Type.setOnClickListener(this);
        et_Date.setOnClickListener(this);
        btn_Sure.setOnClickListener(this);
        btn_Update.setOnClickListener(this);
        btntypeSure.setOnClickListener(this);
        btntypeQuit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnType_1:
                IconType = 1;
                DisplayLinearAddTool("吃饭");

                break;
            case R.id.btnType_2:
                IconType = 2;
                DisplayLinearAddTool("超市");

                break;


            case R.id.btnType_3:
                IconType = 3;
                DisplayLinearAddTool("娱乐");

                break;

            case R.id.btnType_4:
                IconType = 4;
                DisplayLinearAddTool("水果");

                break;

            case R.id.btnType_5:
                IconType = 5;
                DisplayLinearAddTool("礼物");

                break;

            case R.id.btnType_6:
                IconType = 6;
                DisplayLinearAddTool("购物");

                break;


            case R.id.btnType_7:
                IconType = 7;
                DisplayLinearAddTool("外卖");

                break;

            case R.id.btnType_8:
                IconType = 8;
                DisplayLinearAddTool("护理");

                break;
            case R.id.btnType_9:
                IconType = 9;
                DisplayLinearAddTool("献爱心");

                break;

            case R.id.btnType_10:
                IconType = 10;
                DisplayLinearAddTool("医疗");

                break;

            case R.id.btnType_11:
                IconType = 11;
                DisplayLinearAddTool("通讯费用");

                break;

            case R.id.btnType_12:
                IconType = 12;
                DisplayLinearAddTool("拍照相片");

                break;

            case R.id.etType:
                ViewLReaChooseType.setVisibility(View.VISIBLE);
                ViewLReaChooseType.onWindowFocusChanged(true);

                break;


            case R.id.etCalender:

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
//                .setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                        .build();
                pvTime.show();

                break;


            case R.id.btnSure:

                //将账目数据写入数据库
                WriteInDB();
                ViewLinearTool.setVisibility(View.GONE);

                break;


            case R.id.btnUpdate:

                //将账目数据写入数据库
                UpdataDB();
//                Log.w("检查处-1：" , "更新成功-1");
                clearText();
                ViewLinearTool.setVisibility(View.GONE);

                break;

            case R.id.btnCostTypeSure:
                ViewLReaChooseType.setVisibility(View.GONE);
                et_Type.setText(used_Type);
                break;

            case R.id.btnCostTypeQuit:
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




    private void UpdataDB() {
        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        final SQLiteDatabase db = dbhelper.getReadableDatabase();



        //实例化内容值
        ContentValues values = new ContentValues();
        //在values中添加内容


        String usedType = et_Type.getText().toString() ;
        String Note = et_Note.getText().toString();
        String Date = et_Date.getText().toString();
        String Money = et_Money.getText().toString();


        values.put("iconType", String.valueOf(IconType));

        if (!usedType.isEmpty()) {
            values.put("used_type",usedType);
        }

        if (!Note.isEmpty()) {
            values.put("notes", Note);
        }

        if (!Date.isEmpty()) {
            values.put("record_date", Date);
        }

        if (!Money.isEmpty()) {
            values.put("money", Money);
        }

        //修改条件
        String whereClause = "nId=?";

        //修改添加参数
        String[] whereArgs={String.valueOf(nid)};

        //修改
        int returesult = db.update("mainData",values,whereClause,whereArgs);

        Log.w("更新结果：" , String.valueOf(returesult));
        if (returesult > 0) {
            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
        }

//        //修改SQL语句
//        String sql = "update stu_table set snumber = 654321 where id = 1";
//        //执行SQL
//        db.execSQL(sql);

    }

    private void WriteInDB() {
        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        final SQLiteDatabase db = dbhelper.getReadableDatabase();

        String usedType = et_Type.getText().toString() ;
        String Note =     et_Note.getText().toString();
        String Date =     et_Date.getText().toString();
        String Money =    et_Money.getText().toString();

        try{
            //记账信息表   账条编号nid，记录者编号userId，收支payment_type，
            //记录时间record_date，用途used_type，金额money，备注notes  图标类型 iconType
            Log.w("检查处0：" , "数据读取成功");

            ContentValues values=new ContentValues();
            values.put("uid", uid );
            values.put("payment_type","支出");
            values.put("record_date", Date);
            values.put("used_type", usedType);
            values.put("money",Double.parseDouble(Money));
            values.put("notes", Note);
            values.put("iconType", IconType);

            Long insertSuccess =  db.insert("mainData",null,values);
//            Log.w("检查处1：" , String.valueOf(insertSuccess)+"  数据插入成功");

            if (insertSuccess == -1) {
                Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "记录成功", Toast.LENGTH_SHORT).show();
            }

//            db.close();
        }catch (SQLiteException e){
            Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
        }

    }


    private  void  DisplayLinearAddTool(String typeName){
        ViewLinearTool.setVisibility(View.VISIBLE);
        et_Type.setText(typeName);
        et_Date.setText(getCurrentDate());
    }

    private void DisUpdateTool() {
        ViewLinearTool.setVisibility(View.VISIBLE);
        btn_Sure.setVisibility(View.GONE);
        btn_Update.setVisibility(View.VISIBLE);

//      nid, uid, payment_type, record_date , used_type ,money,notes icontype

        String ut = mo.get("used_type").toString();
        String note = mo.get("notes").toString();
        String date = mo.get("record_date").toString();
        String money = mo.get("money").toString();

        if( !ut.equals(null) ) {
            et_Type.setText(ut);
        }
        if(!note.equals(null)) {
            et_Note.setText(note);
        }
        if(!date.equals(null)) {
            et_Date.setText(date);
        }
        if(!money.equals(null)) {
            et_Money.setText(money);
        }

    }


    //得到当前的日期
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public static void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            // TODO: 2018/8/27 处理错误
        }

    }

}

