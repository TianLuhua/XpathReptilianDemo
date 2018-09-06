package com.example.demo;

public class Config {

    /**
     * 豌豆荚相关路径
     */
    //搜索界面的根路径
    final static String ROOTPATH = "https://www.wandoujia.com/search/";
    //获取当前返回的数据页数的 关键字符
    final static String WANDOUJIA_PAGE_INDEX = "//*ul[@id=j-search-list]/div/div/a[@class=page-item]";
    //获取当前页面有多少条数据的 关键字符
    final static String WANDOUJIA_LIST_INDEX = "//*ul[@id=j-search-list]/li/div[@class=app-desc]";
    //详情界面的根路径
    final static String KEY_ID = "14232182728454798196";
    final static String PAGE = "_page";

    /**
     * 应用宝相关路径
     */

    /**
     * 360相关路径
     */
    final static String ROOTPATH_360 = "http://zhushou.360.cn/search/index/?kw=";
    //需要搜索apk的类型
    final static String ID_360 = "早教";
    //同一类型的page前缀
    final static String PAGE_360 = "&page=";

    /**
     * 下载次数的单位
     */
    public static class Unit {
        final static String Hundreds_of_millions = "亿";
        final static String Hundreds = "万";
    }
}
