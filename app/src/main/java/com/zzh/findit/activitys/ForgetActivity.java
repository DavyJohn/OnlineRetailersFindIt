package com.zzh.findit.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.BaseMode;
import com.zzh.findit.mode.ForgetMode;
import com.zzh.findit.utils.CommonUtil;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.widget.ClearEditText;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 腾翔信息 on 2017/7/21.
 */

public class ForgetActivity extends BaseActivity {

    private static final String TAG = ForgetActivity.class.getSimpleName();
    private TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象;
    @BindView(R.id.forget_code_button)
    Button mBtn;
    @BindView(R.id.forget_username)
    ClearEditText mFu;
    @BindView(R.id.forget_et_pw)
    ClearEditText mFep;

    @OnClick(R.id.forget_code_button) void checkPhone(){
        if (CommonUtil.isMobileNO(mFu.getText().toString())){
            time.start();
            //发送
            getSmsCode();
        }
    }

    @OnClick(R.id.forget_next_button) void next (){
        //验证验证码
        setValidateCode();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("忘记密码");

    }
    private void getSmsCode(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("key","update_password");
        map.put("mobile",mFu.getText().toString().trim());
        OkHttpUtils.post().url(Contants.BASEURL + Contants.SENDSMSCODE).params(map).build().execute(new ToCallBack<BaseMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(BaseMode data, int id) {
                if (data.getResult().equals("1")){
                    showToast(data.getMessage());
                }else {
                    showToast(data.getMessage());
                }
            }
        });
    }

    //验证验证码
    private void setValidateCode(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("key","update_password");
        map.put("mobile",mFu.getText().toString());
        map.put("validcode",mFep.getText().toString());
        OkHttpUtils.post().url(Contants.BASEURL + Contants.VALIDATECODE).params(map).build().execute(new ToCallBack<ForgetMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ForgetMode data, int id) {
                if (data.getResult().equals("1")){
                    Intent intent = new Intent(mContext,ResetPassActivity.class);
                    intent.putExtra("memberId",data.getData().getMemberId());
                    startActivity(intent);
                }else {
                    showToast(data.getMessage());
                }
            }
        });

    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mBtn.setText("获取验证码");
            mBtn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (!isFinishing()){
                mBtn.setClickable(false);
                mBtn.setText("剩余" + (int) millisUntilFinished / 1000 + "秒");
            }else {
                time.cancel();
            }
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.forget_main_layout;
    }

}
