package com.zzh.findit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.mobstat.StatService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.activitys.LoginActivity;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.fragment.CartFragment;
import com.zzh.findit.fragment.HomeFragment;
import com.zzh.findit.fragment.MineFragment;
import com.zzh.findit.fragment.NewsFragment;
import com.zzh.findit.fragment.SortFargment;
import com.zzh.findit.http.OkHttpHelper;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.CartMode;
import com.zzh.findit.mode.LoginMode;
import com.zzh.findit.utils.CommonUtil;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static MainActivity instance;
    List<Badge> badges;
    private QBadgeView badgeView;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> view = new ArrayList<>();
    private static final String FIRST_OPEN = "FIRST_OPEN";
    @BindView(R.id.main_vp)
    ViewPager mPage;
    @BindView(R.id.cart_layout)
    LinearLayout cartLayout;
    @BindViews({R.id.home_tab_image,R.id.sort_tab_image,R.id.news_tab_image,R.id.cart_tab_image,R.id.mine_tab_image})
    List<ImageView> Images;
    @BindViews({R.id.home_tab_text,R.id.sort_tab_text,R.id.news_tab_text,R.id.cart_tab_text,R.id.mine_tab_text})
    List<TextView> Texts;
    //四个底部点击事件 home
    @OnClick(R.id.home_tab) void home(){
        setTab(0);
    }
    @OnClick(R.id.sort_tab) void sort(){
        setTab(1);
    }
    @OnClick(R.id.news_tab) void news(){
        setTab(2);
    }
    @OnClick(R.id.cart_tab) void cart(){
        setTab(3);
    }
    @OnClick(R.id.mine_tab) void mine(){
        setTab(4);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatService.start(this);
        //首先先获取屏幕宽度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        Contants.screenHeight = (int) (height / density);// 屏幕高度(dp)
        Contants.screenWidth = (int) (width / density);;// 屏幕宽度(dp)
        boolean isFirstOpen = SharedPreferencesUtil.getInstance(mContext).getBoolean(FIRST_OPEN);
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        badgeView = new QBadgeView(mContext);
        MyApplication.getInstance().add(this);
        instance = this;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        upDataCartList();
        hasToolBar(false);

        //添加四个fragment
        Fragment home = new HomeFragment();
        Fragment sort = new SortFargment();
        Fragment news = new NewsFragment();
        Fragment cart = new CartFragment();
        Fragment mine = new MineFragment();

        view.add(home);
        view.add(sort);
        view.add(news);
        view.add(cart);
        view.add(mine);

        pagerAdapter  = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return view.get(position);
            }

            @Override
            public int getCount() {
                return view.size();
            }
        };

        mPage.setAdapter(pagerAdapter);
        mPage.setCurrentItem(0);
        setTab(0);

        mPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int num = mPage.getCurrentItem();
                setTab(num);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void initBadgeView(){
        //消息原点功能

        badges = new ArrayList<>();
        //购物车提示数字
        badges.add(badgeView.bindTarget(cartLayout).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeNumber(Contants.pointNum));
        badgeView.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                }
            }
        });

        for (Badge badge :badges){
            badge.setGravityOffset(0, 0, true);
        }
    }
    private void setTab(int index){
        clean();
        switch (index){
            case 0:
                mPage.setCurrentItem(0);
                Texts.get(0).setTextColor(Color.parseColor("#FF9908"));
                Images.get(0).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_selhome_icon));
                break;
            case 1:
                mPage.setCurrentItem(1);
                Texts.get(1).setTextColor(Color.parseColor("#FF9908"));
                Images.get(1).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_sort_sel_icon));
                break;
            case 2:
                mPage.setCurrentItem(2);
                Texts.get(2).setTextColor(Color.parseColor("#FF9908"));
                Images.get(2).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.news_sel));

                break;
            case 3:
                mPage.setCurrentItem(3);
                Texts.get(3).setTextColor(Color.parseColor("#FF9908"));
                Images.get(3).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_cart_sel_icon));
                break;
            case 4:
                mPage.setCurrentItem(4);
                Texts.get(4).setTextColor(Color.parseColor("#FF9908"));
                Images.get(4).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_mine_sel_icon));
                break;
        }


    }
    private void clean(){
        Texts.get(0).setTextColor(Color.parseColor("#93969B"));
        Texts.get(1).setTextColor(Color.parseColor("#93969B"));
        Texts.get(2).setTextColor(Color.parseColor("#93969B"));
        Texts.get(3).setTextColor(Color.parseColor("#93969B"));
        Texts.get(4).setTextColor(Color.parseColor("#93969B"));
        Images.get(0).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_home_icon));
        Images.get(1).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_sort_icon));
        Images.get(2).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.news));
        Images.get(3).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_cart_icon));
        Images.get(4).setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_mine_icon));
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cart"))&&getIntent().getStringExtra("cart").equals("cart")&&SharedPreferencesUtil.getInstance(mContext).getString("cart").equals("cart")){
            setTab(2);
            SharedPreferencesUtil.getInstance(mContext).putString("cart","");
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        upDataCartList();
    }

    private void upDataCartList(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.CARTDATA).params(map).build().execute(new ToCallBack<CartMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(CartMode data, int id) {
                if (data.getResult().equals("1")){
                    if (data.getData().getCartList() != null ){
                        Contants.pointNum = data.getData().getCartList().size();
                        initBadgeView();
                    }
                }

            }
        });
    }
}
