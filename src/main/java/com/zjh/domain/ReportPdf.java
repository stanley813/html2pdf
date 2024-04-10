package com.zjh.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReportPdf {
    public String idExtension;
    public String patientId;
    public String id;
    public Date reportTime;
}
