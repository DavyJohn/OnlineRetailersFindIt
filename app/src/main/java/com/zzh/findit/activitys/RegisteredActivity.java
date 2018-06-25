package com.zzh.findit.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.findit.R;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.http.callback.ToCallBack;
import com.zzh.findit.mode.BaseMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.widget.ClearEditText;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/7/21.
 */

public class RegisteredActivity extends BaseActivity {

    private static final String TAG = RegisteredActivity.class.getSimpleName();
    private TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象;
    @BindView(R.id.register_checkbox)
    CheckBox mCheck;
    @BindView(R.id.register_username)
    ClearEditText name;
    @BindView(R.id.register_phonecode)
    ClearEditText phoneSmsCode;
    @BindView(R.id.register_ps)
    ClearEditText ps;
    @BindView(R.id.register_ps_ag)
    ClearEditText psAg;
    @BindView(R.id.registered_protocol)
    TextView mProtocaol;
    @BindView(R.id.registerd_phone)
    ClearEditText mPhoneCode;
    @BindView(R.id.get_registered_code)
    Button mCode;
    @BindView(R.id.regster_company)
    ClearEditText mCompany;
    @BindView(R.id.register_tel)
    ClearEditText mTel;
    @BindView(R.id.register_email)
    ClearEditText mEmail;
    @BindView(R.id.registerd_button)
    Button mBtn;
    @OnClick(R.id.get_registered_code) void getCode(){
        if (!TextUtils.isEmpty(mPhoneCode.getText().toString())){

            //获取验证码
            getSmsCode();
        }
    }
    @OnClick(R.id.registered_protocol) void right(){
        //
        startActivity(new Intent(mContext,RuleActivity.class));
    }
    @OnClick(R.id.registerd_button) void register(){
        if (TextUtils.isEmpty(name.getText().toString())){
            setShakeAnimation(name);
        }else if (TextUtils.isEmpty(mPhoneCode.getText().toString())){
            setShakeAnimation(mPhoneCode);
        }else if (TextUtils.isEmpty(phoneSmsCode.getText().toString())){
            setShakeAnimation(phoneSmsCode);
        }else if (TextUtils.isEmpty(ps.getText().toString())){
            setShakeAnimation(ps);
        }else if (!ps.getText().toString().equals(psAg.getText().toString())){
            showToast("两次密码设置不一致");
            setShakeAnimation(psAg);
        }else if (ps.getText().toString().length() <6){
            showToast("密码格式不正确！");
        }else if (psAg.getText().toString().length() < 6){
            showToast("密码格式不正确！");
        }else if (name.getText().toString().length()<4){
            showToast("用户名长度不正确！");
        }else {
            //实现注册接口
            setRegister();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("注册");
        String pl = mProtocaol.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(pl);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.dots_select)),11,pl.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mProtocaol.setText(style);

        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mBtn.setClickable(true);
                    mBtn.setEnabled(true);
                }else {
                    mBtn.setClickable(false);
                    mBtn.setEnabled(false);
                    showMessageDialog("请阅读并勾选注册协议",mContext);
                }
            }
        });
        //监听 用户名
        name.addTextChangedListener(new TextWatcher() {
            private CharSequence n;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                n = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (n.length() == 20){
                    showToast("已到达输入的最大长度");
                }
            }
        });
        //监听 密码
        ps.addTextChangedListener(new TextWatcher() {
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

        psAg.addTextChangedListener(new TextWatcher() {
            private CharSequence wordag;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordag  = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (wordag.length() == 20){
                    showToast("已到达输入的最大长度");
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
            mCode.setText("获取验证码");
            mCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (!isFinishing()){
                mCode.setClickable(false);
                mCode.setText("剩余" + (int) millisUntilFinished / 1000 + "秒");
            }else {
                time.cancel();
            }

        }
    }

    private void getSmsCode(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("key","register");
        map.put("mobile",mPhoneCode.getText().toString().trim());
        OkHttpUtils.post().url(Contants.BASEURL + Contants.SENDSMSCODE).params(map).build().execute(new ToCallBack<BaseMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(BaseMode data, int id) {
                if (data.getResult().equals("1")){
                    showToast(data.getMessage());
                    time.start();
                }else {
                    showToast(data.getMessage());
                }
            }
        });
    }

    private void setRegister(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("username",name.getText().toString().trim());
        map.put("mobile",mPhoneCode.getText().toString().trim());
        map.put("validcode",phoneSmsCode.getText().toString());
        map.put("license","agree");
        map.put("password",ps.getText().toString());
        map.put("compname",mCompany.getText().toString());
        map.put("email",mTel.getText().toString());
        map.put("tel",mEmail.getText().toString());
        OkHttpUtils.post().url( Contants.BASEURL + Contants.REGISTER)
                .params(map)
                .build().execute(new ToCallBack<BaseMode>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(BaseMode data, int id) {
                if (data.getResult().equals("1")){
                    showToast(data.getMessage());
                    finish();
                }else {
                    showToast(data.getMessage());
                }
            }
        });

    }
    @Override
    public int getLayoutId() {
        return R.layout.registerd_main_layout;
    }
}
