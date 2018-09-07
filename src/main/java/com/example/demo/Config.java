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
     * 百度平台应用相关
     */
    final static String ROOTPATH_BAIDU = "https://shouji.baidu.com/s?wd=";
    //获取当前返回的数据页数的 关键字符
    final static String PAGE_INDEX_BAIDU = "//*form[@class=page-form]/input[@class=total-page]/@value";
    //获取当前页面有多少条数据的 关键字符
    final static String CONTENT_INDEX_BAIDU = "//*ul[@class=app-list]/li/";
    //需要搜索apk的类型关键字
    final static String KEY_ID_BAIDU = "思维";
    //同一类型的page前缀
    final static String PAGE_BAIDU = "#page";


    /**
     * PAGE_INDEX_WANDOUJIA
     * 360相关路径
     */
    final static String ROOTPATH_360 = "http://zhushou.360.cn/search/index/?kw=";
    //获取当前返回的数据页数的 关键字符
    final static String PAGE_INDEX_360 = "//*script[@type=text/javascript]";
    //获取当前页面有多少条数据的 关键字符
    final static String CONTENT_INDEX_360 = "//*div[@class=SeaCon]/ul/";
    //需要搜索apk的类型
    final static String KEY_ID_360 = "早教";
    //同一类型的page前缀
    final static String PAGE_360 = "&page=";

    public enum AppType_360 {
        //早教，AR，绘本，思维训练
        ZAOJIAO("早教", 22), AR("AR", 35), HUIBEN("绘本", 7), SIWEI("思维训练", 7);

        private String type;
        private int pages;

        private AppType_360(String type, int pages) {
            this.type = type;
            this.pages = pages;
        }

        public String getType() {
            return type;
        }

        public int getPages() {
            return pages;
        }
    }

    /**
     * 下载次数的单位
     */
    public static class Unit {
        final static String Hundreds_of_millions = "亿";
        final static String Hundreds = "万";
        final static String THOUSAND = "千";
    }
}
