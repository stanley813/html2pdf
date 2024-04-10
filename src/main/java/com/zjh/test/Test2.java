package com.zjh.test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class Test2 {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        //       html文件所在相对路径
        String htmlFile = "src/main/resources/html/网页转pdf_百度搜索.html";
        //       pdf文件存储相对路径
        String pdfFile = "src/main/resources/index.pdf";
        //        自定义水印
        String waterMarkText = "";
        InputStream inputStream = new FileInputStream(htmlFile);


        OutputStream outputStream = new FileOutputStream(pdfFile);
        //微软雅黑在windows系统里的位置如下，linux系统直接拷贝该文件放在linux目录下即可
        //        String fontPath = "src/main/resources/font/STHeiti Light.ttc,0";
        String fontPath = "";
//        String fontPath = "src/main/resources/font/simsunb.ttf";
        HtmlToPdfUtils.convertToPdf(inputStream, waterMarkText, fontPath, outputStream);
//        HtmlToPdfUtils.convertToPdf(inputStream, waterMarkText, fontPath, outputStream);

        log.info("转换结束，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    public static String getHtml(String urlString) {
        try {
            StringBuffer html = new StringBuffer();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
            br.close();
            isr.close();
            return html.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}