package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2017/9/4.
 */

public class CartMode {
    private String result;
    private String message;
    private CartData data;

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

    public CartData getData() {
        return data;
    }

    public void setData(CartData data) {
        this.data = data;
    }

    public static class CartData{
        private List<CartList> cartList;

        public List<CartList> getCartList() {
            return cartList;
        }

        public void setCartList(List<CartList> cartList) {
            this.cartList = cartList;
        }

        public static class CartList{
            private String id;
            private String product_id;
            private String goods_id;
            private String name;
            private String mktprice;
            private String price;
            private String coupPrice;
            private String subtotal;
            private String num;
            private String image_default;
            private String catid;
            private String activity_id;
            private String is_check;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

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

            public String getMktprice() {
                return mktprice;
            }

            public void setMktprice(String mktprice) {
                this.mktprice = mktprice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCoupPrice() {
                return coupPrice;
            }

            public void setCoupPrice(String coupPrice) {
                this.coupPrice = coupPrice;
            }

            public String getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(String subtotal) {
                this.subtotal = subtotal;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getImage_default() {
                return image_default;
            }

            public void setImage_default(String image_default) {
                this.image_default = image_default;
            }

            public String getCatid() {
                return catid;
            }

            public void setCatid(String catid) {
                this.catid = catid;
            }

            public String getActivity_id() {
                return activity_id;
            }

            public void setActivity_id(String activity_id) {
                this.activity_id = activity_id;
            }

            public String getIs_check() {
                return is_check;
            }

            public void setIs_check(String is_check) {
                this.is_check = is_check;
            }

            //单独的标签
            private boolean isSelect = true; //true 直接选中 false 不直接选中

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }
}
