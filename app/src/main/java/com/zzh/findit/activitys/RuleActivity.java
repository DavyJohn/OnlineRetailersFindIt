package com.zzh.findit.activitys;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzh.findit.R;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2018/2/12.
 * 注册规则
 */

public class RuleActivity extends BaseActivity{
    private static final String TAG = RuleActivity.class.getSimpleName();
    @BindView(R.id.rule_view)
    WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getAppContext().add(this);
        getToolBar().setTitle("智造库注册协议");
        mWeb.loadUrl("file:///android_asset/agreement.html");
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //支持App内部javascript交互
        mWeb.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        mWeb.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        mWeb.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        mWeb.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.rule_main_layout;
    }
}
