package com.zzh.findit.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.MainActivity;
import com.zzh.findit.R;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.http.OkHttpHelper;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.LoginMode;
import com.zzh.findit.utils.CommonUtil;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.utils.SharedPreferencesUtil;
import com.zzh.findit.widget.ClearEditText;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/7/12.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private boolean isapppwd = true;
    @BindView(R.id.login_register)
    TextView mTextRegister;
    @BindView(R.id.login_et_pw)
    ClearEditText mEtPw;
    @BindView(R.id.et_username)
    ClearEditText mEtName;
    @OnClick(R.id.login_button) void login(){
        if (TextUtils.isEmpty(mEtName.getText().toString())){
            setShakeAnimation(mEtName);
        }else if (TextUtils.isEmpty(mEtPw.getText().toString())){
            setShakeAnimation(mEtPw);
        }else if (mEtPw.getText().toString().length() <6){
           showToast("密码格式不正确！");
        }else {
            setLogin();
            Contants.isLogin = true;
        }
    }

    @OnClick(R.id.forget_pw) void forget(){
        startActivity(new Intent(mContext,ForgetActivity.class));
    }

    @OnClick(R.id.login_register) void register(){
        startActivity(new Intent(mContext,RegisteredActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        MyApplication.getInstance().add(this);
        hasToolBar(false);
        isAppearPwd();
        String text = mTextRegister.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.dots_select)),5,9, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTextRegister.setText(style);
        mEtPw.addTextChangedListener(new TextWatcher() {
            private CharSequence word;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                word = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (word.length() == 20){
                    showToast("已到达输入的最大长度");
                }
            }
        });
    }
    private void isAppearPwd() {
        if (isapppwd == true) {
            // 设置为密文显示
            mEtPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(mEtPw);
        }
    }

    private void setLogin(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("username",mEtName.getText().toString().trim());
        map.put("password",mEtPw.getText().toString().trim());
        SharedPreferencesUtil.getInstance(mContext).putString("username",mEtName.getText().toString().trim());
        SharedPreferencesUtil.getInstance(mContext).putString("password",mEtPw.getText().toString().trim());
        OkHttpUtils.post().url(Contants.BASEURL + Contants.LOGIN).params(map).build().execute(new ToCallBack<LoginMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(LoginMode data, int id) {
                 if (data.getResult().equals("1")){
                    LoginBg();
                    SharedPreferencesUtil.getInstance(mContext).putString("memberid",data.getData().getMember().getMember_id());
                    SharedPreferencesUtil.getInstance(mContext).putString("nickname",data.getData().getMember().getNickname());
                    //存储 用户等级
                    SharedPreferencesUtil.getInstance(mContext).putString("memberLv",data.getData().getMember().getLvname());
                    SharedPreferencesUtil.getInstance(mContext).putString("storeid",data.getData().getMember().getStore_id()+"");
                }else {
                    showToast(data.getMessage());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_mian_layout;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    //拿到 cookie
    private void LoginBg(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("username",SharedPreferencesUtil.getInstance(mContext).getString("username"));
        map.put("password",SharedPreferencesUtil.getInstance(mContext).getString("password"));
        OkHttpHelper.getInstace().post(mContext, Contants.BASEURL + Contants.LOGIN, map, TAG, new SpotsCallBack<LoginMode>(mContext) {

            @Override
            public void onSuccess(Response response, LoginMode loginMode) {
                List<String> cookie = response.headers("set-cookie");
                SharedPreferencesUtil.getInstance(mContext).putString("cookie",cookie.get(0));
                startActivity(new Intent(mContext, MainActivity.class));
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
