package a_evan.zhku.pnt_v2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import a_evan.zhku.pnt_v2.database.FileAccessor;

public class DataBackupActivity extends AppCompatActivity {


    View linerBackup;
    View linerRestore;

    Button btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_backup);

        linerBackup = findViewById(R.id.linerBackupData);
        linerRestore = findViewById(R.id.linerRestoreData);
        btnQuit = findViewById(R.id.btnreturnPC);

        linerBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDir("Backup");

                //待备份文件所在路径
                String pa =  getApplicationContext().getDatabasePath("mydb").getAbsolutePath();

                //备份路径：
                String backupPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Backup/mydb";

                FileAccessor fa = new FileAccessor();
                if (fa.copyFile(pa,backupPath))
                    Toast.makeText(getApplicationContext(),"备份成功,路径："  + backupPath, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"备份失败", Toast.LENGTH_SHORT).show();


            }


        });

        linerRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //备份路径：
                String backupPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Backup/mydb";

                //待恢复文件所在路径
                String restorePath =  getApplicationContext().getDatabasePath("mydb").getAbsolutePath();

                FileAccessor fa = new FileAccessor();
                if (fa.copyFile(backupPath,restorePath))
                    Toast.makeText(getApplicationContext(),"恢复成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"恢复失败", Toast.LENGTH_SHORT).show();


            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private final int REQUESTCODE = 101;
    public void createDir(String DirName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
            }
        }
        //Environment.getExternalStorageDirectory().getAbsolutePath():SD卡根目录
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DirName);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
//            Toast.makeText(MainActivity.this, "文件夹创建成功", Toast.LENGTH_LONG).show();
            Log.w("创建结果：","成功");

        } else {
//            Toast.makeText(MainActivity.this, "文件夹已存在", Toast.LENGTH_LONG).show();
            Log.w("创建结果：","失败");


        }
    }

}
