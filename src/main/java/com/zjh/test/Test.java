package com.zjh.test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class Test {

    public static void aa() throws IOException {
        long startTime = System.currentTimeMillis();
        //       pdf文件存储相对路径
        String pdfFile = "./zjh.pdf";
        //        自定义水印
        String waterMarkText = "";

        /*
        http://192.168.1.48:8080/ris/integration/showReport.action?accession=CT02006995&patientid=0003228020&source=kiosk&flag=PRINT
         */
        String html = getHtml("http://192.168.1.48:8080/ris/integration/showReport.action?accession=CT02006995&patientid=0003228020&source=kiosk&flag=PRINT");
//        String html = getHtml("https://www.python100.com/html/7RAC6626M6TE.html");

        log.info(html);
        System.out.println(html);
        OutputStream outputStream = new FileOutputStream(pdfFile);
        //微软雅黑在windows系统里的位置如下，linux系统直接拷贝该文件放在linux目录下即可
        //        String fontPath = "src/main/resources/font/STHeiti Light.ttc,0";
        String fontPath = "";
//        String fontPath = "src/main/resources/font/simsunb.ttf";
        HtmlToPdfUtils.convertToPdf(html, waterMarkText, fontPath, outputStream);
//        HtmlToPdfUtils.convertToPdf(inputStream, waterMarkText, fontPath, outputStream);
        log.info("转换结束，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    public static String getHtml(String urlString) {
        try {
            StringBuffer html = new StringBuffer();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream(),"UTF-8");
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