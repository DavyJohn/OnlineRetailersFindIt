package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2017/8/30.
 */

public class RecommendBrandMode {

    private String result;
    private String message;
    private RecommendBrandData data;

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

    public RecommendBrandData getData() {
        return data;
    }

    public void setData(RecommendBrandData data) {
        this.data = data;
    }

    public class RecommendBrandData{
        private List<BrandData> brandList;

        public List<BrandData> getBrandList() {
            return brandList;
        }

        public void setBrandList(List<BrandData> brandList) {
            this.brandList = brandList;
        }

        public class BrandData{
            private String brandId;
            private String name;
            private String logo;
            private String keywords;
            private String brief;
            private String url;
            private String disabled;
            private String ordernum;
            private String initials;

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }

            public String getOrdernum() {
                return ordernum;
            }

            public void setOrdernum(String ordernum) {
                this.ordernum = ordernum;
            }

            public String getInitials() {
                return initials;
            }

            public void setInitials(String initials) {
                this.initials = initials;
            }
        }
    }
}
