package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2018/3/14.
 */

public class FloorList {
    private String result;
    private String message;
    private FloorListData data;

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

    public FloorListData getData() {
        return data;
    }

    public void setData(FloorListData data) {
        this.data = data;
    }
    public class FloorListData{
        private List<FloorListDatas> list;


        public List<FloorListDatas> getList() {
            return list;
        }

        public void setList(List<FloorListDatas> list) {
            this.list = list;
        }

        public class FloorListDatas{
            private String id;
            private String page_id;
            private String title;
            private String parent_id;
            private String brand_ids;
            private String path;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }


           public String getBrand_ids() {
               return brand_ids;
           }

           public void setBrand_ids(String brand_ids) {
               this.brand_ids = brand_ids;
           }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }

    }
}
