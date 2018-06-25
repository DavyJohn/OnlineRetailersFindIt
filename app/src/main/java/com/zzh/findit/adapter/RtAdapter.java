package com.zzh.findit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.activitys.LoginActivity;
import com.zzh.findit.base.CommonAdapter;
import com.zzh.findit.base.MultiItemTypeAdapter;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.Floor;
import com.zzh.findit.mode.FloorGoodsList;
import com.zzh.findit.mode.FloorList;
import com.zzh.findit.mode.LogData;
import com.zzh.findit.mode.RecommendBrandMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.mode.collectMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.DividerItemDecoration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2018/2/28.
 */

public class RtAdapter extends RecyclerView.Adapter<RtAdapter.ViewHolder> {
    private static final String TAG = RtAdapter.class.getSimpleName();
    private Context context;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private String cookies = null;
    //logo
    private CommonAdapter<LogData.Logdata.LogList> adapterLoge;
    private LinkedList<RecommendBrandMode.RecommendBrandData.BrandData> rtLogoData = new LinkedList<>();

    private CommonAdapter<FloorGoodsList.FloorGoodsListData.FloorGoodsData> adapter;
    private LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData> dataGoods = new LinkedList<>();
    private LinkedList<String> titles = new LinkedList<>();//小标题
    private LinkedList<String> ids = new LinkedList<>();//ids xiaobiaoti
    private LinkedList<FloorList.FloorListData.FloorListDatas> floorListDatas = new LinkedList<>();
    private LinkedList<Floor.FloorData.FloorMap.ChildFloors> childsGoods = new LinkedList<>();
    private LinkedList<LinkedList<Floor.FloorData.FloorMap.ChildFloors>> childAll = new LinkedList<>();
    private LinkedList<LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData>> dataGoodsAll = new LinkedList<>();
    private LinkedList<LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData>> GoodsAll = new LinkedList<>();
    private LinkedList<LinkedList<LogData.Logdata.LogList>> logs = new LinkedList<>();
    private LinkedList<LinkedList<String>> idLists = new LinkedList<LinkedList<String>>();
    //第二个接口第一个child 里面的 goods-ids
    LinkedList<String> goodsid = new LinkedList<>();
    com.tencent.smtt.sdk.CookieSyncManager cookieSyncManager ;
    com.tencent.smtt.sdk.CookieManager cookieManager ;

    public RtAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(LinkedList<String> title,LinkedList<String> id,LinkedList<LinkedList<Floor.FloorData.FloorMap.ChildFloors>> data1,LinkedList<LinkedList<FloorGoodsList.FloorGoodsListData.FloorGoodsData>> data2
            ,LinkedList<LinkedList<LogData.Logdata.LogList>> data3,LinkedList<LinkedList<String>> data4){
        titles.clear();
        ids.clear();
        logs.clear();
        GoodsAll.clear();
        childAll.clear();
        dataGoodsAll.clear();
        idLists.clear();
        this.ids = id;
        this.titles = title;
        this.childAll = data1;
        this.dataGoodsAll = data2;
        this.logs  = data3;
        this.idLists = data4;
        //需要给dataGoodsAll排序
        System.out.print(idLists);
//        Log.e("childAll==",childAll.size()+"");
//        Log.e("dataGoodsAll==",dataGoodsAll.size()+"");
//        Log.e("idLists==",idLists.size()+"");// 错误idLists出现变化
        for (int i=0;i<titles.size();i++){
            for (int n=0;n<6;n++){
                for (int m=0;m<idLists.get(i).size();m++){
                    if (idLists.get(i).get(m).equals(dataGoodsAll.get(n).get(0).getGoods_id())){
                        GoodsAll.add(dataGoodsAll.get(n));
                    }
                }

            }
        }
        System.out.print(GoodsAll);
        Log.e("GoodsAll==",GoodsAll+"");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rt_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            //第一层
        holder.mTips.setText(titles.get(position));//小标题
           //先排序
            //第二层 goods
        Log.e("GoodsAll==",GoodsAll.size()+"");
        if (GoodsAll.size() == titles.size()){

            adapter = new CommonAdapter<FloorGoodsList.FloorGoodsListData.FloorGoodsData>(context,R.layout.main_rt_layout,GoodsAll.get(position)) {
                @Override
                protected void convert(com.zzh.findit.base.ViewHolder holder, final FloorGoodsList.FloorGoodsListData.FloorGoodsData hotGoodsListData, final int pos) {

                    holder.setText(R.id.main_rt_name,GoodsAll.get(position).get(pos).getName());
                    String str = "￥"+GoodsAll.get(position).get(pos).getPrice()+"起";
                    SpannableStringBuilder style=new SpannableStringBuilder(str);
                    style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.red_light)),0,str.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
                    holder.setText(R.id.main_rt_price,style);
                    holder.setImageUrl(R.id.main_rt_image,GoodsAll.get(position).get(pos).getSmall());
                    holder.setOnClickListener(R.id.main_rt_zan, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //抽藏
                            addCollect(dataGoodsAll.get(position).get(pos).getGoods_id());
                        }
                    });
                }
            };
            holder.rtItem.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int pos) {
                    //点击事件
                    webView("goodsId",dataGoodsAll.get(position).get(pos).getGoods_id(),"goodsDetail","goods");
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }

        //第二层logo
        if (logs.size() == titles.size()){
            Log.e("logs==",logs.size()+"");

            adapterLoge = new CommonAdapter<LogData.Logdata.LogList>(context,R.layout.loge_main_layout,logs.get(position)) {
                @Override
                protected void convert(com.zzh.findit.base.ViewHolder holder, LogData.Logdata.LogList data, int pos) {
                    holder.setImageUrl(R.id.logo_main,logs.get(position).get(pos).getLogo());

                }
            };
            holder.rtLogo.setAdapter(adapterLoge);
            adapterLoge.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int pos) {
                    webView("brand",logs.get(position).get(pos).getBrand_id(),"brands_list","goods");
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        Log.e("postions==",titles.size()+"");
        return titles.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rt_item_recycler)
        public RecyclerView rtItem;
        @BindView(R.id.rt_tips)
        TextView mTips;
        @BindView(R.id.rt_hot_logo)
        RecyclerView rtLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            rtItem.setHasFixedSize(true);
            rtLogo.setHasFixedSize(true);
            rtLogo.addItemDecoration(new DividerItemDecoration(context, LinearLayout.HORIZONTAL));
            rtLogo.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
            rtLogo.setLayoutManager(new GridLayoutManager(context,5));
            rtItem.setLayoutManager(new GridLayoutManager(context,2));
            rtItem.addItemDecoration(new DividerItemDecoration(context, LinearLayout.HORIZONTAL));
            rtItem.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
        }
    }

    //获取 logo 数据
    private void webView(String key,String v,String type,String module){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(key,v == null ?"":v);//其他参数
        map.put("module",module);
        map.put("type",type);
        map.put("memberId", SharedPreferencesUtil.getInstance(context).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map).build()
                .execute(new ToCallBack<WebMode>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(WebMode data, int id) {
                        if (data.getData() != null){
                            Intent intent = new Intent(context, WebJS.class);
                            SharedPreferencesUtil.getInstance(context).putString("url",data.getData().getWebUrl());
                            context.startActivity(intent);
                        }else if (data.getMessage().contains("用户id不能为空")){
                            //todo 游客模式
                            dialog = new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("处于游客模式,请前往注册登录")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();

                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            context.startActivity(new Intent(context, LoginActivity.class));
                                        }
                                    }).create();
                            dialog.show();
                        }else {
                        }
                    }
                });
    }
    //收藏
    private void addCollect(String id){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("goods_id",id);
        map.put("memberId",SharedPreferencesUtil.getInstance(context).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.collect).params(map).build().execute(new ToCallBack<collectMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(collectMode data, int id) {
                Toast.makeText(context,data.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
