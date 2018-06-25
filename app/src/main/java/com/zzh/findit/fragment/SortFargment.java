package com.zzh.findit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.adapter.SortContentAdapter;
import com.zzh.findit.adapter.SortTipsAdapter;
import com.zzh.findit.base.BaseFragment;
import com.zzh.findit.base.CommonAdapter;
import com.zzh.findit.base.MultiItemTypeAdapter;
import com.zzh.findit.base.ViewHolder;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.CatTreeMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2017/7/7.
 */

public class SortFargment extends BaseFragment {
    private static final String TAG = SortFargment.class.getSimpleName();
    private LinkedList<CatTreeMode.CatTreeData.CatTreeListData> mTips = new LinkedList<>();
    private LinkedList<CatTreeMode.CatTreeData.CatTreeListData.Children> mTipsTwo = new LinkedList<>();
    @BindView(R.id.sort_tool)
    Toolbar mBar;
    @BindView(R.id.sort_recycler)
    RecyclerView mTipRecycler;
    @BindView(R.id.sort_content_recycler)
    RecyclerView mContentRecycler;
    @BindView(R.id.sort_search_view)
    TextView mSearch;
    @OnClick(R.id.sort_search_view) void search(){
        webView("cat",null,"search-app");
    }
    @OnClick(R.id.sort_message) void message(){
        webView("","","message_list");
    }
    private SortTipsAdapter mTipsAdapter;
    private SortContentAdapter mContentAdapter;//二级 三级

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sort_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mBar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBar);
        getCatThree();
        mTipRecycler.setHasFixedSize(true);
        mContentRecycler.setHasFixedSize(true);
        mContentRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mTipRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mTipRecycler.addItemDecoration(new DividerItemDecoration(mContext,LinearLayoutManager.HORIZONTAL,2, ContextCompat.getColor(mContext,R.color.decoration_color)));

    }

    private void initContent(int postion){
        if (mTips.size() != 0){
            mContentAdapter = new SortContentAdapter(mContext);
            mContentRecycler.setAdapter(mContentAdapter);
            //传入一级
            mContentAdapter.addAllData(mTips.get(postion).getChildren());
        }

    }
    //获取分类
    private void getCatThree(){
        OkHttpUtils.get().params(null).url(Contants.BASEURL+Contants.CATTREE)
                .build().execute(new ToCallBack<CatTreeMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(CatTreeMode data, int id) {
                mTips.clear();
                mTipsTwo.clear();
                mTips.addAll(data.getData().getCatTreeList());//拿到所有数据
                mTipsAdapter = new SortTipsAdapter(mContext);
                mTipsAdapter.addTips(mTips);
                mTipRecycler.setAdapter(mTipsAdapter);
                //初始化二级三级数据
                initContent(0);
                mTipsAdapter.setOnClickItemListener(new SortTipsAdapter.OnClickItemListener() {
                    @Override
                    public void onClickItem(int postion) {
                        mTipsAdapter.addstatus(postion);
                        initContent(postion);
                    }
                });
            }
        });
    }
    private void webView(String key,String v,String type){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(key,v == null ?"":v);//其他参数
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

                        if (data.getData() != null){
                            Intent intent = new Intent(mContext, WebJS.class);
                            SharedPreferencesUtil.getInstance(mContext).putString("url",data.getData().getWebUrl());
                            startActivity(intent);
                        }else if (data.getMessage().contains("用户id不能为空")){
                            //todo 游客模式
                            youke(mContext);
                        }else {
                            showToast(data.getMessage(),mContext);
                        }


                    }
                });
    }
}
