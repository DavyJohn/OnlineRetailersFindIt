package com.zzh.findit.utils;

/**
 * Created by 腾翔信息 on 2017/7/18.
 */

public class Contants {

    public static final String BASEURL = "http://m.zzkonline.com";
//    public static final String BASEURL = "http://19e850p851.iask.in/b2c";
//      public static final String BASEURL = "http://192.168.6.165:8090/b2c";//测试 分享 地址

    //登录
    public static final String LOGIN = "/api/shop/member/AppBylogin.do";
    //注册
    public static final String REGISTER = "/api/shop/member/AppByRegister.do";
    //发送验证码
    public static final String SENDSMSCODE = "/api/shop/sms/AppBySendSmsCode.do";
    //验证验证码
    public static final String VALIDATECODE = "/api/shop/member/AppByValidateCode.do";
    //重置密码
    public static final String RESETPASSWORD = "/api/shop/member/AppByResetPassword.do";
    //获取首页轮播
    public static final String BANNER = "/api/shop/appIndex/AppByGetAdvLists.do";
    //获取个人信
    public static final String MEMBER = "/api/shop/member/AppByGetMember.do";
    //热销商品
    public static final String HOTGOODS = "/api/shop/appIndex/AppByGetHotGoodsList.do";
    //推荐品牌
    public static final String RECOMMENDBRAND= "/api/shop/appIndex/AppByGetRecommendBrandList.do";
    //获取分类（包含二级三级）
    public static final String CATTREE = "/api/shop/appIndex/AppByGetCatTreeList.do";
    //获取所有购物车
    public static final String CARTDATA = "/api/shop/cart/AppByGetCartList.do";
    //更改购物车数量
    public static final String UPDATEGOODS = "/api/shop/cart/AppByUpdateGoods.do";
    //添加购物车
    public static final String ADDGOODS = "/api/shop/cart/AppByAddGoods.do";
    //删除购物车
    public static final String DELETEGOODS = "/api/shop/cart/AppByDeleteGoods.do";
    //网页跳转接口
    public static final String WEBAPI = "/api/shop/webUrlApi/AppByGetAppWebUrl.do";
    //获取所有品牌
    public static final String ALLBRAND = "/api/shop/appIndex/AppByGetBrandList.do";
    //判断是否为登录进来开关
    public static Boolean isLogin = false;
    //test
    public static String secretKey = "qJrSdoqhmYQJuetU3rMwed3gX5kvKUnr2G8ks9A0qxwGSyghrEy3HAxYzP3";
    //购物车消息点数
    public static Integer pointNum = -1;
    //我的界面第一行功能菜单消息数量接口
    public static final String GetOrderSum = "/api/shop/appIndex/AppByGetOrderSum.do";
    //获取精选产品
    public static final String Appexec = "/api/shop/appIndex/Appexec.do";
    //获取热推
    public static final String AppHotGoods = "/api/shop/appIndex/AppHotGoods.do";
    //屏幕宽度
    public static int screenWidth ;
    //屏幕高度
    public static int screenHeight ;
    //名字+品牌id 数组
    public static final String FloorList = "/api/shop/appIndex/FloorList.do";
    //获取 品牌数组ids 需要传入 id
    public static final String Floor = "/api/shop/appIndex/Floor.do";
    //获取rt 商品
    public static final String FloorGoodsList = "/api/shop/appIndex/FloorGoodsList.do";
    //获取 rt 下方 logo
    public static final String FloorBrandListByCatId = "/api/shop/appIndex/FloorBrandListByCatid.do";
    public static final String collect = "/api/shop/appIndex/addCollect.do";
}
