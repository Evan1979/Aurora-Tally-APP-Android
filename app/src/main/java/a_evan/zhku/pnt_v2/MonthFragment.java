package a_evan.zhku.pnt_v2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment  implements View.OnClickListener{

    private HorizontalScrollView hs;
    private LinearLayout liner;
    private ViewPager view_pager;
    private String[] titles;
    private ArrayList<TextView> titlesView;

    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_month, container, false);

        //初始化
        hs = (HorizontalScrollView)v.findViewById(R.id.hs);
        liner = (LinearLayout)v.findViewById(R.id.liner);
        view_pager = (ViewPager) v.findViewById(R.id.view_pager);
        titles = new String[]{"2019-06", "2019-07", "2019-08", "2019-09", "2019-10", "2019-11", "2019-12", "2020-01",
                "2020-02", "2020-03"};

        //装标题控件的集合
        titlesView = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(v.getContext());
            textView.setTextSize(15);
            if (i == 0) {
                textView.setTextColor(Color.RED);
            }
            textView.setText(titles[i]);   //设置滑动栏文本信息
            textView.setId(i);//把循环的i设置给textview的下标;
            textView.setOnClickListener(this);

            //LinearLayout中的孩子的定位参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(10,10,10,10);//设置左上右下四个margin值;
            //layoutParams是让linearLayout知道如何摆放自己孩子的位置的;
            liner.addView(textView,layoutParams);
            titlesView.add(textView);
        }


        view_pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MonthInnerFragment.getInstance(titles[position]);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

        });





        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            // 此方法是页面跳转完后被调用
            @Override
            public void onPageSelected(int position) {
                // 标题变色,用循环改变标题颜色,通过判断来决定谁红谁灰;
                // 举例:娱乐的下标是position是1
                for (int i = 0; i < titles.length; i++) {
                    if(i == position){
                        titlesView.get(i).setTextColor(Color.RED);
    //                        titlesView.get(i).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线

                    }else {
                        titlesView.get(i).setTextColor(Color.GRAY);
    //                        titlesView.get(i).getPaint().setFlags();    //抗锯齿

                    }

                }
                // 标题滑动功能
                int width = titlesView.get(position).getWidth();
                int totalWidth = (width +20)*position;
                hs.scrollTo(totalWidth,0);
            }

            // 此方法是在状态改变的时候调用。
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        view_pager.setCurrentItem(id);
    }
}
