package com.example.demo;

public class Config {

    /**
     * 豌豆荚相关路径
     */
    //搜索界面的根路径
    final static String ROOTPATH_WANDOUJIA = "https://www.wandoujia.com/search/";
    //获取当前返回的数据页数的 关键字符
    final static String PAGE_INDEX_WANDOUJIA = "//*ul[@id=j-search-list]/div/div/a[@class=page-item]";
    //获取当前页面有多少条数据的 关键字符
    final static String CONTENT_INDEX_WANDOUJIA = "//*ul[@id=j-search-list]/li/div[@class=app-desc]";
    //apk类型的关键字
    final static String KEY_ID_WANDOUJIA = "14232182728454798196";
    //同一类型的page前缀
    final static String PAGE_WANDOUJIA = "_page";

    /**
     * 应用宝相关路径
     */

    /**
     * 360相关路径
     */
    final static String ROOTPATH_360 = "http://zhushou.360.cn/search/index/?kw=";
    //获取当前返回的数据页数的 关键字符
    final static String PAGE_INDEX_360 = "//*ul[@id=j-search-list]/div/div/a[@class=page-item]";
    //获取当前页面有多少条数据的 关键字符
    final static String CONTENT_INDEX_360 = "//*ul[@id=j-search-list]/li/div[@class=app-desc]";
    //需要搜索apk的类型
    final static String KEY_ID_360 = "早教";
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
