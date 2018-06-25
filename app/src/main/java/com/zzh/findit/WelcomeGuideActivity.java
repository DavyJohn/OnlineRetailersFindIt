package com.zzh.findit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zzh.findit.adapter.ViewPagerAdapter;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.utils.DisplayUtil;
import com.zzh.findit.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/3/9.
 */

public class WelcomeGuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = WelcomeGuideActivity.class.getSimpleName();
    @BindView(R.id.guide_vp)
    ViewPager mVP;
    @BindView(R.id.widget_banner_points_group)
    LinearLayout points;
    @BindView(R.id.wel_button)
    Button mBtn;
    @OnClick(R.id.wel_button) void wel(){
        //跳过
        SharedPreferencesUtil.getInstance(this).putBoolean(this,"FIRST_OPEN",true);
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
    private LinearLayout.LayoutParams params;

    private List<View> views = new ArrayList<>();
    private ViewPagerAdapter adapter;
    //选中显示Indicator
    private int selectRes = R.drawable.image_sel_doct_shap;

    //非选中显示Indicator
    private int unSelcetRes = R.drawable.image_doct_shap;

    private static final int [] pics = {R.drawable.start_one,R.drawable.start_two,R.drawable.start_three};
    //记录当前选中位置
    private int currentIndex;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    startActivity(new Intent(mContext,MainActivity.class));
                    finish();
                    break;
            }

        }

    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasToolBar(false);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        //第一次默认为游客模式
        SharedPreferencesUtil.getInstance(mContext).putString("nickname","游客模式");
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i =0;i<pics.length;i++){
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(mParams);
            Picasso.with(mContext).load(pics[i]).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).into(iv);
            views.add(iv);

        }

        adapter = new ViewPagerAdapter(views);
        mVP.setAdapter(adapter);
        mVP.addOnPageChangeListener(this);

        //初始化与个数相同的指示器点
        for (int i = 0; i < pics.length; i++)
        {
            View dot = new View(mContext);
            dot.setBackgroundResource(R.drawable.image_doct_shap);
            params = new LinearLayout.LayoutParams(
                    DisplayUtil.dip2px(mContext, 30),
                    DisplayUtil.dip2px(mContext, 30));
            params.leftMargin = 30;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            points.addView(dot);
        }
        points.getChildAt(0).setBackgroundResource(selectRes);
    }
    @Override
    public int getLayoutId() {
        return R.layout.guide_layout;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 2){
            final Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            thread.start();
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = position % 3;
        for (int i = 0; i < points.getChildCount(); i++)
        {
            points.getChildAt(i).setBackgroundResource(unSelcetRes);
        }
        points.getChildAt(position).setBackgroundResource(selectRes);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        //切换后台就设置下次不进入功能引导
        SharedPreferencesUtil.getInstance(this).putBoolean(this,"FIRST_OPEN",true);
        finish();
    }
}