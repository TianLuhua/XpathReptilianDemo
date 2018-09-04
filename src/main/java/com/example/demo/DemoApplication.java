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
        String listUrl = "https://www.wandoujia.com/search/9891950679460233267";
        HttpGet httpGet = new HttpGet(listUrl);
        HttpResponse response = httpClient.execute(httpGet);
        Html html = new Html(getHtml(response));
        //获取当前返回数据的页数
        pages = html.xpath("//*ul[@id=j-search-list]/div/div/a[@class=page-item]").nodes().size() - 2;

        for (int i = 1; i <= pages; i++) {
            listUrl = "https://www.wandoujia.com/search/9891950679460233267_page" + i;
            doRequest(listUrl);

            if (i == pages)
                for (APPBean a : appBeans) {
                    System.out.println(a.toString());
                }
        }
        System.out.println(appBeans.size());
    }

    public static List<String> doRequest(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        Html html = new Html(getHtml(response));
        List<Selectable> nodes = html.xpath("//*ul[@id=j-search-list]/li/div[@class=app-desc]").nodes();
        return parseHtml(nodes);
    }


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
