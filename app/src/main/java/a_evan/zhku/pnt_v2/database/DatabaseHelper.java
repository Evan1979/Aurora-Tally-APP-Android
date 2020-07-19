package a_evan.zhku.pnt_v2.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String dbname="mydb";
    public DatabaseHelper(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //账号userId，姓名name，密码passWord
        db.execSQL("create table if not exists User(" +
                "uId INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "uName  VARCHAR (20) NOT NULL," +
                "upassw VARCHAR (20) NOT NULL)");

//        CREATE TABLE User (
//                uId INTEGER  PRIMARY KEY NOT NULL,
//                uName  VARCHAR (20) NOT NULL,
//                upassw VARCHAR (20) NOT NULL
//        );


        //记账信息表   账条编号nid，记录者账号userId，收支payment_type，记录时间record_date，用途used_type，金额money，备注notes
        //图标类型 iconType
        db.execSQL("create table if not exists mainData(" +
                "nid  INTEGER   UNIQUE NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "uid  INTEGER  REFERENCES User (uId)," +
                "payment_type CHAR (4) NOT NULL," +
                "record_date  DATE NOT NULL," +
                "used_type VARCHAR (20)," +
                "notes VARCHAR (50) DEFAULT (1),"+
                "money  DOUBLE NOT NULL," +
                "iconType VARCHAR (30) DEFAULT (0))");


//        CREATE TABLE mainData (
//                nid  INTEGER   UNIQUE NOT NULL PRIMARY KEY,
//                uid  INTEGER  REFERENCES User (uId),
//                payment_type CHAR (4) NOT NULL,
//                record_date  DATE NOT NULL,
//                used_type VARCHAR (20),
//                money  DOUBLE NOT NULL   );

//        imgId  uId  imaData
        db.execSQL("create table if not exists hImageData(" +
                "imgId   INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL," +
                "uId INTEGER REFERENCES User (uId)  NOT NULL," +
                "imaData BLOB  NOT NULL DEFAULT (fffffff))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

