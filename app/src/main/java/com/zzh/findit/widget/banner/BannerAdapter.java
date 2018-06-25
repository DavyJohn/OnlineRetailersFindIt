package com.zzh.findit.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import com.zzh.findit.adapter.CartAdapter;
import com.zzh.findit.widget.SnappingStepper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcc on 16/8/7 21:18
 * 100332338@qq.com
 * <p/>
 * Banner适配器
 */
public class BannerAdapter extends PagerAdapter
{

    private List<ImageView> mList = new ArrayList<>();

    private int pos;
    private Context context;
    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;

    public void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener)
    {

        this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;
    }

    public BannerAdapter(List<ImageView> list,Context context)
    {
        mList.clear();
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount()
    {
        //获取数据条数
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {

        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ImageView v = mList.get(position);
        v.setScaleType(ImageView.ScaleType.FIT_XY);
        ((ViewPager) container).addView(v);
        ((ViewPager) container).setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //点击图片
        v.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if (mViewPagerOnItemClickListener != null)
                {
                    mViewPagerOnItemClickListener.onItemClick(pos);
                }
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(mList.get(position % mList.size()));
    }


    public interface ViewPagerOnItemClickListener
    {

        void onItemClick(int position);
    }

}
