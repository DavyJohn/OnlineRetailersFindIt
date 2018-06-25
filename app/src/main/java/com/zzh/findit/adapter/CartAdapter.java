package com.zzh.findit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.findit.R;
import com.zzh.findit.mode.CartMode;
import com.zzh.findit.widget.SnappingStepper;
import com.zzh.findit.widget.listener.SnappingStepperValueChangeListener;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/7/18.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    private OnResfreshListener mOnResfreshListener;
    private OnEditClickListener mOnEditClickListener;
    private LinkedList<CartMode.CartData.CartList> cartlistBeen = new LinkedList<>();
    public CartAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addCart(LinkedList<CartMode.CartData.CartList> cartlist){
        cartlistBeen.clear();
        cartlistBeen.addAll(cartlist);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_item_main_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mPrice.setText(cartlistBeen.get(position).getPrice());
        holder.mShopName.setText(cartlistBeen.get(position).getName());
        final Double price = Double.parseDouble(holder.mPrice.getText().toString().substring(1,holder.mPrice.getText().toString().length()));
        Picasso.with(context).load(cartlistBeen.get(position).getImage_default()).placeholder(R.mipmap.ic_launcher).into(holder.mShopImage);
        if (onClickItemListener != null){
            holder.snapping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItem(holder.snapping,position,price);
                }
            });
        }
        holder.snapping.setValue(Integer.valueOf(cartlistBeen.get(position).getNum()));
        holder.snapping.setOnValueChangeListener(new SnappingStepperValueChangeListener() {
            @Override
            public void onValueChange(View view, int value) {
                System.out.print(value);
                int va= holder.snapping.getValue();
                System.out.print(holder.snapping.getValue());
                if (mOnEditClickListener != null ){
                    mOnEditClickListener.onEditClick(position,value);
                }

                cartlistBeen.get(position).setNum(value+"");

                //跟新购物车数量数据
                notifyDataSetChanged();
            }
        });


        if(cartlistBeen.get(position).isSelect()){
            holder.mSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_selected));
        }else {
            holder.mSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_unselected));
        }
        holder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartlistBeen.get(position).setSelect(!cartlistBeen.get(position).isSelect());
               notifyDataSetChanged();
            }
        });

        if(mOnResfreshListener != null){
            boolean isSelect = false;
            for(int i = 0;i < cartlistBeen.size(); i++){
                if(!cartlistBeen.get(i).isSelect()){
                    isSelect = false;
                    break;
                }else{
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }
    }

    @Override
    public int getItemCount() {
        return cartlistBeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.snapping)
        SnappingStepper snapping;
        @BindView(R.id.shop_image)
        ImageView mShopImage;
        @BindView(R.id.unit_price)
        TextView mPrice;
        @BindView(R.id.cart_shop_title)
        TextView mShopName;
        @BindView(R.id.tv_item_shopcart_clothselect)
        ImageView mSelect;
        @BindView(R.id.image_delete)
        public ImageView iSchedule;
        public View vBackground; // 背景
        public View vItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            vBackground = itemView.findViewById(R.id.linear_background);
            vItem = itemView.findViewById(R.id.cart_content);
            iSchedule.setImageResource(R.drawable.delete_icon);

        }
    }
    public interface OnClickItemListener{
        void onClickItem(SnappingStepper stepper, int postion, Double d);
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
    public interface OnResfreshListener{
        void onResfresh(boolean isSelect);
    }
    public void setResfreshListener(OnResfreshListener mOnResfreshListener){
        this.mOnResfreshListener = mOnResfreshListener;
    }

    public interface OnEditClickListener{
        void onEditClick(int position, int count);
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener){
        this.mOnEditClickListener = mOnEditClickListener;
    }

}
