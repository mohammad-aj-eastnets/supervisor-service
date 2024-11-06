package com.eastnets.reportservice.service;

import com.eastnets.reportservice.model.GeneratedReport;
import com.eastnets.reportservice.serviceInterface.IJasperReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService implements IJasperReportService {

    @Override
    public byte[] generatePdfReport(ResponseEntity<List<GeneratedReport>> reports) throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("GeneratedReport");
        jasperDesign.setPageWidth(842); // Increase page width to A4 landscape
        jasperDesign.setPageHeight(595); // A4 height in landscape
        jasperDesign.setColumnWidth(802); // Adjust column width
        jasperDesign.setColumnSpacing(5); // Revert column spacing
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);
        jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

        // Add fields
        addField(jasperDesign, "agentID", Integer.class);
        addField(jasperDesign, "agents", String.class); // New field
        addField(jasperDesign, "totalNumberOfCalls", Long.class);
        addField(jasperDesign, "totalTalkTime", Long.class);
        addField(jasperDesign, "longestTalkTime", Long.class);
        addField(jasperDesign, "shortestTalkTime", Long.class);
        addField(jasperDesign, "totalTimeNotReady", Long.class);
        addField(jasperDesign, "avgRecOnTotal", Double.class);

        // Add title band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(50);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText("Generated Report");
        titleText.setX(0);
        titleText.setY(0);
        titleText.setWidth(802); // Adjust width
        titleText.setHeight(50);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        titleBand.addElement(titleText);
        jasperDesign.setTitle(titleBand);

        // Add column headers
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(20);
        addColumnHeader(columnHeaderBand, "Agent ID", 0);
        addColumnHeader(columnHeaderBand, "Agent Name", 100); // New column
        addColumnHeader(columnHeaderBand, "Total Calls", 200);
        addColumnHeader(columnHeaderBand, "Total Talk Time", 300);
        addColumnHeader(columnHeaderBand, "Longest Talk Time", 400);
        addColumnHeader(columnHeaderBand, "Shortest Talk Time", 500);
        addColumnHeader(columnHeaderBand, "Total Time Not Ready", 600);
        addColumnHeader(columnHeaderBand, "Avg Rec On Total", 700);
        jasperDesign.setColumnHeader(columnHeaderBand);

        // Add detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        addTextField(detailBand, "agentID", 0, false);
        addTextField(detailBand, "agents", 100, false); // New field
        addTextField(detailBand, "totalNumberOfCalls", 200, false);
        addTextField(detailBand, "totalTalkTime", 300, true); // Time field
        addTextField(detailBand, "longestTalkTime", 400, true); // Time field
        addTextField(detailBand, "shortestTalkTime", 500, true); // Time field
        addTextField(detailBand, "totalTimeNotReady", 600, true); // Time field
        addTextField(detailBand, "avgRecOnTotal", 700, false);
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reports.getBody());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Call Center Management System");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private void addField(JasperDesign jasperDesign, String fieldName, Class<?> fieldType) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(fieldName);
        field.setValueClass(fieldType);
        jasperDesign.addField(field);
    }

    private void addTextField(JRDesignBand band, String fieldName, int xPosition, boolean isTimeField) {
        JRDesignTextField textField = new JRDesignTextField();
        textField.setX(xPosition);
        textField.setY(0);
        textField.setWidth(100); // Adjust width for better alignment
        textField.setHeight(20);
        textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER); // Center align text
        textField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);

        if (isTimeField) {
            textField.setExpression(new JRDesignExpression(
                    "String.format(\"%02d:%02d:%02d\", $F{" + fieldName + "} / 3600, ($F{" + fieldName + "} % 3600) / 60, $F{" + fieldName + "} % 60)"
            ));
        } else {
            textField.setExpression(new JRDesignExpression("$F{" + fieldName + "}"));
        }
        band.addElement(textField);
    }

    private void addColumnHeader(JRDesignBand band, String headerText, int xPosition) {
        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setText(headerText);
        staticText.setX(xPosition);
        staticText.setY(0);
        staticText.setWidth(100); // Adjust width for better alignment
        staticText.setHeight(20);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER); // Center align text
        staticText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        band.addElement(staticText);
    }
}
