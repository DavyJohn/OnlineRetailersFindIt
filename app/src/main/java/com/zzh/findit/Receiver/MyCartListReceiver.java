package com.zzh.findit.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.MainActivity;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.CartMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2017/11/2.
 */

public class MyCartListReceiver extends BroadcastReceiver {
    private static final String TAG = "MyCartListReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        upDataCartList(context);
    }
    private void upDataCartList(Context context){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("memberId", SharedPreferencesUtil.getInstance(context).getString("memberid"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.CARTDATA).params(map).build().execute(new ToCallBack<CartMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(CartMode data, int id) {
                if (data.getResult().equals("1")){
                    if (data.getData().getCartList() != null ){
                        Contants.pointNum = data.getData().getCartList().size();
                        MainActivity.instance.initBadgeView();
                    }
                }


            }
        });
    }
}
