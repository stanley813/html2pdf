package com.zjh.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.zjh.service.Html2PdfService;
import com.zjh.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("")
@Slf4j
public class PdfDownloadController {

    public static final String FILE_PATH = "d://report_pdf/";

    @Autowired
    Html2PdfService html2PdfService;

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam(value = "idExtension") String idExtension) {
        try {
            String fileName = idExtension + ".pdf";
            Date reportTime = html2PdfService.getReportTime(idExtension);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String time = simpleDateFormat.format(reportTime);
            String filePath = FILE_PATH + time + "/" + fileName;
            return this.download(filePath, fileName);
        } catch (Exception e) {
            log.error("文件不存在,{}", e);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Resource> download(String filePath, String fileName) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName).body(new FileSystemResource(filePath));
    }

}
