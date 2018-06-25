package com.zzh.findit.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;
import com.zzh.findit.activitys.LoginActivity;
import com.zzh.findit.utils.NetworkUtils;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

//import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
	private static MyApplication mcontext;
	private static MyApplication instance;
	public static int mNetWorkState = -1;
	private static Handler mHandler;
	private static Thread mMainThread;
	private static long	mMainThreadId;
	private static Looper mMainThreadLooper;
	int cacheSize = 100*1024*1024;//100M

	private static List<Activity> activities = new ArrayList<Activity>();

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public void onCreate() {
		super.onCreate();
		mcontext = this;
		//一定要添加 虽然我没有测试 哈哈
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL ));
		//end
		//低版本报错解决
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
			builder.detectFileUriExposure();
		}
		//end
		// handler,用来子线程和主线程通讯
		mHandler = new Handler();
		// 主线程
		mMainThread = Thread.currentThread();
		// id
		// mMainThreadId = mMainThread.getId();
		mMainThreadId = android.os.Process.myTid();
		// looper
		mMainThreadLooper = getMainLooper();
//		File cacheFile = new File(getDiskCacheDir(mcontext),"cache");
//		final Cache cache = new Cache(cacheFile,cacheSize);
        final ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getAppContext()));
//		CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
		HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String s, SSLSession sslSession) {
						return true;
					}
				})
				.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
				.cookieJar(new CookieJar() {
					private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
					@Override
					public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
						cookieStore.put(url.host(), cookies);
					}
					@Override
					public List<Cookie> loadForRequest(HttpUrl url) {
						List<Cookie> cookies = cookieStore.get(url.host());
						return cookies != null ? cookies : new ArrayList<Cookie>();
					}
				})
				.build();

		OkHttpUtils.initClient(okHttpClient);
		//App 首次就可以加载 x5 内核
		QbSdk.initX5Environment(mcontext,null);
		//初始化 mob
		MobSDK.init(mcontext);

		CrashReport.initCrashReport(getApplicationContext());

	}

	public static Handler getHandler()
	{
		return mHandler;
	}

	public static Thread getMainThread()
	{
		return mMainThread;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper()
	{
		return mMainThreadLooper;
	}

	public MyApplication() {

	}

	public static MyApplication getInstance() {
		if (instance == null)
			instance = new MyApplication();
		return instance;
	}


	/**
	 * 获取网络类型
	 * 
	 * @return
	 */
	public void getConnType() {
		mNetWorkState = NetworkUtils.getNetworkState(mcontext);
	}


	public static MyApplication getAppContext() {
		return mcontext;
	}


	public void add(Activity activity) {
		if (activity != null) {
			activities.add(activity);
		}
	}

	/**
	 * 销毁当前集合中所有的Activity。
	 */
	public void finishAll() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}
	//除了一些activity 其他全部销毁
	public void finishAllExceptHome() {
		try {
			for (Activity activity : activities) {
				if (activity != null  )
					if (activity instanceof LoginActivity )
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	protected void attachBaseContext(Context base) {
//		super.attachBaseContext(base);
//		MultiDex.install(base);
//		Beta.installTinker();
//	}

	public String getDiskCacheDir(Context context){
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
			cachePath = context.getExternalCacheDir().getPath();
		}else {
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath;
	}

}
