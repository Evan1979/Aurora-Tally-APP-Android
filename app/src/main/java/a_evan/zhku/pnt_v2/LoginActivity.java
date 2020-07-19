package a_evan.zhku.pnt_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a_evan.zhku.pnt_v2.data_Obj.User;
import a_evan.zhku.pnt_v2.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {




    String result = " , ";
    int code = 1;
    User user;


    EditText etname = null;
    EditText etpwd = null;
    CheckBox cbRempwd = null;
    Button btlogin = null;
    Button btregiste = null;

    private  String uname = "";
    private  String upassw = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RequestPermission();

        etname = findViewById(R.id.etname);
        etpwd = findViewById(R.id.etpassword);
        cbRempwd = findViewById(R.id.cbrempassw);
        btlogin = findViewById(R.id.btlogin);
        btregiste = findViewById(R.id.btregister);


//        Log.w("uname",uname);
//        Log.w("upassw",upassw);

//        //读取data文件方法
//        firstRead(uname,upassw);


        Button btnLogin = findViewById(R.id.btlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname = etname.getText().toString();
                upassw = etpwd.getText().toString();

                if(uname.equals("")||uname==null){
                    Toast.makeText(getApplicationContext(), "请输入用户账号！", Toast.LENGTH_SHORT).show();
                }
                if(upassw.equals("")||upassw==null){
                    Toast.makeText(getApplicationContext(), "请输入用户密码！", Toast.LENGTH_SHORT).show();
                }
                UserJudge(uname,upassw);

            }
        });

    }


    //获取列表数据
    private void UserJudge(String uname, String upassw) {

        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        try{
            String sql="SELECT * FROM User WHERE uName=? and upassw=?";
            Cursor cursor=db.rawQuery(sql,new String[]{uname,upassw});



            if(cursor.getCount()==0){
                Toast.makeText(getApplicationContext(), "用户密码错误！", Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(LoginActivity.this, MainUserActivity.class);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

//              nid, uid, payment_type, record_date , used_type ,money,notes icontype

                        user = new User(Integer.parseInt(cursor.getString(0)),uname, upassw);

                        loginIntent.putExtra("UserId", Integer.parseInt(cursor.getString(0)));
                        loginIntent.putExtra("UserName", cursor.getString(1));
                        loginIntent.putExtra("UserPassw", cursor.getString(2));

//                         cursor.getString(1);
//                         cursor.getString(2);
                        cursor.moveToNext();
                    }
                }

                startActivity(loginIntent);
            }
            cursor.close();
            db.close();
            finish();

        }catch (SQLiteException e){
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }
    }






    //获取读写权限
    private static final int REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, //获得权限后回调
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            // Log.w("onRequestPermissions", "得到权限！");
        }
    }


    void RequestPermission() {//请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                String[] permissions = {//申请权限列表
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, REQUEST_CODE);
            }
        }
    }



}
