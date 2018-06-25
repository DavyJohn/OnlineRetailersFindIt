package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2018/2/27.
 */

public class JxMode {
    private String result;
    private String message;
    private JxData data;

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

    public JxData getData() {
        return data;
    }

    public void setData(JxData data) {
        this.data = data;
    }

    public class JxData{
        private List<ListData> list;

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public class ListData{
            private String goods_id;
            private String name;
            private String sn;
            private String brand_id;
            private String cat_id;
            private String type_id;
            private String goods_type;
            private String unit;
            private String weight;
            private String market_enable;
            private String thumbnail;
            private String big;
            private String small;
            private String original;
            private String brief;
            private String intro;
            private String price;
            private String create_time;
            private String last_modify;
            private String disabled;
            private String point;
            private String spec_params;
            private String size_params;
            private String goods_model_id;
            private String goods_status;
            private String sery_id;
            private String goodsLvPrices;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getMarket_enable() {
                return market_enable;
            }

            public void setMarket_enable(String market_enable) {
                this.market_enable = market_enable;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getBig() {
                return big;
            }

            public void setBig(String big) {
                this.big = big;
            }

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getLast_modify() {
                return last_modify;
            }

            public void setLast_modify(String last_modify) {
                this.last_modify = last_modify;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }

            public String getPoint() {
                return point;
            }

            public void setPoint(String point) {
                this.point = point;
            }

            public String getSpec_params() {
                return spec_params;
            }

            public void setSpec_params(String spec_params) {
                this.spec_params = spec_params;
            }

            public String getSize_params() {
                return size_params;
            }

            public void setSize_params(String size_params) {
                this.size_params = size_params;
            }

            public String getGoods_model_id() {
                return goods_model_id;
            }

            public void setGoods_model_id(String goods_model_id) {
                this.goods_model_id = goods_model_id;
            }

            public String getGoods_status() {
                return goods_status;
            }

            public void setGoods_status(String goods_status) {
                this.goods_status = goods_status;
            }

            public String getSery_id() {
                return sery_id;
            }

            public void setSery_id(String sery_id) {
                this.sery_id = sery_id;
            }

            public String getGoodsLvPrices() {
                return goodsLvPrices;
            }

            public void setGoodsLvPrices(String goodsLvPrices) {
                this.goodsLvPrices = goodsLvPrices;
            }
        }
    }
}
