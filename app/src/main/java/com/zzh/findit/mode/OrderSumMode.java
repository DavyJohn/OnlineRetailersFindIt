package com.zzh.findit.mode;

/**
 * Created by 腾翔信息 on 2018/2/26.
 */

public class OrderSumMode {
    private String result;
    private String message;
    private OrderData data;


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

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }
    public class OrderData{
        private  OrderMap ordermap;

        public OrderMap getOrdermap() {
            return ordermap;
        }

        public void setOrdermap(OrderMap ordermap) {
            this.ordermap = ordermap;
        }
    }

    public class OrderMap{
        private String orderReceivedCount;
        private String orderFinishCount;
        private String orderDeliveryCount;
        private String orderServiceCount;
        private String orderPaymentCount;

        public String getOrderReceivedCount() {
            return orderReceivedCount;
        }

        public void setOrderReceivedCount(String orderReceivedCount) {
            this.orderReceivedCount = orderReceivedCount;
        }

        public String getOrderFinishCount() {
            return orderFinishCount;
        }

        public void setOrderFinishCount(String orderFinishCount) {
            this.orderFinishCount = orderFinishCount;
        }

        public String getOrderDeliveryCount() {
            return orderDeliveryCount;
        }

        public void setOrderDeliveryCount(String orderDeliveryCount) {
            this.orderDeliveryCount = orderDeliveryCount;
        }

        public String getOrderServiceCount() {
            return orderServiceCount;
        }

        public void setOrderServiceCount(String orderServiceCount) {
            this.orderServiceCount = orderServiceCount;
        }

        public String getOrderPaymentCount() {
            return orderPaymentCount;
        }

        public void setOrderPaymentCount(String orderPaymentCount) {
            this.orderPaymentCount = orderPaymentCount;
        }
    }
}
