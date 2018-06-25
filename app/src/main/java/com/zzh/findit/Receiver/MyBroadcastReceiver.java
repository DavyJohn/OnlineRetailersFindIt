package com.zzh.findit.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zzh.findit.WebJS;
import com.zzh.findit.http.OkHttpHelper;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.mode.LoginMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/9/29.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {
        //接受广播后的操作
        //更新 cookie
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("username",SharedPreferencesUtil.getInstance(context).getString("username"));
        map.put("password",SharedPreferencesUtil.getInstance(context).getString("password"));
        OkHttpHelper.getInstace().post(context, Contants.BASEURL + Contants.LOGIN, map, TAG, new SpotsCallBack<LoginMode>(context) {
            @Override
            public void onSuccess(Response response, LoginMode data) {
                List<String> cookie = response.headers("set-cookie");
                SharedPreferencesUtil.getInstance(context).putString("cookie",cookie.get(0));
                WebJS.instans.initUpdata();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
