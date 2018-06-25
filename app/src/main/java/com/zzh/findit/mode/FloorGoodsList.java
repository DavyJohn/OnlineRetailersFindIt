package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2018/3/14.
 */

public class FloorGoodsList {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String result;
    private String message;
    private FloorGoodsListData data;

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

    public FloorGoodsListData getData() {
        return data;
    }

    public void setData(FloorGoodsListData data) {
        this.data = data;
    }

    public class FloorGoodsListData{
        private List<FloorGoodsData> list;

        public List<FloorGoodsData> getList() {
            return list;
        }

        public void setList(List<FloorGoodsData> list) {
            this.list = list;
        }
        public class FloorGoodsData{
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            private String goods_id;
            private String name;
            private String brand_id;
            private String cat_id;
            private String sery_id;
            private String goods_model_id;
            private String  goods_status;
            private String price;
            private String thumbnail;
            private String big;
            private String small;
            private String original;

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

            public String getGoods_status() {
                return goods_status;
            }

            public void setGoods_status(String goods_status) {
                this.goods_status = goods_status;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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
        }
    }
}
