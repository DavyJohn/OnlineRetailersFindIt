package com.zzh.findit.base;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzh.findit.R;

public class ToolBarX {
public Toolbar mToolBar;
private AppCompatActivity mActivity;
private ActionBar mActionBar;
private RelativeLayout mRlCustom;
private TextView mTitleView;
private View view;

public ToolBarX(Toolbar mToolBar, final AppCompatActivity mActivity) {
    this.mToolBar = mToolBar;
    this.mActivity = mActivity;
    this.mRlCustom = (RelativeLayout) mToolBar.findViewById(R.id.rlCustom);
    this.mActivity.setSupportActionBar(mToolBar);
    mActionBar = mActivity.getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    mActionBar.setTitle("");

    mTitleView = new TextView(mActivity);
    //标题加粗---------------------------
//    TextPaint tp = mTitleView.getPaint();
//    tp.setFakeBoldText(true);
    //----------------------------------
    mTitleView.setTextSize(20);
    mTitleView.setTextColor(mActivity.getResources().getColor(R.color.black));
    ActionBar.LayoutParams params = new ActionBar.LayoutParams(-2, -2);
    params.gravity = Gravity.CENTER;
    mActionBar.setCustomView(mTitleView, params);
    mActionBar.setDisplayShowCustomEnabled(true);
    mToolBar.setNavigationIcon(R.drawable.ic_backback);
    mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
        }
    });

}

public ToolBarX setTitle(String title) {
    mTitleView.setText(title);
    return this;
}

public ToolBarX setTitle(int resId) {
    setTitle(mActivity.getString(resId));
    return this;
}

public ToolBarX setTitleColor(int color) {
    mTitleView.setTextColor(mActivity.getResources().getColor(color));
    return this;
}

/**
 * 是否可见
 * @param flag
 * @return
 */
public ToolBarX setDisplayHomeAsUpEnabled(boolean flag) {
    mActionBar.setDisplayHomeAsUpEnabled(flag);
    return this;
}


public ToolBarX setNavigationIcon(int resId) {
    mToolBar.setNavigationIcon(resId);
    return this;
}

public ToolBarX setNavigationOnClickListener(View.OnClickListener listener) {
    mToolBar.setNavigationOnClickListener(listener);
    return this;
}

public ToolBarX setCustomView(View view) {
    mRlCustom.removeAllViews();
    mRlCustom.addView(view);
    return this;
}


}
