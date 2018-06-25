package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2017/8/29.
 */

public class BannerMode {

    private String result;
    private String message;
    private BannerData data;

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

    public BannerData getData() {
        return data;
    }

    public void setData(BannerData data) {
        this.data = data;
    }

    public class BannerData{
        private List<AdData> advLists;

        public List<AdData> getAdvLists() {
            return advLists;
        }

        public void setAdvLists(List<AdData> advLists) {
            this.advLists = advLists;
        }
        public class AdData{
            private String aid;
            private String acid;
            private String begintime;
            private String endtime;
            private String atturl;
            private String aname;
            private String url;
            private String disabled;
            private String httpAttUrl;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getAcid() {
                return acid;
            }

            public void setAcid(String acid) {
                this.acid = acid;
            }

            public String getBegintime() {
                return begintime;
            }

            public void setBegintime(String begintime) {
                this.begintime = begintime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getAtturl() {
                return atturl;
            }

            public void setAtturl(String atturl) {
                this.atturl = atturl;
            }

            public String getAname() {
                return aname;
            }

            public void setAname(String aname) {
                this.aname = aname;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }

            public String getHttpAttUrl() {
                return httpAttUrl;
            }

            public void setHttpAttUrl(String httpAttUrl) {
                this.httpAttUrl = httpAttUrl;
            }
        }

    }

}
