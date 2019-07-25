package com.hd.test.noname;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hd.R;

import java.util.List;

public class MainTabActivity extends AppCompatActivity {
    List<String> mData;
    MyFragmentPagerAdapter adapter;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        Fragment fragments[] = new Fragment[]{
                new MyFragment(),
                new ScroFrament(),
                new ScroFrament(),
                new ScroFrament(),
                new ScroFrament()

        };

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
//        initData(1);
//        initView();
    }

//    private void initData(int pager) {
//        mData = new ArrayList<>();
//        for (int i = 1; i < 50; i++) {
//            mData.add("pager" + pager + " 第" + i + "个item");
//        }
//    }

//    private void initView() {
//        //设置ToolBar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        setSupportActionBar(toolbar);//替换系统的actionBar
//
//        //设置TabLayout
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        for (int i = 1; i < 20; i++) {
//            tabLayout.addTab(tabLayout.newTab().setText("TAB" + i));
//        }
//        //TabLayout的切换监听
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                initData(tab.getPosition() + 1);
//                setScrollViewContent();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        setScrollViewContent();
//    }

    /**
     * 刷新ScrollView的内容
     */
    private void setScrollViewContent() {
//        //NestedScrollView下的LinearLayout
//        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_sc_content);
//        layout.removeAllViews();
//        for (int i = 0; i < mData.size(); i++) {
//            View view = View.inflate(MainTabActivity.this, R.layout.item_layout, null);
//            ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
//            //动态添加 子View
//            layout.addView(view, i);
//        }
    }

}