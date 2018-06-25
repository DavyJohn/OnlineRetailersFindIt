package com.zzh.findit;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.mingle.widget.LoadingView;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.zzh.findit.activitys.LoginActivity;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.Downloader;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.ActionSheetDialog;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * Created by 腾翔信息 on 2017/8/8.
 */

public class WebJS extends BaseActivity {

    public static WebJS instans;
    @BindView(R.id.loadView)
    LoadingView mLoadView;
    @BindView(R.id.webFrameLayout)
    FrameLayout mLayout;
    com.tencent.smtt.sdk.WebView webView;
    AlertDialog dialog;
    private String url = null;
    private String shareUrl = null;
    private String cookies = null;
    private static Boolean isBack = false;
    private static final int REQUEST_CODE_TAKE_PICETURE = 11;
    private static final int REQUEST_CODE_PICK_PHOTO = 12;
    private static final int REQUEST_CODE_PERMISSION = 13;
    public static ValueCallback mFilePathCallback;
    private File picturefile;
    com.tencent.smtt.sdk.CookieSyncManager cookieSyncManager ;
    com.tencent.smtt.sdk.CookieManager cookieManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        hasToolBar(false);
        instans = this;
        isBack = false;
        webView = new WebView(WebJS.this);// 必须使用当前ACTIVITY 的context 不然无法使用 H5 的select 弹出框  
        mLayout.addView(webView,0);
        cookieSyncManager = com.tencent.smtt.sdk.CookieSyncManager.createInstance(webView.getContext());
        cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
        url = SharedPreferencesUtil.getInstance(mContext).getString("url");
        if (url.contains(":80/")){
            url = url.replace(":80","");
            initView(url);
        }else {
            initView(url);
        }
    }

    private void initView(String weburl){
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("cookie"))){
            cookies = SharedPreferencesUtil.getInstance(mContext).getString("cookie");
        }
        if (!TextUtils.isEmpty(cookies)){
            syncCookie(weburl,cookies.substring(0,cookies.indexOf(";")));
        }
        //// 修改ua使得web端正确判断
        final String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua+"; zzkonline-android");
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true); // 显示放大缩小
        webView.getSettings().setSupportZoom(true); // 可以缩放
        webView.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);

        //如果不设置WebViewClient，请求会跳转系统浏览器

        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient(){
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100){
                    mLoadView.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                System.out.print(s1);
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
                System.out.print(s1);
                return super.onJsConfirm(webView, s, s1, jsResult);
            }

            @Override
            public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
                System.out.print(s1);
                return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
            }

            @Override
            public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.sdk.ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                showDialog();
                mFilePathCallback = valueCallback;

                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {// android 系统版本>4.1.1
                showDialog();
                mFilePathCallback = uploadMsg;
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {//android 系统版本<3.0
                showDialog();
                mFilePathCallback = uploadMsg;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {//android 系统版本3.0+
                showDialog();
                mFilePathCallback = uploadMsg;
            }
        });

        webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
                return super.shouldInterceptRequest(webView, s);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                Uri url = webResourceRequest.getUrl();
                if (!TextUtils.isEmpty(cookies)){
                    syncCookie(url+"",cookies.substring(0,cookies.indexOf(";")));
                }
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }
            @Override
            public boolean shouldOverrideUrlLoading(final com.tencent.smtt.sdk.WebView view, String url) {
                //分享
                if (url.substring(0,16).contains("zzkonlineshare")){
                    view.stopLoading();
                    //需要申请存储权限
                    shareUrl = url;
                    final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(WebJS.this, permission);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(WebJS.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 333);
                    }else {
                        showShARE(url);
                    }
                }
                if (url.contains("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb")){
                    //截取支付 返回
                    String urlBack = url.substring(url.indexOf("redirect_url")+13,url.length());
                    try {
                        String text2 =  URLDecoder.decode(urlBack,"utf-8");
                        System.out.print(text2);
                        SharedPreferencesUtil.getInstance(WebJS.this).putString("zhifu_url",text2);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //pdf文件判断
                if (!url.substring(0,16).contains("zzkonlineshare") &&url.contains("bkt.clouddn.com")){
                    //通过外部浏览器方式
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
//                    try {
//                        URL pdfurl = new URL(url);
//                        String path = Environment.getExternalStorageDirectory()
//                                + "/Download/";
//                        String[] splitStrings = url.split("/");
//                        String[] nameStrings = splitStrings[splitStrings.length - 1]
//                                .split(".pdf");
//
//                        String fileName = nameStrings[0] + ".pdf";//当pdf的url带签名时，文件名过长下载会失败
//                        File file = new File(path + fileName);
//
//                        ExecutorService executorService = Executors
//                                .newSingleThreadExecutor();
//                        Downloader downloader = new Downloader();
//                        downloader.setUrlAndFile(pdfurl, file);
//                        File downloadFile = executorService.submit(downloader)
//                                .get();
//                        // loadingDialog.dismiss();
//                        Uri path1 = Uri.fromFile(downloadFile);
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        intent.setDataAndType(path1, "application/pdf");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                        try {
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            showToast( "打开失败");
//                        }
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
                }
                //alipay判断
                if (url.contains("mclient.alipay.com")){
                    final PayTask task = new PayTask(WebJS.this);
                    boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                        @Override
                        public void onPayResult(H5PayResultModel result) {
                            final String url = result.getReturnUrl();
                            if (!TextUtils.isEmpty(url)){
                                WebJS.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.loadUrl(url);
                                    }
                                });
                            }
                        }
                    });
                    if (!isIntercepted) {
                        view.loadUrl(url);
                    }else {
                        //成功连接
                        if (view.canGoBack()){
                            view.goBack();
                        }else {
                            finish();
                        }
                    }
                    return true;
                }
                if (url.contains("http://m.zzkonline.com/login.html?forward=/member.html")){
                    youke(mContext);

                }
                //微信判断
                if (url.startsWith("weixin://wap/pay?")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                com.tencent.smtt.sdk.WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult==null解决重定向问题
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    if (openWithWebView(url) && !url.contains("login.html?forward=")){
                        view.loadUrl(url);
                    }
                }

                return false;

            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String startUrl, Bitmap favicon) {
                super.onPageStarted(view, startUrl, favicon);
                //适配vivo 分享
                if (startUrl.substring(0,16).contains("zzkonlineshare")){
                    url = startUrl.substring(startUrl.indexOf("&url=")+5,startUrl.length());
                    view.loadUrl(url);
                }
                if (startUrl.contains("login.html?forward=")){
                    url  = startUrl.replace("/login.html?forward=","");
                }
                //忽略第一次安装 nick == "" 这个细节
                if (startUrl.contains("login.html?forward=") && SharedPreferencesUtil.getInstance(mContext).getString("nickname").equals("游客模式")){
                    dialog = new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("处于游客模式,请前往注册登录")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    if (webView.canGoBack()){
                                        webView.goBack();
                                    }else {
                                        finish();
                                    }

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(mContext,LoginActivity.class));
                                }
                            }).create();
                    dialog.show();

                }else if (startUrl.contains("login.html?forward=") && !SharedPreferencesUtil.getInstance(mContext).getString("nickname").equals("游客模式")){
                    //cookie 失效处理
                    //通知广播 率刷新cokie 还是有点小bug 要判断当前是否销毁
                    if (isFinishing()){

                    }else {
                        Intent intent = new Intent();
                        intent.setAction("BROADCAST_ACTION");
                        sendBroadcast(intent);
                    }

                }
            }


            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String endurl) {
                super.onPageFinished(view, endurl);
                if (endurl.contains("login.html?forward=")){
                    view.setVisibility(View.GONE);
                }else {
                    view.setVisibility(View.VISIBLE);
                }

            }
        });
        webView.loadUrl(weburl);
        webView.addJavascriptInterface(new WebAppInterface(this),"app");
    }

    public  class WebAppInterface {
        Activity mActivity;
        public WebAppInterface(Activity activity) {
            mActivity = activity;
        }
        @JavascriptInterface
        public void backApp() {
            //TODO 处理代码
            isBack = true;
            finish();
        }

        @JavascriptInterface
        public void backCart(){
            Intent intent = new Intent(mContext, MainActivity.class);
            SharedPreferencesUtil.getInstance(mContext).putString("cart","cart");
            intent.putExtra("cart","cart");
            startActivity(intent);
        }

        @JavascriptInterface
        public void backLoginOut(){
            SharedPreferencesUtil.getInstance(mContext).putString("cookie","");
            SharedPreferencesUtil.getInstance(mContext).putString("memberid","");
            SharedPreferencesUtil.getInstance(mContext).putString("nickname","游客模式");
            startActivity(new Intent(mContext, LoginActivity.class));
            Contants.isLogin = false;
        }

    }

    public void initUpdata(){
        //webView.reload();
        //重新加载 url 需要获取当前失效的url
        initView(url);
    }
    public void syncCookie(String url,String cookie) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
            cookieManager.setCookie(url, cookie);
            cookieManager.flush();  //强制立即同步cookie

        } else {
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);
            cookieSyncManager.sync();
        }
    }

    @Override
    public void onBackPressed() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();//
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.webjs_main_layout;
    }

    protected boolean openWithWebView(String url) {//处理判断url的合法性
// TODO Auto-generated method stub
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return true;
        }
        return false;
    }
    private void showDialog() {
        final String permission = Manifest.permission.CAMERA;  //相机权限
        final String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE; //写入数据权限
        ActionSheetDialog dialog = new ActionSheetDialog(WebJS.this).builder()
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        //检测 权限问题
                        if (Build.VERSION.SDK_INT > 23){
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(WebJS.this, permission);
                            int checkCallPhonePermission1 = ContextCompat.checkSelfPermission(WebJS.this,permission1);
                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkCallPhonePermission1 != PackageManager.PERMISSION_GRANTED ){
                                boolean d = ActivityCompat.shouldShowRequestPermissionRationale(WebJS.this, permission) ;
                                boolean b = ActivityCompat.shouldShowRequestPermissionRationale(WebJS.this, permission1) ;

                                if (ActivityCompat.shouldShowRequestPermissionRationale(WebJS.this, permission)){
                                    ActivityCompat.requestPermissions(WebJS.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                                }else {
                                    ActivityCompat.requestPermissions(WebJS.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                                }
//                                ActivityCompat.requestPermissions(WebJS.this,new String[]{Manifest.permission.CAMERA},222);
                                return;
                            }else {
                                //
                                takeForPicture();
                            }
                        }else {
                            //
                            takeForPicture();
                        }
                    }
                })
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        takeForPhoto();
                    }
                }).setCancelable(false).setCanceledOnTouchOutside(false);

        dialog.show();
        //设置点击“取消”按钮监听，目的取消mFilePathCallback回调，可以重复调起弹窗
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelFilePathCallback();
            }
        });
    }
    /**
     * 调用相机
     */
    private void takeForPicture() {
        File pFile = new File(Environment.getExternalStorageDirectory(), "MyPictures");//图片位置
        if (!pFile.exists()) {
            pFile.mkdirs();
        }
        //拍照所存路径
        picturefile = new File(pFile + File.separator + "IvMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > 23) {//7.0及以上
            Uri contentUri = FileProvider.getUriForFile(WebJS.this, getResources().getString(R.string.filepath), picturefile);
            grantUriPermission(getPackageName(),contentUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {//7.0以下
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picturefile));
        }
//        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICETURE);

    }
    //调用权限回掉
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    takeForPicture();
                } else {
                    // Permission Denied
                    Toast.makeText(WebJS.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 333:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showShARE(shareUrl);
                }else {
                    Toast.makeText(WebJS.this, "很遗憾你把存储权限禁用了。请务必开启存储权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /**
     * 调用相册
     */
    private void takeForPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }
    private void cancelFilePathCallback() {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //相机
            case REQUEST_CODE_TAKE_PICETURE:
                takePictureResult(resultCode);
                break;

            case REQUEST_CODE_PICK_PHOTO:
                //相册
                takePhotoResult(resultCode, data);
                break;
        }
    }
    //相册
    private void takePhotoResult(int resultCode, Intent data) {
        if (mFilePathCallback != null){
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                Cursor cursor = mContext.getContentResolver().query(result,null,null,null,null);
                if (cursor != null && cursor.moveToFirst()){
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    Uri uri = Uri.fromFile(new File(path));
                    if (Build.VERSION.SDK_INT > 18) {
                        mFilePathCallback.onReceiveValue(new Uri[]{uri});
                    } else {
                        mFilePathCallback.onReceiveValue(uri);
                    }
                }
            }else {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
        }
    }

    private void takePictureResult(int resultCode) {
        if (mFilePathCallback != null) {
            if (resultCode == RESULT_OK) {
                Uri uri = Uri.fromFile(picturefile);
                if (Build.VERSION.SDK_INT > 18) {
                    mFilePathCallback.onReceiveValue(new Uri[]{uri});
                } else {
                    mFilePathCallback.onReceiveValue(uri);
                }
            } else {
                //点击了file按钮，必须有一个返回值，否则会卡死
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(WebJS.this).getString("zhifu_url"))){
            initView(SharedPreferencesUtil.getInstance(WebJS.this).getString("zhifu_url"));
            SharedPreferencesUtil.getInstance(WebJS.this).putString("zhifu_url","");
        }
        cookieSyncManager.stopSync();
    }

    @Override
    protected void onDestroy() {
        if (webView != null){
            webView.removeAllViews();
            mLayout.removeView(webView);
            webView.destroy();
        }
        super.onDestroy();

    }

    private void showShARE(String ss){
        try {
            String url = URLDecoder.decode(ss,"utf-8");
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            oks.setSilent(true);
// title标题，微信、QQ和QQ空间等平台使用
            oks.setTitle("智造库");
            //  siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            oks.setSiteUrl(url.substring(url.indexOf("&url=")+5,url.length()));
            // titleUrl QQ和QQ空间跳转链接
            oks.setTitleUrl(url.substring(url.indexOf("&url=")+5,url.length()));
            // url在微信、微博，Facebook等平台中使用
            //url.substring(url.indexOf("&imageUrl=")+10,url.indexOf("&url"))
            oks.setUrl(url.substring(url.indexOf("&url=")+5,url.length()));
            // text是分享文本，所有平台都需要这个字段
            oks.setText(url.substring(url.indexOf("&url=")+5,url.length()));
            //url.substring(url.indexOf("content=")+8,url.indexOf("&imageUrl"))
            oks.setImageUrl(url.substring(url.indexOf("&imageUrl=")+10,url.indexOf("&url")));
//            oks.setPlatform(QZone.NAME);
            oks.setCallback(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.i("1234","onComplete");
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Log.i("1234",throwable.toString());
                }
                @Override
                public void onCancel(Platform platform, int i) {
                    Log.i("1234","onCancel");
                }
            });
            oks.show(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}