package com.zzh.findit.mode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 腾翔信息 on 2018/3/15.
 */

public class LogData {

    private String result;
    private String message;
    private Logdata data;

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

    public Logdata getData() {
        return data;
    }

    public void setData(Logdata data) {
        this.data = data;
    }

    public class Logdata{

        private List<LogList> list;

        public List<LogList> getList() {
            return list;
        }

        public void setList(List<LogList> list) {
            this.list = list;
        }

        public class LogList{
            private String logo;
            private String brand_id;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }
        }
    }
}
