package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2017/8/30.
 */

public class HotGoodsMode {
    private String result;
    private String message;
    private HotGoodsData data;

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

    public HotGoodsData getData() {
        return data;
    }

    public void setData(HotGoodsData data) {
        this.data = data;
    }

    public class HotGoodsData{
        private List<HotGoodsList> goodsList;

        public List<HotGoodsList> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<HotGoodsList> goodsList) {
            this.goodsList = goodsList;
        }

        public class HotGoodsList{
            private String goods_id;
            private String name;
            private String sn;
            private String brand_id;
            private String cat_id;
            private String sery_id;
            private String goods_model_id;
            private String type_id;
            private String intro;
            private String price;
            private String cost;
            private String mktprice;
            private String params;
            private String specs;
            private String create_time;
            private String last_modify;
            private String store;
            private String enable_store;
            private String thumbnail;
            private String big;
            private String small;
            private String original;
            private String store_name;
            private String goods_transfee_charge;

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

            public String getSery_id() {
                return sery_id;
            }

            public void setSery_id(String sery_id) {
                this.sery_id = sery_id;
            }

            public String getGoods_model_id() {
                return goods_model_id;
            }

            public void setGoods_model_id(String goods_model_id) {
                this.goods_model_id = goods_model_id;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
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

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getMktprice() {
                return mktprice;
            }

            public void setMktprice(String mktprice) {
                this.mktprice = mktprice;
            }

            public String getParams() {
                return params;
            }

            public void setParams(String params) {
                this.params = params;
            }

            public String getSpecs() {
                return specs;
            }

            public void setSpecs(String specs) {
                this.specs = specs;
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

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getEnable_store() {
                return enable_store;
            }

            public void setEnable_store(String enable_store) {
                this.enable_store = enable_store;
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

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getGoods_transfee_charge() {
                return goods_transfee_charge;
            }

            public void setGoods_transfee_charge(String goods_transfee_charge) {
                this.goods_transfee_charge = goods_transfee_charge;
            }
        }
    }
}
