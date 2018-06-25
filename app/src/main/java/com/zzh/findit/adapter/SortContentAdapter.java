package com.zzh.findit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.activitys.LoginActivity;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.CatTreeMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2017/7/21.
 */

public class SortContentAdapter extends RecyclerView.Adapter<SortContentAdapter.ViewHolder> {
    private Context context;
    private ItemAdapter adapter;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private List<CatTreeMode.CatTreeData.CatTreeListData.Children> datas = new LinkedList<>();
    private List<CatTreeMode.CatTreeData.CatTreeListData.Children.TWOChildrenData> mTipsThree = new LinkedList<>();
    public SortContentAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void addAllData(List<CatTreeMode.CatTreeData.CatTreeListData.Children> list){
        datas.clear();
        datas.addAll(list);
        notifyDataSetChanged();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sort_content_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTips.setText(datas.get(position).getName());
        holder.mRecycler.setHasFixedSize(true);
        holder.mRecycler.setLayoutManager(new GridLayoutManager(context,2));
        holder.mRecycler.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL,2,ContextCompat.getColor(context,R.color.decoration_color)));
        holder.mRecycler.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL, 2,ContextCompat.getColor(context,R.color.decoration_color)));
        adapter = new ItemAdapter(context);
        holder.mRecycler.setAdapter(adapter);
        mTipsThree.addAll( datas.get(position).getChildren());
        adapter.addTips(mTipsThree);//三级的数据

        adapter.setOnClickItemListener(new ItemAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(int p, String s) {
                //
                webView(mTipsThree.get(p).getCat_id());
//                Toast.makeText(context,s+p+holder.mTips.getText().toString()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sort_content_items_tips)
        TextView mTips;
        @BindView(R.id.sort_content_items_recycler)
        RecyclerView mRecycler;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

        private Context context;
        private LayoutInflater inflater;
        private List<CatTreeMode.CatTreeData.CatTreeListData.Children.TWOChildrenData> list = new LinkedList<>();
        public ItemAdapter(Context context){
            this.context = context;
            inflater = LayoutInflater.from(context);
        }
        public void addTips(List<CatTreeMode.CatTreeData.CatTreeListData.Children.TWOChildrenData> data ){
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
        @Override
        public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.sort_content_item_recycler_item_layout,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ItemAdapter.ViewHolder holder, final int position) {
            holder.mText.setText(list.get(position).getName());
            if (onClickItemListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickItemListener.onClickItem(position,holder.mText.getText().toString());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.sort_content_recycler_item_tips)
            TextView mText;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
        public interface OnClickItemListener{
            void onClickItem( int postion, String s);
        }

        public OnClickItemListener onClickItemListener;

        public void setOnClickItemListener(OnClickItemListener onClickItemListener){
            this.onClickItemListener = onClickItemListener;
        }

    }
    private void webView(String catid){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("cat",catid);
        map.put("module","goods");
        map.put("type","goods-list");
        map.put("memberId", SharedPreferencesUtil.getInstance(context).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map).build()
                .execute(new ToCallBack<WebMode>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(WebMode data, int id) {
                        if (data.getResult().equals("1")){

                            if (data.getMessage() != null && TextUtils.isEmpty(data.getMessage()) ){
                                if (data.getMessage().contains("用户id不能为空")){
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
                                } else {
                                    Intent intent = new Intent(context, WebJS.class);
                                    SharedPreferencesUtil.getInstance(context).putString("url",data.getData().getWebUrl());
                                    intent.putExtra("url",data.getData().getWebUrl());
                                    context.startActivity(intent);
                                }
                            }else {
                                Intent intent = new Intent(context, WebJS.class);
                                SharedPreferencesUtil.getInstance(context).putString("url",data.getData().getWebUrl());
                                intent.putExtra("url",data.getData().getWebUrl());
                                context.startActivity(intent);
                            }


                        }



                    }
                });
    }
}
