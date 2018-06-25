package com.zzh.findit.mode;

/**
 * Created by 腾翔信息 on 2017/8/29.
 */

public class ForgetMode {
    private String result;
    private String message;
    private ForgetData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ForgetData getData() {
        return data;
    }

    public void setData(ForgetData data) {
        this.data = data;
    }

    public class ForgetData{
        private String memberId;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }
    }

}
