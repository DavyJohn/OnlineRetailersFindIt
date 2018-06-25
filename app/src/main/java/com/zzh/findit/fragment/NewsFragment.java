package com.zzh.findit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mingle.widget.LoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.WebJS;
import com.zzh.findit.base.BaseFragment;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.CartMode;
import com.zzh.findit.mode.WebMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2018/3/15.
 */

public class NewsFragment extends BaseFragment {
    private static final String TAG = NewsFragment.class.getSimpleName();
    public NewsFragment instance;
    @BindView(R.id.news_tool)
    Toolbar mBar;
    @BindView(R.id.new_web)
    WebView mWebView;
    @BindView(R.id.news_loading)
    LoadingView mLoadView;

    private String url = null;
    private String cookies = null;
    private boolean isEnter = false;
    private boolean isPrepared;//初始化标志位
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_main_layout,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mBar.setTitle("");
        instance = this;
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBar);
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("cookie"))){
            cookies = SharedPreferencesUtil.getInstance(mContext).getString("cookie");
        }
        isPrepared = true;
        lazyLoad();
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
        Log.e("懒加载","不可见");
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
        getWebUrl();
    }
    private void initWebview(String url){
        if (!TextUtils.isEmpty(cookies)){
            syncCookie(url,cookies.substring(0,cookies.indexOf(";")));
        }

        // 设置可以访问文件
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        //如果不设置WebViewClient，请求会跳转系统浏览器
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100){
                    mLoadView.setVisibility(View.GONE);
                }
            }
        });

        //如果不设置WebViewClient，请求会跳转系统浏览器
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String startUrl, Bitmap favicon) {
                super.onPageStarted(view, startUrl, favicon);
                if (startUrl.contains("login.html?forward=")){
                    Intent intent = new Intent();
                    intent.setAction("BROADCAST_ACTION");
                    mContext.sendBroadcast(intent);
                }
            }

            @Override
            public void onPageFinished(WebView view, String endurl) {
                super.onPageFinished(view, endurl);
            }
        });

        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new WebAppInterface(getActivity()),"app");
    }
    public class WebAppInterface {
        Activity mActivity;
        WebAppInterface(Activity activity) {
            mActivity = activity;
        }
        @JavascriptInterface
        public void targetApp(String url){
            String ur = url;
            Intent intent = new Intent(mContext, WebJS.class);
            SharedPreferencesUtil.getInstance(mContext).putString("url", Contants.BASEURL+url);
            startActivity(intent);
        }

        @JavascriptInterface
        public void refreshCart(){
            Intent intent = new Intent();
            intent.setAction("BROADCAST_CARTLIST");
            getActivity().sendBroadcast(intent);
            mWebView.reload();
        }
    }
    //老版本不适合需要兼容修改如下
    public void syncCookie(String url,String cookie) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mWebView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(mWebView,true);
            cookieManager.setCookie(url, cookie);
            cookieManager.flush();  //强制立即同步cookie

        } else {
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);
            cookieSyncManager.sync();
        }
    }
    private void getWebUrl(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId",SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        map.put("module","user");
        map.put("type","message");
        OkHttpUtils.post().url(Contants.BASEURL+Contants.WEBAPI).params(map)
                .build().execute(new ToCallBack<WebMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(WebMode data, int id) {
                if (data.getResult().equals("1")){
                    url = data.getData().getWebUrl();
                    initWebview(url);
                }else if (data.getMessage().contains("用户id不能为空")){
                    //todo 游客模式
                    youke(mContext);
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        upDataCartList();
    }

    private void upDataCartList(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId", SharedPreferencesUtil.getInstance(mContext).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.CARTDATA).params(map).build().execute(new ToCallBack<CartMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(CartMode data, int id) {
                if (data.getResult().equals("1")){
                    if (data.getData().getCartList() != null ){
                        updataWeb();
                    }
                }

            }
        });
    }
    //更新webview
    public void updataWeb(){
        mWebView.reload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.setVisibility(View.GONE);
        mWebView.destroy();
    }
}
