package com.zzh.findit.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.CommonAdapter;
import com.zzh.findit.base.MultiItemTypeAdapter;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.base.ViewHolder;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.RecommendBrandMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.DividerItemDecoration;
import com.zzh.findit.widget.sidebar.WaveSideBar;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2017/9/14.
 */

public class MoreIconActivity extends BaseActivity {
    private static final String TAG = MoreIconActivity.class.getSimpleName();
    private List<RecommendBrandMode.RecommendBrandData.BrandData> brands = new LinkedList<>();
    private CommonAdapter<RecommendBrandMode.RecommendBrandData.BrandData> adapter;
    LinkedList<String> index = new LinkedList<String>();
    String[] data = null;
    @BindView(R.id.brand_recycler)
    RecyclerView mRecyclerview;
    @BindView(R.id.side_brand_bar)
    WaveSideBar mSideBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle("全部品牌");
        MyApplication.getInstance().add(this);
        moreIocn();

    }

    private void initView(){

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.HORIZONTAL));
        adapter = new CommonAdapter<RecommendBrandMode.RecommendBrandData.BrandData>(mContext,R.layout.side_item_layout,brands) {
            @Override
            protected void convert(ViewHolder holder, RecommendBrandMode.RecommendBrandData.BrandData data, int position) {
                if (position == 0 || !brands.get(position-1).getInitials().equals(data.getInitials())){
                    holder.setGone(R.id.tv_index,true);
                    holder.setText(R.id.tv_index,data.getInitials());
                }else {
                    holder.setGone(R.id.tv_index,false);
                }
                holder.setText(R.id.tv_name,data.getName());
            }
        };
        mRecyclerview.setAdapter(adapter);
        data = index.toArray(new String[index.size()]);
        mSideBar.setIndexItems(data);
        mSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String s) {
                for (int i = 0;i<brands.size();i++){
                    if (brands.get(i).getInitials().equals(s)){
                        ((LinearLayoutManager) mRecyclerview.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                webView("brand",brands.get(position).getBrandId(),"brands_list");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.side_main_layout;
    }
    //更多icon
    private void moreIocn(){
        OkHttpUtils.get().url(Contants.BASEURL+Contants.ALLBRAND).params(null).build().execute(new ToCallBack<RecommendBrandMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(RecommendBrandMode data, int id) {
                if (data.getResult().equals("1")){
                    brands.addAll(data.getData().getBrandList());
                    index.clear();
                    for (int i=0;i<data.getData().getBrandList().size();i++){
                        if (i<data.getData().getBrandList().size()-1){
                            if (data.getData().getBrandList().get(i).getInitials().equals(data.getData().getBrandList().get(i+1).getInitials())){
                                //不做操作
                            }else {
                                index.add(data.getData().getBrandList().get(i).getInitials());
                            }
                        }else {
                            index.add(data.getData().getBrandList().get(i).getInitials());
                        }
                    }
                    initView();
                }
            }
        });
    }
    private void webView(String key,String catid,String type){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(key,catid == null ?"":catid);
        map.put("module","goods");
        map.put("type",type);
        map.put("memberId", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map).build()
                .execute(new ToCallBack<WebMode>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(WebMode data, int id) {
                        if (data.getData()!= null){
                            Intent intent = new Intent(mContext, WebJS.class);
                            SharedPreferencesUtil.getInstance(mContext).putString("url",data.getData().getWebUrl());
                            startActivity(intent);
                        }else if (data.getMessage().contains("用户id不能为空")){
                            //todo 游客模式
                            youke(mContext);
                        }else {
                            showToast(data.getMessage());
                        }


                    }
                });
    }
}
