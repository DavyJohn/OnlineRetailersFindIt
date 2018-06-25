package com.zzh.findit.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.zzh.findit.R;
import com.zzh.findit.base.BaseActivity;
import com.zzh.findit.base.MyApplication;
import com.zzh.findit.http.callback.SpotsCallBack;
import com.zzh.findit.mode.BaseMode;
import com.zzh.findit.utils.Contants;
import com.zzh.findit.widget.ClearEditText;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/7/24.
 */

public class ResetPassActivity extends BaseActivity{

    private static final String TAG = ResetPassActivity.class.getSimpleName();
    private String memberId;
    @BindView(R.id.reset_new_ps)
    ClearEditText ps;
    @BindView(R.id.reset_ps_ag)
    ClearEditText agps;

    @OnClick(R.id.reset_button) void reset(){
        if (TextUtils.isEmpty(ps.getText().toString())){
            setShakeAnimation(ps);
        }else if (TextUtils.isEmpty(agps.getText().toString())){
            setShakeAnimation(agps);
        }else if (!ps.getText().toString().equals(agps.getText().toString())){
            showToast("两次密码不一致");
        }else if (ps.getText().toString().length() <6){
            showToast("密码格式不正确！");
        }else if (agps.getText().toString().length() < 6){
            showToast("密码格式不正确！");
        }else {
            setReset();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("重置密码");
        memberId = "0";
//        memberId = getIntent().getStringExtra("memberId");
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
                    showToast("已到达输入的最大长度！");
                }
            }
        });
        agps.addTextChangedListener(new TextWatcher() {
            private CharSequence wordag;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordag = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (wordag.length() == 20){
                    showToast("已到达输入的最大长度！");
                }
            }
        });
    }

    private void setReset(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("newpassword",ps.getText().toString());
        map.put("re_passwd",agps.getText().toString());
        map.put("memberId",memberId);
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.RESETPASSWORD, map, TAG, new SpotsCallBack<BaseMode>(mContext) {
            @Override
            public void onSuccess(Response response, BaseMode data) {
                if (data.getResult().equals("1")){
                    showToast(data.getMessage());
                    startActivity(new Intent(mContext,LoginActivity.class));
                    finish();
                }else {
                    showToast(data.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.reset_pass_main_layout;
    }
}
