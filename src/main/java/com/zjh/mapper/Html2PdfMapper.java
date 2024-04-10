package com.zjh.mapper;

import com.alibaba.fastjson.JSONObject;
import com.zjh.domain.ReportPdf;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface Html2PdfMapper {

    List<ReportPdf> getTodayUrlInfo();
    Date getReportTime(String idExtension);

}


