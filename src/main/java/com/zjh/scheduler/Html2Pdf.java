package com.zjh.scheduler;

import com.alibaba.fastjson.JSON;
import com.zjh.controller.PdfDownloadController;
import com.zjh.domain.ReportPdf;
import com.zjh.service.Html2PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class Html2Pdf {

    /*
    http://192.168.1.48:8080/ris/integration/showReport.action
    ?accession=[实际的检查编号]&patientid=[实际的病人编号]&source=kiosk&flag=PRINT
     */
    public static final String URL = "http://192.168.3.73:8080/ris/integration/showReport.action" +
            "?accession=%s&patientid=%s&source=kiosk&flag=PRINT";

    public static final String CMD = "d://html2pdf/wkhtmltox/bin/wkhtmltopdf.exe -q  %s %s";
    @Autowired
    Html2PdfService html2pdfService;

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void transHTML2Pdf() {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();
        log.info("transHTML2Pdf start:" + new Date().toLocaleString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<ReportPdf> todayUrlInfo = html2pdfService.getTodayUrlInfo();
        log.info(String.valueOf(todayUrlInfo.size()));
        todayUrlInfo.forEach(reportPdf -> {

            String accession = reportPdf.getIdExtension();
            String patientid = reportPdf.getPatientId();
            String id = reportPdf.getId();
            Date reportTime = reportPdf.getReportTime();
            String reportUrl = String.format(URL, accession, patientid);
            String formatTime = dateFormat.format(reportTime);
            //TODO: 改成参数注入
            boolean mkdirs = new File(PdfDownloadController.FILE_PATH + formatTime).mkdirs();
            String absolutePath = PdfDownloadController.FILE_PATH + formatTime + "/" + accession + ".pdf";
            File file = new File(absolutePath);
            boolean exists = file.exists();
            //小于15K认为是错误的文件或者未正确生成的pdf
            if (exists && file.length() > (1024 * 15) && file.lastModified() == reportTime.getTime()) {
                return;
            }
            String cmd = String.format(CMD, reportUrl, absolutePath);
            log.info(id + ": " + cmd + " time:" + formatTime + "/" + reportTime.getTime());
            try {
                Process exec = Runtime.getRuntime().exec(cmd);
                count.getAndIncrement();
                while (exec.isAlive()) {
                    boolean b = exec.waitFor(1, TimeUnit.MINUTES);
                    if (!b) {
                        errorCount.getAndIncrement();
                        log.info("id:" + id + " error");
                    }
                }
                //修改文件的修改时间为数据库取出的报告时间
                file.setLastModified(reportTime.getTime());
            } catch (Exception e) {
                errorCount.getAndIncrement();
                log.info(reportUrl + " error:{}", e);
            }
        });
        log.info("trans: " + count);
        log.info("trans failed amount: " + errorCount);
        log.info("transHTML2Pdf end:" + new Date().toLocaleString());

    }


    public static void main(String[] args) throws IOException, InterruptedException {

        Process exec = Runtime.getRuntime().exec("C://Users/carrymore/Desktop/wkhtmltox/bin/wkhtmltopdf.exe" +
                " https://w3techs.com/sites/info/baidu.com/ D://99.pdf");
        System.out.println(exec.isAlive() + "$$$");
        while (exec.isAlive()) {
            System.out.println(exec.isAlive() + "###");
            boolean b = exec.waitFor(1, TimeUnit.MINUTES);
            System.out.println(b);
            System.out.println(exec.isAlive() + "^^^");

            if (!b) {
                log.info("id:" + " error");
            }
        }
    }
}
