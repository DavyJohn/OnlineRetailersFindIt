package com.zzh.findit.mode;

/**
 * Created by 腾翔信息 on 2017/9/4.
 */

public class WebMode {
    private String result;
    private String message;
    private WebData data;

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

    public WebData getData() {
        return data;
    }

    public void setData(WebData data) {
        this.data = data;
    }

    public class WebData{
        private String webUrl;

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }
    }
}
