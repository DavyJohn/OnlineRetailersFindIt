package com.zzh.findit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.activitys.MoreIconActivity;
import com.zzh.findit.adapter.RtAdapter;
import com.zzh.findit.base.BaseFragment;
import com.zzh.findit.base.CommonAdapter;
import com.zzh.findit.base.MultiItemTypeAdapter;
import com.zzh.findit.base.ViewHolder;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.BannerMode;
import com.zzh.findit.mode.CatTreeMode;
import com.zzh.findit.mode.Floor;
import com.zzh.findit.mode.FloorGoodsList;
import com.zzh.findit.mode.FloorList;
import com.zzh.findit.mode.HotGoodsMode;
import com.zzh.findit.mode.JxMode;
import com.zzh.findit.mode.LogData;
import com.zzh.findit.mode.RecommendBrandMode;
import com.zzh.findit.mode.RtCatList;
import com.zzh.findit.mode.RtMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.banner.BannerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/7/7.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.home_tool)
    Toolbar mBar;
    @BindView(R.id.search_view)
    TextView mSearch;
    @BindView(R.id.main_banner)
    BannerView mBanner;
    @BindView(R.id.main_function_one)
    RecyclerView mRecyclerOne;
//    @BindView(R.id.main_hot_shop)
//    RecyclerView mRecyclerHotShop;
    @BindView(R.id.main_hot_loge)
    RecyclerView mRecyclerLoge;
    @BindView(R.id.main_jx_shop)
    RecyclerView mRecyclerJx;
    @BindView(R.id.main_rt_shop)
    RecyclerView mRecyclerRt;

    @OnClick(R.id.search_view) void search(){
        webView("cat",null,"search-app","goods");
    }
    @OnClick(R.id.more_icon) void more(){
        startActivity(new Intent(mContext,MoreIconActivity.class));
    }
    @OnClick(R.id.jingxuan_more) void jxMore(){
        webView("","","goods-list","goods");
    }
    @OnClick(R.id.hot_more) void hotMore(){
        webView("","","goods-list","goods");
    }
    @OnClick(R.id.main_message) void message(){
        webView("","","message_list","user");
    }
    @OnClick(R.id.rt_more) void reMore(){
        webView("","","goods-list","goods");
    }
    private boolean isEnter = false;
    private boolean isPrepared;//初始化标志位
    private CommonAdapter<RecommendBrandMode.RecommendBrandData.BrandData> adapterLoge;
    private CommonAdapter<CatTreeMode.CatTreeData.CatTreeListData> adapterOne ;
    private CommonAdapter<HotGoodsMode.HotGoodsData.HotGoodsList> adapterHotShop;
    private CommonAdapter<JxMode.JxData.ListData>  jxAdapter;
    private LinkedList<RecommendBrandMode.RecommendBrandData.BrandData> listLoge = new LinkedList<>();
    private LinkedList<HotGoodsMode.HotGoodsData.HotGoodsList> listShop = new LinkedList<>();
    private LinkedList<CatTreeMode.CatTreeData.CatTreeListData> listOne = new LinkedList<>();
    private LinkedList<BannerMode.BannerData.AdData> banners = new LinkedList<>();
    private LinkedList<JxMode.JxData.ListData> listJx = new LinkedList<>();
    private LinkedList<RtCatList> rt = new LinkedList<>();
    private RtAdapter rtAdapter;
    private LinkedList<String> titles = new LinkedList<>();
    private LinkedList<String> ids = new LinkedList<>();
    private LinkedList<String> goodsId = new LinkedList<>();
    //
    private LinkedList<FloorList.FloorListData.FloorListDatas> floorListDatas = new LinkedList<>();
    private LinkedList<LinkedList<String>> idLists = new LinkedList<LinkedList<String>>();
    private LinkedList<String> idList = new LinkedList<>();
    private LinkedList<LinkedList<Floor.FloorData.FloorMap.ChildFloors>> childAll = new LinkedList<>();
    private LinkedList<LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData>> dataGoodsAll = new LinkedList<>();
    private LinkedList<LinkedList<Floor.FloorData.FloorMap.FloorMapFloor>> mapFloorsAll = new LinkedList<>();
    private LinkedList<LinkedList<LogData.Logdata.LogList>> logAll = new LinkedList<>();
    private LinkedList<Floor.FloorData.FloorMap.ChildFloors> childsGoods = new LinkedList<>();
    private LinkedList<Floor.FloorData.FloorMap.FloorMapFloor> mapFloors = new LinkedList<>();
    private LinkedList<String> goodsid = new LinkedList<>();
    private LinkedList<String> brandid = new LinkedList<>();
    private LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData> dataGoods = new LinkedList<>();
    private LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData> dataGood = new LinkedList<>();
    private LinkedList<LogData.Logdata.LogList> logs = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        mBar.setTitle("");

        ((AppCompatActivity)getActivity()).setSupportActionBar(mBar);
        isPrepared = true;
        lazyLoad();
        mRecyclerOne.setHasFixedSize(true);
        mRecyclerRt.setNestedScrollingEnabled(false);
        mRecyclerJx.setNestedScrollingEnabled(false);
        mRecyclerJx.setHasFixedSize(true);
        mRecyclerRt.setHasFixedSize(true);
        mRecyclerRt.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerOne.setLayoutManager(new GridLayoutManager(mContext,3));
        LinearLayoutManager jx = new LinearLayoutManager(mContext);
        jx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerJx.setLayoutManager(jx);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerLoge.setHasFixedSize(true);
        mRecyclerLoge.setLayoutManager(new GridLayoutManager(mContext,4));
    }

    private void getIdList(final String id){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("floorId",id);
        try {
            Response response = OkHttpUtils.post().url(Contants.BASEURL+Contants.Floor).params(map).build().execute();
            String AllData = response.body().string();
            JSONObject object = new JSONObject(AllData);
            JSONObject data = object.getJSONObject("data");
            JSONObject mapData = data.getJSONObject("map");
            JSONArray Floors = mapData.getJSONArray("childFloors");
            JSONObject goods_ids = (JSONObject) Floors.opt(0);
            String json = (String) goods_ids.get("goods_ids_json");
            JSONObject json_ids = new JSONObject(json);
            System.out.print(json_ids);
            Iterator iterator = json_ids.keys();
            idList = new LinkedList<>();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String v = (String) json_ids.get(key);
                System.out.print(v);
                idList.add(v);
            }
            System.out.print(idList);
            idLists.add(idList);
            System.out.print(idLists);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //一个小标题一个数据源
    //最好同步
    private void getFloor(final String id, final int s, final int postion) {
            //确保相等才解析
            LinkedHashMap<String,String> map = new LinkedHashMap<>();
            map.put("floorId",id);
            OkHttpUtils.post().url(Contants.BASEURL+Contants.Floor).params(map).build()
                    .execute(new ToCallBack<Floor>() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(Floor data, int i) {
                            if (id.equals(ids.get(postion))){
                                System.out.print(i);

                                data.setTitle(titles.get(postion));
                                if (data.getData().getMap().getChildFloors().size()>1){
                                    data.getData().getMap().getChildFloors().get(0).setItemName(titles.get(postion));
                                }
                                if (data.getResult().equals("1")){
                                    childsGoods = new LinkedList<>();
                                    mapFloors = new LinkedList<>();
                                    childsGoods.addAll(data.getData().getMap().getChildFloors());//传入所有的 child

                                    mapFloors.add(data.getData().getMap().getFloor());//
                                    childAll.add(childsGoods);
                                    mapFloorsAll.add(mapFloors);
                                    System.out.print(childAll);

                                    if (childAll.size() == titles.size()){
                                        //全部记载玩才往下走
                                        for (int m=0;m<childAll.size();m++){
                                            goodsid.clear();
                                            brandid.clear();

                                            try {
                                                JSONObject object = new JSONObject(childAll.get(m).get(0).getGoods_ids_json());
                                                Iterator iterator = object.keys();
                                                while (iterator.hasNext()){
                                                    String key = (String) iterator.next();
                                                    String v = (String) object.get(key);
                                                    goodsid.add(v);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            String goods = "";
                                            for (int n=0;n<goodsid.size();n++){
                                                goods = goods+goodsid.get(n)+"-";
                                            }
                                            goods = goods.substring(0,goods.length()-1);
                                            //也需要goods 排序
                                            //将goods 塞进去 之后判断 在执行
                                            getFloorGoodsList(goods);//获取商品接口


                                            ///////// 品牌
                                            try {
                                                JSONObject object = new JSONObject(mapFloorsAll.get(m).get(0).getBrand_ids());
                                                Iterator iterator = object.keys();
                                                while (iterator.hasNext()){
                                                    String key = (String) iterator.next();
                                                    String v = (String) object.get(key);
                                                    brandid.add(v);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            String brandids = "";
                                            for (int n=0;n<brandid.size();n++){
                                                brandids = brandids+brandid.get(n)+"-";
                                            }
                                            brandids = brandids.substring(0,brandids.length()-1);
                                            getLog(brandids,s);

                                        }
                                    }
                                }
                            }
                        }
                    });
        }

    //显示item 商品
    private void getFloorGoodsList(final String ids){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("goods_ids",ids);
        OkHttpUtils.post().url(Contants.BASEURL+Contants.FloorGoodsList).params(map).build()
                .execute(new ToCallBack<FloorGoodsList>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(FloorGoodsList data, int id) {
                System.out.print(id);

                if (data.getResult().equals("1")){
                    dataGoods = new LinkedList<>();
                    dataGood = new LinkedList<>();
                    //取前6 条 这个会有少于6 条情况的存在 （修改完毕）
                    if (data.getData().getList().size() >=6){
                        for(int i=0;i<6;i++){
                            dataGood.add(data.getData().getList().get(i));
                        }
                    }else {
                        for(int i=0;i<data.getData().getList().size();i++){
                            dataGood.add(data.getData().getList().get(i));
                        }
                    }


                    dataGoods.addAll(dataGood);
                    dataGoodsAll.add(dataGoods);
                    // 所有数据
                    System.out.print(dataGoodsAll);
                    System.out.print(childAll);
                    if (childAll.size() >= titles.size() && dataGoodsAll.size() >= titles.size()  && logAll.size() >= titles.size()){
                        reView();
                    }
                }
            }
        });
    }

    //获取品牌
    private void getLog(String ids, final int s){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("brand_ids",ids);
        OkHttpUtils.post().params(map).url(Contants.BASEURL+Contants.FloorBrandListByCatId).build().execute(new ToCallBack<LogData>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(LogData data, int id) {
                System.out.print(data);
                if (data.getResult().equals("1")){
                    logs = new LinkedList<>();
                    //判断 loge 输出10
                    if (data.getData().getList().size()>10){
                        for (int i=0;i<10;i++){
                            logs.add(data.getData().getList().get(i));
                            logAll.add(logs);
                        }
                    }else {
                        logs.addAll(data.getData().getList());
                        logAll.add(logs);
                    }

                }
                System.out.print(logAll);
                if (childAll.size() >= titles.size() && dataGoodsAll.size() >= titles.size()  && logAll.size() >= titles.size()){
                    reView();
                }

            }
        });
    }
    private void funone(){
        adapterOne = new CommonAdapter<CatTreeMode.CatTreeData.CatTreeListData>(mContext,R.layout.mine_functions_one,listOne) {
            @Override
            protected void convert(ViewHolder holder, CatTreeMode.CatTreeData.CatTreeListData data, int position) {
                holder.setText(R.id.functions_one_title,data.getName());
                holder.setImageUrl(R.id.functions_one_image, data.getImage());
            }

        };
        mRecyclerOne.setAdapter(adapterOne);
        adapterOne.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    webView("cat",listOne.get(position).getCat_id(),"goods-list","goods");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }
    //热推 界面绘制
    private void reView(){

        System.out.print(childAll);
        System.out.print(dataGoodsAll);
        System.out.print(logAll);
        rtAdapter = new RtAdapter(mContext);
        rtAdapter.setData(titles,ids,childAll,dataGoodsAll,logAll,idLists);
        Log.e("childAll==",childAll.size()+"");
        Log.e("dataGoodsAll==",dataGoodsAll.size()+"");
        Log.e("idLists==",idLists.size()+"");
        mRecyclerRt.setAdapter(rtAdapter);
        //添加一个logo 数据

    }
    //精选 界面绘制
    private void jxView(){
        jxAdapter = new CommonAdapter<JxMode.JxData.ListData>(mContext,R.layout.main_hot_shop_layout,listJx) {
            @Override
            protected void convert(ViewHolder holder, JxMode.JxData.ListData listData, int position) {
                holder.setText(R.id.main_hot_shop_name,listData.getName());
                if (listData.getPrice().equals("0.0")){
                    String str = "无货";
                    SpannableStringBuilder style=new SpannableStringBuilder(str);
                    style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.red_light)),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
                    holder.setText(R.id.main_hot_shop_price, style);
                }else {
                    String str = "￥"+listData.getPrice()+"起";
                    SpannableStringBuilder style=new SpannableStringBuilder(str);
                    style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.red_light)),0,str.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
                    holder.setText(R.id.main_hot_shop_price, style);
                }

                holder.setImageUrl(R.id.main_hot_shop_image,listData.getSmall());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Contants.screenWidth/5,Contants.screenWidth/5);

            }
        };
        mRecyclerJx.setAdapter(jxAdapter);
        jxAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                webView("goodsId",listJx.get(position).getGoods_id(),"goodsDetail","goods");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    private void hotShop(){
        adapterHotShop = new CommonAdapter<HotGoodsMode.HotGoodsData.HotGoodsList>(mContext,R.layout.main_hot_shop_layout,listShop) {
            @Override
            protected void convert(ViewHolder holder, HotGoodsMode.HotGoodsData.HotGoodsList s, int position) {
                holder.setText(R.id.main_hot_shop_price,"￥"+s.getPrice()+"起");

                holder.setImageUrl(R.id.main_hot_shop_image,s.getSmall());
                holder.setText(R.id.main_hot_shop_name,s.getName());
            }
        };
//        mRecyclerHotShop.setAdapter(adapterHotShop);
        adapterHotShop.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                webView("goodsId",listShop.get(position).getGoods_id(),"goodsDetail","goods");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void loges(){

        adapterLoge = new CommonAdapter<RecommendBrandMode.RecommendBrandData.BrandData>(mContext,R.layout.loge_main_layout,listLoge) {
            @Override
            protected void convert(ViewHolder holder, RecommendBrandMode.RecommendBrandData.BrandData data, int position) {
                holder.setImageUrl(R.id.logo_main,data.getLogo());

            }
        };
        mRecyclerLoge.setAdapter(adapterLoge);
        adapterLoge.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                webView("brand",listLoge.get(position).getBrandId(),"brands_list","goods");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getBanner(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.BANNER, map, TAG, new SpotsCallBack<BannerMode>(mContext) {
            @Override
            public void onSuccess(Response response, BannerMode data) {
                banners.clear();
                banners.addAll(data.getData().getAdvLists());
                if (data.getData().getAdvLists().size()>1){
                    mBanner.delayTime(5).build(banners);
                }else {
                    mBanner.delayTime(10000).build(banners);
                }

            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //获取一级分类
    private void getCatThree(){
        OkHttpUtils.get().params(null).url(Contants.BASEURL+Contants.CATTREE)
                .build().execute(new ToCallBack<CatTreeMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(CatTreeMode data, int id) {
                listOne.clear();
                listOne.addAll(data.getData().getCatTreeList());
                funone();
            }
        });
    }


    //正品多样
    private void getRecommendBrand(){
        OkHttpUtils.get().params(null).url(Contants.BASEURL+Contants.RECOMMENDBRAND)
                .build().execute(new ToCallBack<RecommendBrandMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(RecommendBrandMode data, int id) {

                if (data.getResult().equals("1")){
                    listLoge.clear();
                    listLoge.addAll(data.getData().getBrandList());
                    loges();
                }

            }
        });
    }

    private void webView(String key,String v,String type,String module){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(key,v == null ?"":v);//其他参数
        map.put("module",module);
        map.put("type",type);
        map.put("memberId", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map).build()
                .execute(new ToCallBack<WebMode>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(WebMode data, int id) {
                        if (data.getData() != null){
                            Intent intent = new Intent(mContext, WebJS.class);
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
        childAll.clear();
        dataGoodsAll.clear();
        logAll.clear();
    }
    protected void onVisible() {
        //可见
        lazyLoad();
    }
    protected void lazyLoad() {
        if (!isEnter || !isPrepared) {
            return;
        }

        //懒加载进行数据的加载
        //banner
        getBanner();
        //获取一级分类
        getCatThree();
        //获取推荐品牌
//        getRecommendBrand();
        //获取热销Goods
//        getHotGoodsList();
        //获取精选
        getJx();
        //热推
//        getRt();
        getFloorList();
    }
    //精选
    private void getJx(){
        OkHttpUtils.get().url(Contants.BASEURL+Contants.Appexec).params(null).build().execute(new ToCallBack<JxMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(JxMode data, int id) {
                if (data.getResult().equals("1")){
                    listJx.clear();
                    listJx.addAll(data.getData().getList());
                    jxView();
                }
            }
        });
    }
    //热推
    private void getRt(){
        OkHttpUtils.get().url(Contants.BASEURL+Contants.AppHotGoods).params(null).build().execute(new ToCallBack<RtMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(RtMode data, int id) {
                if (data.getResult().equals("1")){
                    rt.clear();
                    rt.addAll(data.getData().getOneCatList());
                    reView();
                }
            }
        });
    }
    //获取 首页rt title + id + brand_ids
    private void getFloorList(){
        OkHttpUtils.get()
                .params(null)
                .url(Contants.BASEURL+Contants.FloorList)
                .build()
                .execute(new ToCallBack<FloorList>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(FloorList data, int id) {
                floorListDatas.clear();
                floorListDatas.addAll(data.getData().getList());
                titles.clear();
                ids.clear();
                for (int i=0;i<floorListDatas.size();i++){
                    titles.add(floorListDatas.get(i).getTitle());//正常
                    ids.add(floorListDatas.get(i).getId());//正常
                }
                childAll.clear();
                dataGoodsAll.clear();
                logAll.clear();
                idList.clear();
                idLists.clear();
                Log.e("ids==",ids.size()+"");
                for (int i=0;i<ids.size();i++){
                    getFloor(ids.get(i),ids.size(),i);//会乱
                    //获取 idlist 有序的
                    System.out.print(ids);//楼层id
                    getIdList(ids.get(i));
                }
            }
        });
    }
}
