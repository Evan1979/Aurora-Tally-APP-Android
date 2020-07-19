package a_evan.zhku.pnt_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Fragment2_Static extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private List<Fragment> list;
    private View view;
    private View View_linerlay_choose = null;
    private View View_liner_title = null;

    TextView tvchooseCost = null;
    TextView tvchooseEarn = null;
    TextView tvTitle = null;


    int choose = -1;

    private ViewPager viewPager;
    private Button btn_Day, btn_Month, btn_Year;
    private int uid = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.statistics_frag2,container,false);
        initView();


        Intent getId = getActivity().getIntent();
        uid = getId.getIntExtra("UserId",-1);

        return view;
    }

    private void initView() {
        viewPager=(ViewPager)view.findViewById(R.id.viewpager01);

        list=new ArrayList<>();
        btn_Day =(Button)view.findViewById(R.id.btnfragDay);
        btn_Month =(Button)view.findViewById(R.id.btnfragMonth);
        btn_Year =(Button)view.findViewById(R.id.btnfragYear);
        View_linerlay_choose = view.findViewById(R.id.linearChoose1);
        View_liner_title = view.findViewById(R.id.lineartitle11);
        tvchooseCost = view.findViewById(R.id.tvcostText);
        tvchooseEarn = view.findViewById(R.id.tvearnText);
        tvTitle  = view.findViewById(R.id.tvStatisticsTitle);



        btn_Day.setOnClickListener(this);
        btn_Month.setOnClickListener(this);
        btn_Year.setOnClickListener(this);
        View_linerlay_choose.setOnClickListener(this);
        View_liner_title.setOnClickListener(this);
        tvchooseCost.setOnClickListener(this);
        tvchooseEarn.setOnClickListener(this);

        //这些界面要也要一个一个先去实现
        list.add(new DayFragment());
        list.add(new MonthFragment());
        list.add(new YearFragment());
//        list.add(new Fragment_Month());

        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(),list));
//        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        initBtnListener();
//        switch (i){
//            case 0:
//                btn_Day.setBackgroundColor(Color.parseColor("#ff735c"));
//                break;
//            case 1:
//                btn_Month.setBackgroundColor(Color.parseColor("#ff735c"));
//                break;
//            case 2:
//                btn_Year.setBackgroundColor(Color.parseColor("#ff735c"));
//                break;
//
//        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
//        initBtnListener();


        switch (v.getId()){
            case R.id.btnfragDay:
//                btn_Day.setBackgroundColor(Color.parseColor("#ff735c"));
                viewPager.setCurrentItem(0);
                break;
            case R.id.btnfragMonth:
//                btn_Month.setBackgroundColor(Color.parseColor("#ff735c"));
                viewPager.setCurrentItem(1);
                break;
            case R.id.btnfragYear:
//                btn_Year.setBackgroundColor(Color.parseColor("#ff735c"));
                viewPager.setCurrentItem(2);
                break;

            case R.id.lineartitle11:
//                btn_Year.setBackgroundColor(Color.parseColor("#ff735c"));
                View_linerlay_choose.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "你点击了收入栏", Toast.LENGTH_SHORT);
                break;


            case R.id.tvcostText:
                Toast.makeText(getActivity(), "切换至支出界面", Toast.LENGTH_SHORT);

                choose = 1;
                tvTitle.setText("支出");
                View_linerlay_choose.setVisibility(View.GONE);


                break;

            case R.id.tvearnText:
                Toast.makeText(getActivity(), "切换至收入界面", Toast.LENGTH_SHORT);

                choose = 0;
                tvTitle.setText("收入");
                View_linerlay_choose.setVisibility(View.GONE);
                break;


        }
    }

    private void initBtnListener(){

//        btn_Day.setBackgroundResource(R.color.colorBac);
//        btn_Month.setBackgroundResource(R.color.colorBac);
//        btn_Year.setBackgroundResource(R.color.colorBac);
    }


    private class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
