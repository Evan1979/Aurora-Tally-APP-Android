package a_evan.zhku.pnt_v2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AddBookkeepingAct extends AppCompatActivity  {

    SectionsPagerAdapter mSectionsPagerAdapter;   // = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager mViewPager; //= findViewById(R.id.view_pager);

//    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bookkeeping);


//        //修改这部分
//        mFragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragAddCost, new AddCostFragment());
//        fragmentTransaction.commitAllowingStateLoss();


        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.AddItemViewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.AddTabs);
        tabs.setupWithViewPager(mViewPager);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));




        //浮动按钮
        FloatingActionButton fab = findViewById(R.id.fab_return);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                //处理添加项目
//                Intent it = new Intent(AddBookkeepingAct.this, MainUserActivity.class);
//                startActivity(it);
                finish();

            }
        });

//        Intent AddBookIntent = getIntent();
//        int pt = AddBookIntent.getIntExtra("Ptype",-1);
//        if (pt != -1){
//
//            int fragmentFlag = pt;
//            FragmentManager fManager = this.getSupportFragmentManager();
//            FragmentTransaction transaction = fManager.beginTransaction();
//            switch (fragmentFlag){
//                case 0:
//                    //切支出界面
//                    transaction.replace(R.id.fragAddCost, new AddCostFragment());
//                    break;
//                case 1:
//                    //切收入界面
//                    transaction.replace(R.id.AddEarnFragment, new AddEarnFragment());
////                    navigationBar.check(R.id.AddTabs);
//                    break;
//
//
//            }
//            transaction.commit();
//        }



    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(AddBookkeepingAct AddBookkeeping, FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (position == 0) return new AddCostFragment();
            else return new AddEarnFragment();

        }

        public int getCount() {
            return 2;
        }


        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "支出";
                case 1:
                    return "收入";

            }

            return null;

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            return true;//不执行父类点击事件
        }
        if(keyCode== KeyEvent.KEYCODE_BACK){
            return true;//不执行父类点击事件
        }

        return false;//继续执行父类其他点击事件
    }

}
