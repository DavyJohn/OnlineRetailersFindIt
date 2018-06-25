package com.zzh.findit.mode;

import java.util.List;

/**
 * Created by 腾翔信息 on 2017/8/30.
 */

public class CatTreeMode {
    private String result;
    private String message;
    private CatTreeData data;

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

    public CatTreeData getData() {
        return data;
    }

    public void setData(CatTreeData data) {
        this.data = data;
    }

    public class CatTreeData{
        private List<CatTreeListData> catTreeList;

        public List<CatTreeListData> getCatTreeList() {
            return catTreeList;
        }

        public void setCatTreeList(List<CatTreeListData> catTreeList) {
            this.catTreeList = catTreeList;
        }

        //一级
        public class CatTreeListData{
            private String cat_id;
            private String name;
            private String parent_id;
            private String cat_path;
            private String goods_count;
            private String cat_order;
            private String type_id;
            private String list_show;
            private String image;
            private String hasChildren;
            private String type_name;
            private String state;
            private String totle_num;

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getTotle_num() {
                return totle_num;
            }

            public void setTotle_num(String totle_num) {
                this.totle_num = totle_num;
            }

            private List<Children> children;

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

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getCat_path() {
                return cat_path;
            }

            public void setCat_path(String cat_path) {
                this.cat_path = cat_path;
            }

            public String getGoods_count() {
                return goods_count;
            }

            public void setGoods_count(String goods_count) {
                this.goods_count = goods_count;
            }

            public String getCat_order() {
                return cat_order;
            }

            public void setCat_order(String cat_order) {
                this.cat_order = cat_order;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getList_show() {
                return list_show;
            }

            public void setList_show(String list_show) {
                this.list_show = list_show;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getHasChildren() {
                return hasChildren;
            }

            public void setHasChildren(String hasChildren) {
                this.hasChildren = hasChildren;
            }

            public List<Children> getChildren() {
                return children;
            }

            public void setChildren(List<Children> children) {
                this.children = children;
            }
            //二级
            public class Children{
                private String cat_id;
                private String name;
                private String parent_id;
                private String cat_path;
                private String goods_count;
                private String cat_order;
                private String type_id;
                private String list_show;
                private String image;
                private String hasChildren;
                private String type_name;
                private String state;
                private String totle_num;
                //三级
                private List<TWOChildrenData> children;

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

                public String getParent_id() {
                    return parent_id;
                }

                public void setParent_id(String parent_id) {
                    this.parent_id = parent_id;
                }

                public String getCat_path() {
                    return cat_path;
                }

                public void setCat_path(String cat_path) {
                    this.cat_path = cat_path;
                }

                public String getGoods_count() {
                    return goods_count;
                }

                public void setGoods_count(String goods_count) {
                    this.goods_count = goods_count;
                }

                public String getCat_order() {
                    return cat_order;
                }

                public void setCat_order(String cat_order) {
                    this.cat_order = cat_order;
                }

                public String getType_id() {
                    return type_id;
                }

                public void setType_id(String type_id) {
                    this.type_id = type_id;
                }

                public String getList_show() {
                    return list_show;
                }

                public void setList_show(String list_show) {
                    this.list_show = list_show;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getHasChildren() {
                    return hasChildren;
                }

                public void setHasChildren(String hasChildren) {
                    this.hasChildren = hasChildren;
                }

                public String getType_name() {
                    return type_name;
                }

                public void setType_name(String type_name) {
                    this.type_name = type_name;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getTotle_num() {
                    return totle_num;
                }

                public void setTotle_num(String totle_num) {
                    this.totle_num = totle_num;
                }

                public List<TWOChildrenData> getChildren() {
                    return children;
                }

                public void setChildren(List<TWOChildrenData> children) {
                    this.children = children;
                }
                //三级
                public class TWOChildrenData{
                    private String cat_id;
                    private String name;
                    private String parent_id;
                    private String cat_path;
                    private String goods_count;
                    private String cat_order;
                    private String type_id;
                    private String list_show;
                    private String image;
                    private String hasChildren;
                    private String type_name;
                    private String state;
                    private String totle_num;

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

                    public String getParent_id() {
                        return parent_id;
                    }

                    public void setParent_id(String parent_id) {
                        this.parent_id = parent_id;
                    }

                    public String getCat_path() {
                        return cat_path;
                    }

                    public void setCat_path(String cat_path) {
                        this.cat_path = cat_path;
                    }

                    public String getGoods_count() {
                        return goods_count;
                    }

                    public void setGoods_count(String goods_count) {
                        this.goods_count = goods_count;
                    }

                    public String getCat_order() {
                        return cat_order;
                    }

                    public void setCat_order(String cat_order) {
                        this.cat_order = cat_order;
                    }

                    public String getType_id() {
                        return type_id;
                    }

                    public void setType_id(String type_id) {
                        this.type_id = type_id;
                    }

                    public String getList_show() {
                        return list_show;
                    }

                    public void setList_show(String list_show) {
                        this.list_show = list_show;
                    }

                    public String getImage() {
                        return image;
                    }

                    public void setImage(String image) {
                        this.image = image;
                    }

                    public String getHasChildren() {
                        return hasChildren;
                    }

                    public void setHasChildren(String hasChildren) {
                        this.hasChildren = hasChildren;
                    }

                    public String getType_name() {
                        return type_name;
                    }

                    public void setType_name(String type_name) {
                        this.type_name = type_name;
                    }

                    public String getState() {
                        return state;
                    }

                    public void setState(String state) {
                        this.state = state;
                    }

                    public String getTotle_num() {
                        return totle_num;
                    }

                    public void setTotle_num(String totle_num) {
                        this.totle_num = totle_num;
                    }
                }
            }
        }
    }
}
