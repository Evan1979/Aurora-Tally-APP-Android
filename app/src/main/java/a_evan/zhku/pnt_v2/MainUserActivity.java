package a_evan.zhku.pnt_v2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RadioGroup;

import a_evan.zhku.pnt_v2.data_Obj.User;

//AppCompatActivity  FragmentActivity
public class MainUserActivity extends FragmentActivity {


        private User u;

        private Button btnDetail, btnStatistics, btnPersonCent;
        private RadioGroup radioGroup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_user);


            Intent MainUAct = getIntent();
            int id = MainUAct.getIntExtra("UserId",-1);
            String name = MainUAct.getStringExtra("UserName");
            String passw = MainUAct.getStringExtra("UserPassw");

            u = new User(id, name, passw);

            initView();
            initFragment();
        }

        private void initFragment() {

            FragmentManager ft = getSupportFragmentManager();
            FragmentTransaction fm = ft.beginTransaction();
            fm.replace(R.id.frame, new Fragment1_detail());
            fm.commit();
        }

        private void initView() {


            Drawable drawable1 = getResources().getDrawable(R.drawable.strawberry);
            drawable1 .setBounds(0, 0, 70, 70);
            btnDetail = (Button) findViewById(R.id.button01);
            btnDetail.setCompoundDrawables(drawable1,null , null, null);


            Drawable drawable2 = getResources().getDrawable(R.drawable.susi);
            drawable2 .setBounds(0, 0, 70, 70);
            btnStatistics = (Button) findViewById(R.id.button02);
            btnStatistics.setCompoundDrawables(drawable2,null , null, null);


            Drawable drawable3 = getResources().getDrawable(R.drawable.buding);
            drawable3 .setBounds(0, 0, 70, 70);
            btnPersonCent = (Button) findViewById(R.id.button03);
            btnPersonCent.setCompoundDrawables(drawable3,null , null, null);



            radioGroup = (RadioGroup) findViewById(R.id.radio);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
//                    initBtn();
                    switch (checkedId) {
                        case R.id.button01:
//                            btnDetail.setBackground(R.drawable.tab);
                            ft.replace(R.id.frame, new Fragment1_detail());
                            break;
                        case R.id.button02:
//                            btnStatistics.setBackgroundColor(Color.parseColor("#2bccfc"));
                            ft.replace(R.id.frame, new Fragment2_Static());
                            break;

                        case R.id.button03:
//                            btnPersonCent.setBackgroundColor(Color.parseColor("#2bccfc"));
                            ft.replace(R.id.frame, new Fragment3_personalCenter());
                            break;

                    }
                    ft.commit();

                }
            });
        }

//        private void initBtn() {
//        btnDetail.setBackgroundColor(Color.parseColor("#ffffff"));
//        btnStatistics.setBackgroundColor(Color.parseColor("#ffffff"));
//        btnPersonCent.setBackgroundColor(Color.parseColor("#ffffff"));
//
//        }


    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
             return true;//不执行父类点击事件
        }
        if(keyCode== KeyEvent.KEYCODE_BACK){
            return true;//不执行父类点击事件
        }

        return false;//继续执行父类其他点击事件
    }

}
