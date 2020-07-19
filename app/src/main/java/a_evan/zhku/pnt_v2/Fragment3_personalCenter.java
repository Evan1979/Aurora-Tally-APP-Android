package a_evan.zhku.pnt_v2;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import a_evan.zhku.pnt_v2.database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3_personalCenter extends Fragment implements View.OnClickListener {


    CircleImageView cvHead;
    TextView tvName;
    TextView tvUid;
    TextView tvpCenter;
    TextView tvsqlBackup;
    View v;
    View linearPC;
    View linearSb;
    View linearQuit;

    private byte[] imagebytes;
    int uid = -1;


    public Fragment3_personalCenter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.personpage_frag3, container, false);

        Intent getId = getActivity().getIntent();
        uid = getId.getIntExtra("UserId",-1);

        initview();
        setheadImage();
        //et需要做的操作在这里

        return v;


    }

    private void setheadImage() {

        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        //imgId  uId  imaData
        Cursor cursor = db.query("hImageData", null, " uId =  ? ", new String[]{ String.valueOf(uid)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                imagebytes = cursor.getBlob(2);
                cursor.moveToNext();
            }
        }

        Bitmap imageBitmap =  BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
        cvHead.setImageBitmap(imageBitmap);

    }

    private void initview() {

        cvHead = v.findViewById(R.id.cv_head);
        tvName = v.findViewById(R.id.tvName);
        tvUid = v.findViewById(R.id.tvUid);
        tvpCenter = v.findViewById(R.id.tvpcEdit);
        tvsqlBackup = v.findViewById(R.id.tvSqlBac);
        linearPC = v.findViewById(R.id.linerPC);
        linearSb = v.findViewById(R.id.linerSB);
        linearQuit = v.findViewById(R.id.linerQuit);

        cvHead.setOnClickListener(this);
//        tvName.setOnClickListener(this);
//        tvUid.setOnClickListener(this);
//        tvpCenter.setOnClickListener(this);
//        tvsqlBackup.setOnClickListener(this);
        linearPC.setOnClickListener(this);
        linearSb.setOnClickListener(this);
        linearQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cv_head:

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }

                break;
//            case R.id.tvpcEdit:
//                Toast.makeText(getActivity(), "点击个人信息",Toast.LENGTH_SHORT).show();
//
//                break;
//            case R.id.tvSqlBac:
//                Toast.makeText(getActivity(), "点击备份",Toast.LENGTH_SHORT).show();
//
//                break;

            case R.id.linerPC:

                Intent toEditPC = new Intent(getActivity(), EditPcActivity.class);
                startActivity(toEditPC);
                Toast.makeText(getActivity(), "点击个人信息",Toast.LENGTH_SHORT).show();

                break;
            case R.id.linerSB:
                Intent toBackupAct = new Intent(getActivity(), DataBackupActivity.class);
                startActivity(toBackupAct);
                break;


            case R.id.linerQuit:
                Toast.makeText(getActivity(), "已退出",Toast.LENGTH_SHORT).show();
                Intent toLoginAct = new Intent(getActivity(), LoginActivity.class);
                startActivity(toLoginAct);
                getActivity().finish();
                break;


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imagebytes = baos.toByteArray();
        cvHead.setImageBitmap(bm);

        UpdateImaIntoDB(imagebytes);

    }


    private void UpdateImaIntoDB(byte[] imagebytes) {
        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        final SQLiteDatabase db = dbhelper.getReadableDatabase();


        //实例化内容值
        ContentValues values = new ContentValues();
        //在values中添加内容
//            values.put("uId", uid );
        values.put("imaData", imagebytes);

        //修改条件
        String whereClause = "uId=?";

        //修改添加参数
        String[] whereArgs={String.valueOf(uid)};

        //修改
        int returesult = db.update("hImageData",values,whereClause,whereArgs);

        Log.w("更新结果：" , String.valueOf(returesult));
        if (returesult > 0)
            Toast.makeText(getActivity(), "修改头像成功", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "修改头像失败", Toast.LENGTH_SHORT).show();

//        //修改SQL语句
//        String sql = "update stu_table set snumber = 654321 where id = 1";
//        //执行SQL
//        db.execSQL(sql);

    }




//    private void WriteImaIntoDB(byte[] imagebytes) {
//        final DatabaseHelper dbhelper = new DatabaseHelper(getContext());
//        final SQLiteDatabase db = dbhelper.getReadableDatabase();
//
//
//        try{
//            //imgId  uId  imaData
//
//            ContentValues values=new ContentValues();
////            values.put("uId", uid );
//            values.put("imaData", imagebytes);
//
//            Long insertSuccess =  db.insert("hImageData",null,values);
////            Log.w("检查处1：" , String.valueOf(insertSuccess)+"  数据插入成功");
//
//            if (insertSuccess == -1)
//                Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(getActivity(), "记录成功", Toast.LENGTH_SHORT).show();
//
////            db.close();
//        }catch (SQLiteException e){
//            Toast.makeText(getActivity(), "记录失败", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

}
