package a_evan.zhku.pnt_v2.data_Obj;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    public String userName = null;
    public String pwd = null;
    public int uId ;
    String loginDate = null;


    public User(int uId,String user , String pwd){  // int uId,
        super();
        this.uId = uId;
        this.userName = user;
        this.pwd = pwd;

    }

    public String getUserName() {
        return userName;
    }
    public String getPwd() {
        return pwd;
    }


    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }


    public Boolean isTruePwd(){
        if(userName.equals("admin") && pwd.equals("123"))
            return  true;
        else
            return  false;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLoginDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
        String loginDate = df.format(new Date());
        return loginDate;
    }


    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }



}