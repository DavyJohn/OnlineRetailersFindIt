package com.zzh.findit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzh.findit.R;
import com.zzh.findit.mode.CatTreeMode;
import com.zzh.findit.widget.SnappingStepper;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/7/21.
 */

public class SortTipsAdapter extends RecyclerView.Adapter<SortTipsAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private int mTextColor,mViewBg,mVb,p;

    private LinkedList<CatTreeMode.CatTreeData.CatTreeListData> mTips = new LinkedList<>() ;
    public SortTipsAdapter(Context context){
        this.context =context;
        inflater = LayoutInflater.from(context);
    }
    public void addTips(LinkedList<CatTreeMode.CatTreeData.CatTreeListData> datas){
        mTips.clear();
        mTips.addAll(datas);
        notifyDataSetChanged();
    }
    public void addstatus(int postion){
        p = 0;
        p = postion;
        notifyDataSetChanged();

    }
    @Override
    public SortTipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sort_recycler_items_main_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SortTipsAdapter.ViewHolder holder, final int position) {
        holder.mTitles.setText(mTips.get(position).getName());

        if (position == p){
            holder.mRoot.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.mView.setVisibility(View.VISIBLE);
            holder.mTitles.setTextColor(ContextCompat.getColor(context,R.color.main_bottom_sel_color));
        }else {
            //初始化
            holder.mRoot.setBackgroundColor(ContextCompat.getColor(context,R.color.boot_view));
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mTitles.setTextColor(ContextCompat.getColor(context,R.color.ites_tips_color));
        }

        if (onClickItemListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItem(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sort_items_tips)
        TextView mTitles;
        @BindView(R.id.sort_content)
        LinearLayout mRoot;
        @BindView(R.id.sort_item_view)
        View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //
    public interface OnClickItemListener{
        void onClickItem( int postion);
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
