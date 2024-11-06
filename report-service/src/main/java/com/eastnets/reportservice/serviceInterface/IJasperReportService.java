package com.eastnets.reportservice.serviceInterface;

import com.eastnets.reportservice.model.GeneratedReport;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IJasperReportService {
    byte[] generatePdfReport(ResponseEntity<List<GeneratedReport>> reports) throws JRException;
}
