package com.example.demo;

import com.example.demo.bean.APPBean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.Config.*;

@SpringBootApplication
@Controller
public class DemoApplication {

    private static List<APPBean> appBeans = new ArrayList<>();
    private static HttpClientBuilder builder = HttpClientBuilder.create();
    private static HttpClient httpClient = builder.build();
    private static int pages;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
        list();
    }

    @RequestMapping("/list")
    @ResponseBody
    public static void list() throws Exception {
//        doWanduojiaRequst(ROOTPATH_WANDOUJIA + Config.KEY_ID_WANDOUJIA, PAGE_INDEX_WANDOUJIA, CONTENT_INDEX_WANDOUJIA);
//        get360Page(AppType_360.SIWEI);

        doBaiduRequst();
    }


    //========================================百度  start======================================//
    public static void doBaiduRequst() throws Exception {
        appBeans.clear();
        int pagesBaidu = getBaiduPages(ROOTPATH_BAIDU + KEY_ID_BAIDU + PAGE_BAIDU + 1, PAGE_INDEX_BAIDU);
        System.out.println("===========pagesBaidu=============:" + pagesBaidu);
        for (int i = 0; i <= pagesBaidu; i++) {
           String url = "https://shouji.baidu.com/s?data_type=app&multi=0&ajax=1&wd=%E6%80%9D%E7%BB%B4&page=" + i + "&_=1536309334300";//思维
//                  String url = "https://shouji.baidu.com/s?data_type=app&multi=0&ajax=1&wd=%E7%BB%98%E6%9C%AC&page="+i+"&_=1536310622225";//绘本
//            String url = "https://shouji.baidu.com/s?data_type=app&multi=0&ajax=1&wd=%E6%97%A9%E6%95%99&page=" + i + "&_=1536310899949";//早教
//            String url = "https://shouji.baidu.com/s?data_type=app&multi=0&ajax=1&wd=AR&page="+i+"&_=1536311067173";//Ar
            System.out.println("===========baidu=============:" + url);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            Html html = new Html(getHtml(response));
//            System.out.println(html.toString());
//            System.out.println("===========baidu=============");
            List<Selectable> list = html.xpath(CONTENT_INDEX_BAIDU).nodes();
            System.out.println(list.toString());
            //解析html数据
            parseBaiduHtml(list);
        }
        for (APPBean a : appBeans) {
            System.out.println(a.toString());
        }

    }

    public static int getBaiduPages(String rootUrl, String pageIndex) throws Exception {
        HttpGet httpGet = new HttpGet(rootUrl);
        HttpResponse response = httpClient.execute(httpGet);
        Html html = new Html(getHtml(response));
        String pagesString = html.xpath(pageIndex).get();
        System.out.println("pagesString:" + pagesString);
        return Integer.valueOf(pagesString);
    }

    public static void parseBaiduHtml(List<Selectable> nodes) {
        nodes.forEach(node -> {
            try {
                String name = node.xpath("//*div[@class=top]/a/text()").get();
//                System.out.println("name:" + name);
                String number = node.xpath("//*span[@class=download-num]/text()").get();
//                System.out.println("number:" + number);
                APPBean appBean = new APPBean();
                double loaderCount = getLoaderNumber(number);
                appBean.setName(name);
                appBean.setLoadCount_(number);
                appBean.setLoadCount(loaderCount);
                appBeans.add(appBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Collections.sort(appBeans, new Comparator<APPBean>() {
            @Override
            public int compare(APPBean o1, APPBean o2) {
                return (o1.getLoadCount() > o2.getLoadCount()) ? -1 : 1;
            }
        });

    }


//========================================百度  end======================================//


    public static void get360Page(AppType_360 appType_360) throws Exception {
        appBeans.clear();
        if (appType_360 == null)
            return;
        for (int i = 1; i < appType_360.getPages(); i++) {
            HttpGet httpGet = new HttpGet(ROOTPATH_360 + appType_360.getType() + PAGE_360 + i);
            HttpResponse response = httpClient.execute(httpGet);
            Html html = new Html(getHtml(response));
            System.out.println(html.toString());
            List<Selectable> list = html.xpath(CONTENT_INDEX_360).nodes();
            System.out.println(list.size());
            //解析html数据
            parse360Html(list);
        }
        for (APPBean a : appBeans) {
            System.out.println(a.toString());
        }

    }

    private static void parse360Html(List<Selectable> nodes) {
        nodes.forEach(node -> {
            try {
                String name = node.xpath("//*dl/dd/h3/a/@title").get();
                System.out.println("name:" + name);
                String detailsPath = "http://zhushou.360.cn" + node.xpath("//*dl/dd/h3/a/@href").get();
                System.out.println("detailsPath:" + detailsPath);
                HttpGet httpGet = new HttpGet(detailsPath);
                HttpResponse response = httpClient.execute(httpGet);
                Html html = new Html(getHtml(response));
                APPBean appBean = new APPBean();
//                System.out.println("detailsPathHtml:" + html);
                String developer = html.xpath("div[@class=app-moreinfo]/p[4]/text()").get();
                String version = html.xpath("div[@class=app-moreinfo]/p[5]/text()").get();
//                System.out.println("details:" + developer);
                System.out.println("version:" + version);
                appBean.setDeveloper(developer);
                appBean.setVersion(version);

                String number = node.xpath("//*div[@class=seaDown]/div/p[@class=downNum]/text()").get();
                System.out.println("number:" + number);
                double loaderCount = getLoaderNumber(number);
                appBean.setName(name);
                appBean.setLoadCount_(number);
                appBean.setLoadCount(loaderCount);
                appBean.setDetailsPath(detailsPath);
                appBeans.add(appBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Collections.sort(appBeans, new Comparator<APPBean>() {
            @Override
            public int compare(APPBean o1, APPBean o2) {
                return (o1.getLoadCount() > o2.getLoadCount()) ? -1 : 1;
            }
        });
    }


    /**
     * 豌豆荚页面请求
     *
     * @param pageIndex    从html数据中获取当前返回数据页数的 indexString，比如：//*ul[@id=j-search-list]/div/div/a[@class=page-item]
     * @param contentIndex 具体获取apk内容的 关键字符串："//*ul[@id=j-search-list]/li/div[@class=app-desc]"
     * @throws Exception
     */
    public static void doWanduojiaRequst(String rootUrl, String pageIndex, String contentIndex) throws Exception {
        HttpGet httpGet = new HttpGet(rootUrl);
        HttpResponse response = httpClient.execute(httpGet);
        Html html = new Html(getHtml(response));
        //获取当前返回数据的页数
        pages = html.xpath(pageIndex).nodes().size() - 2;
        appBeans.clear();
        for (int i = 1; i <= pages; i++) {
            String pageUrl = rootUrl + Config.PAGE_WANDOUJIA + i;
            doRequest(pageUrl, contentIndex);
            if (i == pages)
                for (APPBean a : appBeans) {
                    System.out.println(a.toString());
                }
        }
        System.out.println(appBeans.size());
    }

    /**
     * 获取当前页 apk 的数据
     *
     * @param url       当前页的url
     * @param listIndex 从当前页中获取具体apk数据的关键字符串
     * @return
     * @throws Exception
     */
    public static List<String> doRequest(String url, String listIndex) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        Html html = new Html(getHtml(response));
        List<Selectable> nodes = html.xpath(listIndex).nodes();
        //这里具体解析 html 内容
        return parseHtml(nodes);
    }

    /**
     * 获取当前页面的全部的html的所有数据
     *
     * @param response HttpResponse
     * @return
     */
    public static String getHtml(HttpResponse response) {
        StringBuffer result = new StringBuffer();
        try (InputStream inputStream = response.getEntity().getContent()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static List<String> parseHtml(List<Selectable> nodes) {
        List<String> trList = new ArrayList<>();
        nodes.forEach(node -> {
            try {
                String name = node.xpath("/div/h2/allText()").get();
                String detailsPath = node.xpath("/div/h2/a/@href").get();

                HttpGet httpGet = new HttpGet(detailsPath);
                HttpResponse response = httpClient.execute(httpGet);
                Html html = new Html(getHtml(response));
                APPBean appBean = new APPBean();
                List<Selectable> details = html.xpath("//*div[@class=infos]/dl[@class=infos-list]").nodes();
//                System.out.println(details.toString());
//                System.out.println("=======================");
                details.forEach(detail -> {
                    String developer = detail.xpath("//*dd/span[@class=dev-sites]/text()").get();
                    String version = detail.xpath("//*dd[@class=perms]/text()").get();
                    appBean.setVersion(version);
                    appBean.setDeveloper(developer);
                });


                String number = node.xpath("/div/div[1]/span/text()").get();
                double loaderCount = getLoaderNumber(number);
                String join = StringUtils
                        .join(ArrayUtils.toArray(name, number),
                                ",");
                trList.add(join);
                appBean.setName(name);
                appBean.setLoadCount_(number);
                appBean.setLoadCount(loaderCount);
                appBean.setDetailsPath(detailsPath);
                appBeans.add(appBean);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Collections.sort(appBeans, new Comparator<APPBean>() {
            @Override
            public int compare(APPBean o1, APPBean o2) {
                return (o1.getLoadCount() > o2.getLoadCount()) ? -1 : 1;
            }
        });
        return trList;
    }


    /**
     * 根据网页下载量的单位，将数据转换成:double类型器的数据,便于排序
     *
     * @param str
     * @return
     */
    public static double getLoaderNumber(String str) {

        double loaderNumber = 0L;
        String temp = getLoaderNumberFromerString(str);
        if (str.contains(Config.Unit.Hundreds_of_millions)) {
            //如果下载量以亿为单位
            loaderNumber = Double.valueOf(temp) * 100000000;

        } else if (str.contains(Config.Unit.Hundreds)) {
            //如果下载量以万为单位
            loaderNumber = Double.valueOf(temp) * 10000;

        } else if (str.contains(Unit.THOUSAND)) {
            //如果下载量以千为单位
            loaderNumber = Double.valueOf(temp) * 1000;
        } else {
            //如果下载量以个为单位
            loaderNumber = Double.valueOf(temp);
        }
        return loaderNumber;
    }


    /**
     * 获取下载次数中的数字部分，便于排序
     *
     * @param str 来自网页的字符串：23.3万人安装
     */
    public static String getLoaderNumberFromerString(String str) {

        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }

}
