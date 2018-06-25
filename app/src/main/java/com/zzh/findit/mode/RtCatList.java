package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2018/2/28.
 */

public class RtCatList {
    private String cat_id;
    private String name;
    private List<HotGoodsListData> hotGoodsList;

    public List<HotGoodsListData> getHotGoodsList() {
        return hotGoodsList;
    }

    public void setHotGoodsList(List<HotGoodsListData> hotGoodsList) {
        this.hotGoodsList = hotGoodsList;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
