package com.monkeybean.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2019/6/22.
 */
public class ApplicationJSoup {

    public static void main(String[] args) throws IOException, InterruptedException {

        //读取配置参数
        String resourceUrl = ApplicationJSoup.class.getClassLoader().getResource("application.properties").getPath();
        FileInputStream in = new FileInputStream(resourceUrl);
        Properties pro = new Properties();
        pro.load(in);
        in.close();
        String rootDownloadUrl = pro.getProperty("rootDownloadUrl");
        String userAgent = pro.getProperty("userAgent");

        //获取根文档
        Document routeDoc = downloadDocument(rootDownloadUrl, userAgent);
        String article = routeDoc.select(".btitle h1").text();

        //遍历章节目录, 提取信息, 暂存到数组
        Elements elements = routeDoc.select(".chapterlist.cate a");
        List<String> chapterUrlList = new ArrayList<>();
        for (Element each : elements) {
            chapterUrlList.add(each.attr("href"));
        }

        //判断目录是否存在
        String rootStoreUrl = pro.getProperty("rootStoreUrl") + "/" + article + ".txt";
        File file = new File(rootStoreUrl);
        if (!file.exists()) {
            file.createNewFile();
        }

        //请求资源, 写入文件
        try (BufferedWriter output = new BufferedWriter(new FileWriter(file, true))) {
            for (String chapterUrl : chapterUrlList) {
                Document doc = downloadDocument(chapterUrl, userAgent);
                String chapterTitle = doc.select("#BookCon h1").text();
                String chapterContent = replaceUnless(doc);
                output.write(chapterTitle);
                output.write("\r\n");
                output.write(chapterContent);
                output.write("\r\n");
                output.flush();
                System.out.println("write finish: " + chapterTitle);
                Thread.sleep(1000);
            }
        }
    }

    /**
     * 下载资源
     *
     * @param requestUrl 请求连接
     * @param userAgent  用户代理头设置
     */
    private static Document downloadDocument(String requestUrl, String userAgent) throws IOException {
        return Jsoup.connect(requestUrl)
                .userAgent(userAgent)
                .timeout(3000)
                .get();
    }

    /**
     * 删除章节无用文字，如标签，链接
     */
    private static String replaceUnless(Document doc) {
        return doc.select("#BookText").html()
                .replaceAll("</?p>", "")
                .replace("<br>", "\n")
                .replaceAll("在线阅读.*[\\t\\n\\r\\s]*.*div>", "");
    }
}
