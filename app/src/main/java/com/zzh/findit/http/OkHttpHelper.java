package com.zzh.findit.http;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zzh.findit.http.callback.BaseCallBack;
import com.zzh.findit.utils.NetworkUtils;
import com.zzh.findit.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

;

/**
 * Created by zhailiangrong on 16/6/11. 这个不支持cookie的持久 但是可以拿到cookie
 */
public class OkHttpHelper {

    private static OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;
    private Call call;


    public  OkHttpHelper() {

        okHttpClient = new OkHttpClient();

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        //无效
        builder.cookieJar(new CookieJar() {
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
        });
        builder.build();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static  OkHttpHelper getInstace(){

        return new OkHttpHelper();
    }

    public void get(Context mContext, String url, LinkedHashMap<String, String> map, String tag, BaseCallBack callBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET, tag);
        doRequest(request, callBack, tag);
    }

    public void post(Context context, String url, LinkedHashMap<String,String> map, String tag, BaseCallBack callBack) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "请检查当前网络", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i(tag, url);
        Request request = buildRequest(url, map, HttpMethodType.POST, tag);
        doRequest(request, callBack, tag);
    }

    private void doRequest(final Request request, final BaseCallBack callBack, final String tag) {
        callBack.onRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response);
                Headers headers=response.headers();
                for (int i=0;i<headers.size();i++){
                    System.out.println(headers.name(i) + ": " + headers.value(i));
                }
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (StringUtils.isNotEmpty(resultStr)) {
                        if (callBack.mType == String.class) {
                            callbackSuccess(callBack, response, resultStr);

                        } else {
                            try {
                                Object object = gson.fromJson(resultStr, callBack.mType);
                                callbackSuccess(callBack, response, object);
                            } catch (JsonParseException e) {
                                callbackError(callBack, response, e);
                            }
                        }
                    }else{
                        callbackError(callBack, response, null);
                    }
                } else {
                    callbackError(callBack, response, null);
                }
            }

        });
    }

    private Request buildRequest(String url, LinkedHashMap<String,String> map, HttpMethodType type, String tag) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.tag(tag);

        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            RequestBody body = buildFormData(map);
            System.out.println(body);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(LinkedHashMap<String,String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        if (null != map) {
            for (Map.Entry<String,String> entry :map.entrySet()) {
                builder.addEncoded(entry.getKey(), entry.getValue());
            }
        }
        return  builder.build();
    }

    private void callbackSuccess(final BaseCallBack callBack, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final BaseCallBack callBack, final Response response, final Exception exception) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(response, response.code(), exception);
            }
        });
    }

    public void cancelTag(Object tag)
    {
        for (Call call : okHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }
    enum HttpMethodType {
        GET,
        POST
    }


}
