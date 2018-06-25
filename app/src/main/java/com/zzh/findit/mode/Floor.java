package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2018/3/14.
 */

public class Floor {
    //判别是哪个item
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String result;
    private String message;
    private FloorData data;

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

    public FloorData getData() {
        return data;
    }

    public void setData(FloorData data) {
        this.data = data;
    }
    public class FloorData{
        private FloorMap map;

        public FloorMap getMap() {
            return map;
        }

        public void setMap(FloorMap map) {
            this.map = map;
        }

        public class FloorMap{
            private FloorMapFloor floor;
            private List<ChildFloors> childFloors;

            public FloorMapFloor getFloor() {
                return floor;
            }

            public void setFloor(FloorMapFloor floor) {
                this.floor = floor;
            }

            public List<ChildFloors> getChildFloors() {
                return childFloors;
            }

            public void setChildFloors(List<ChildFloors> childFloors) {
                this.childFloors = childFloors;
            }

            public class FloorMapFloor{
                private String id;
                private String title;
                private String brand_ids;

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
            }
            public class ChildFloors{
                private String itemName;

                public String getItemName() {
                    return itemName;
                }

                public void setItemName(String itemName) {
                    this.itemName = itemName;
                }

                private String catt_id;
                private String id;
                private String page_id;
                private String title;
                private String parent_id;
                private String goods_ids_json;

                public String getGoods_ids_json() {
                    return goods_ids_json;
                }

                public void setGoods_ids_json(String goods_ids_json) {
                    this.goods_ids_json = goods_ids_json;
                }

                public String getCatt_id() {
                    return catt_id;
                }

                public void setCatt_id(String catt_id) {
                    this.catt_id = catt_id;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPage_id() {
                    return page_id;
                }

                public void setPage_id(String page_id) {
                    this.page_id = page_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getParent_id() {
                    return parent_id;
                }

                public void setParent_id(String parent_id) {
                    this.parent_id = parent_id;
                }

            }
        }
    }
}
