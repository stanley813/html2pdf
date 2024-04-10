package com.zjh.service;

import com.alibaba.fastjson.JSONObject;
import com.zjh.domain.ReportPdf;
import com.zjh.mapper.Html2PdfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class Html2PdfService {

    @Autowired
    Html2PdfMapper html2PdfMapper;

    public List<ReportPdf> getTodayUrlInfo() {
        return html2PdfMapper.getTodayUrlInfo();
    }
    public Date getReportTime(String idExtension) {
        return html2PdfMapper.getReportTime(idExtension);
    }
}
