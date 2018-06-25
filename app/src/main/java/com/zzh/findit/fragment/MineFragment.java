package com.zzh.findit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.base.BaseFragment;
import com.zzh.findit.base.CommonAdapter;
import com.zzh.findit.base.MultiItemTypeAdapter;
import com.zzh.findit.base.ViewHolder;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.MemberMode;
import com.zzh.findit.mode.OrderSumMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.CommonUtil;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by 腾翔信息 on 2017/7/7.
 */

public class MineFragment extends BaseFragment {
    private static final String TAG = MineFragment.class.getSimpleName();
    @BindView(R.id.mine_tool)
    Toolbar mBar;
    @BindView(R.id.roundImageView)
    ImageView mHeadImage;
    @BindView(R.id.recycler_function_one)
    RecyclerView mRecycler;
    @BindView(R.id.recycler_function_two)
    RecyclerView mRecyclerTwo;
    @BindView(R.id.nickname)
    TextView mMemberName;
    @BindView(R.id.phoneNum)
    TextView mMemberPhone;
    @BindView(R.id.member_lv)
    TextView mMemberLv;
    @OnClick(R.id.my_info_layout) void info(){
        //// TODO: 2017/8/17  info 
        getWebUrl("my-info");
    }
    @OnClick(R.id.mine_meaasge) void message(){
        getWebUrl("message_list");
    }
    @OnClick(R.id.order_list) void getOrder(){
        getWebUrl("order-list");
    }
    private CommonAdapter<String> adapter;
    private CommonAdapter<String> adaptertwo;
    private Integer[] icons = {R.mipmap.ic_payment_icon,R.mipmap.ic_ship_icon,R.mipmap.ic_receipt_icon
            ,R.mipmap.ic_complete_icon,R.mipmap.ic_sale_icon};
    private Integer[] iconTwos = {R.mipmap.my_collection,R.mipmap.my_wallet,R.mipmap.my_integral
            ,R.mipmap.security_center,R.mipmap.address_management,R.mipmap.my_invoice,R.mipmap.inquiry_sheet};
    private String [] titles = {"待付款","待发货","待收货","已完成","售后"};
    private String [] titletwos = {"我的收藏","我的钱包","我的积分","安全中心","地址管理","我的发票","我的仓库"};
    private List<String> listTitles = new LinkedList<>();
    private List<String> listTitleTwo = new LinkedList<>();
    private List<String> listOrderSum = new LinkedList<>();
    private LinearLayout toolView;
    private QBadgeView badgeView;
    List<Badge> badges;
    //懒加载
    private boolean isEnter = false;
    private boolean isPrepared;//初始化标志位
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_fargment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        isPrepared = true;
        lazyLoad();

        initTool();
        //消息原点
//        initBadge();
        initRecycler();
//
        if ( SharedPreferencesUtil.getInstance(mContext).getString("memberid") == null|| SharedPreferencesUtil.getInstance(mContext).getString("memberid").equals("") || TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("memberid"))){
            initView();
        }else {
            getOrderSum();
        }

        mMemberLv.setVisibility(View.INVISIBLE);
    }
    private void initRecycler(){
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext,5));
        mRecyclerTwo.setHasFixedSize(true);
        mRecyclerTwo.setNestedScrollingEnabled(false);
        mRecyclerTwo.setLayoutManager(new GridLayoutManager(mContext,3));
        mRecyclerTwo.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL));
    }
    private void initView(){
        //第一行功能
        listTitleTwo.clear();
        listTitles.clear();
        for (int i=0;i<titles.length;i++){
            listTitles.add(titles[i]);
        }
        for (int i=0;i<titletwos.length;i++){
            listTitleTwo.add(titletwos[i]);
        }

        //第一行功能菜单
        adapter = new CommonAdapter<String>(mContext,R.layout.mine_functions_one,listTitles) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.functions_one_title,s);
                holder.setImageDrawable(R.id.functions_one_image, ContextCompat.getDrawable(mContext,icons[position]));
                //添加第一行菜单 小圆点
                if (listOrderSum.size()>0){
                    badgeView = new QBadgeView(mContext);
                    badges = new ArrayList<>();
                    badges.add(badgeView.bindTarget(holder.getView(R.id.badge_view)).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeNumber(Integer.parseInt(listOrderSum.get(position))));
                }

            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //todo 待付款 代发货 待收货 完成 售后
                switch (position){
                    case 0:
                        getWebUrl("order-list-wait_pay");
                        break;
                    case 1:
                        getWebUrl("order-list-2");
                        break;
                    case 2:
                        getWebUrl("order-list-wait_rog");
                        break;
                    case 3:
                        getWebUrl("order-list-7");
                        break;
                    case 4:
                        getWebUrl("order-list-8");
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecycler.setAdapter(adapter);

        // 第二行功能菜单
        adaptertwo = new CommonAdapter<String>(mContext,R.layout.mine_functions_one,listTitleTwo) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                String ms = SharedPreferencesUtil.getInstance(mContext).getString("storeid");
                if (ms == null || TextUtils.isEmpty(ms)){
                    if (position != 6){
                        holder.setText(R.id.functions_one_title,s);
                        //todo 测试 图片数据
                        holder.setImageDrawable(R.id.functions_one_image, ContextCompat.getDrawable(mContext,iconTwos[position]));
                    }
                }else if (Integer.parseInt(ms)> 0){
                    holder.setText(R.id.functions_one_title,s);
                    //todo 测试 图片数据
                    holder.setImageDrawable(R.id.functions_one_image, ContextCompat.getDrawable(mContext,iconTwos[position]));
                }else {
                    if (position != 6){
                        holder.setText(R.id.functions_one_title,s);
                        //todo 测试 图片数据
                        holder.setImageDrawable(R.id.functions_one_image, ContextCompat.getDrawable(mContext,iconTwos[position]));
                    }
                }
            }
        };

        mRecyclerTwo.setAdapter(adaptertwo);
        adaptertwo.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    switch (position){
                        case 0:
                            getWebUrl("my-collect");
                            break;
                        case 1:
                            getWebUrl("store-price");//钱包 ==》 账户明细
                            break;
                        case 2:
                            getWebUrl("my-point");
                            break;
                        case 3:
                            getWebUrl("security");
                            break;
                        case 4:
                            getWebUrl("address-list");
                            break;
                        case 5:
                            getWebUrl("apply_receipt");
                            break;
                        case 6:
                            getWebUrl("store-index");
                            break;
                    }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    ////获取第一行功能菜单消息数量
    private void getOrderSum(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("member_id", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.GetOrderSum).params(map).build().execute(new ToCallBack<OrderSumMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(OrderSumMode data, int id) {
                initView();
                if (data.getResult().equals("1")){
                    listOrderSum.clear();
                    listOrderSum.add(data.getData().getOrdermap().getOrderPaymentCount());//待付款
                    listOrderSum.add(data.getData().getOrdermap().getOrderDeliveryCount());//代发货
                    listOrderSum.add(data.getData().getOrdermap().getOrderReceivedCount());//待收货
                    listOrderSum.add(data.getData().getOrdermap().getOrderFinishCount());
                    listOrderSum.add(data.getData().getOrdermap().getOrderServiceCount());
                }
            }
        });
    }
    private void initTool(){
        mBar.setTitle("");
        toolView = mBar.findViewById(R.id.mine_message);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBar);
    }

    private void initBadge(){
        badgeView = new QBadgeView(mContext);
        badges = new ArrayList<>();
        badges.add(badgeView.bindTarget(toolView).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeNumber(6));
        toolView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("消息中心",mContext);
                for (Badge badge : badges){
                    badge.hide(true);
                }
            }
        });

        badgeView.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                }
            }
        });
    }

    //获取member
    private void getMember(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.MEMBER)
                .params(map).build().execute(new ToCallBack<MemberMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(MemberMode data, int id) {
                if (data.getResult().equals("1")){
                    mMemberName.setText(TextUtils.isEmpty(data.getData().getMember().getNickname()) ? data.getData().getMember().getName() : data.getData().getMember().getNickname());
                    mMemberPhone.setText(CommonUtil.getPrivacyPhone(data.getData().getMember().getMobile()));
                    if (TextUtils.isEmpty(data.getData().getMember().getFace())){
                        Picasso.with(mContext).load(R.drawable.people_icon).into(mHeadImage);
                    }else {
                        Picasso.with(mContext).load(data.getData().getMember().getFace()).error(R.drawable.people_icon).placeholder(R.drawable.people_icon).into(mHeadImage);
                    }
                    mMemberLv.setVisibility(View.VISIBLE);
                    mMemberLv.setText(SharedPreferencesUtil.getInstance(mContext).getString("memberLv"));
                }else if (data.getResult().equals("0")){
                    mMemberLv.setVisibility(View.INVISIBLE);
                    mMemberName.setText("游客模式");
                    mMemberPhone.setText("");
                    Picasso.with(mContext).load(R.drawable.people_icon).into(mHeadImage);
                }

            }
        });
    }

    private void getWebUrl(final String type){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId",SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        map.put("module","user");
        map.put("type",type);
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map)
                .build().execute(new ToCallBack<WebMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(WebMode data, int id) {
                if (data.getResult().equals("1")){
                    Intent intent = new Intent(mContext, WebJS.class);
                    intent.putExtra("type",type);// 基本无用
                    SharedPreferencesUtil.getInstance(mContext).putString("url",data.getData().getWebUrl());
                    startActivity(intent);
                }else if (data.getMessage().contains("用户id不能为空")){
                    //todo 游客模式
                    youke(mContext);
                }
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isEnter = true;
            onVisible();
        }else {
            isEnter = false;
            onInvisible();
        }
    }
    protected void onInvisible() {
        //不可见
        Log.e("懒加载","不可见");
    }
    protected void onVisible() {
        //可见
        lazyLoad();
    }
    protected void lazyLoad() {
        if (!isEnter || !isPrepared) {
            return;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getMember();
        if ( SharedPreferencesUtil.getInstance(mContext).getString("memberid") == null|| SharedPreferencesUtil.getInstance(mContext).getString("memberid").equals("") || TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("memberid"))){
            initView();
        }else {
            getOrderSum();
        }

    }
}
