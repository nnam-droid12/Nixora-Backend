package com.nixora.loan.report.export.utils;

import com.nixora.loan.report.dto.HighRiskLoanRow;
import com.nixora.loan.report.dto.MaturityExposureRow;
import com.nixora.loan.report.dto.PortfolioSummaryDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelReportExporter {


    public byte[] exportHighRisk(List<HighRiskLoanRow> rows) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("High Risk Loans");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Loan ID");
        header.createCell(1).setCellValue("Covenants");
        header.createCell(2).setCellValue("Defaults");

        int i = 1;
        for (HighRiskLoanRow r : rows) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(r.getLoanId().toString());
            row.createCell(1).setCellValue(r.getCovenantCount());
            row.createCell(2).setCellValue(r.getDefaultCount());
        }

        return write(wb);
    }


    public byte[] exportMaturity(List<MaturityExposureRow> rows) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Maturity");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Year");
        header.createCell(1).setCellValue("Loans");

        int i = 1;
        for (MaturityExposureRow r : rows) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(r.getYear());
            row.createCell(1).setCellValue(r.getLoanCount());
        }

        return write(wb);
    }


    public byte[] exportPortfolio(PortfolioSummaryDTO p) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Portfolio");

        sheet.createRow(0).createCell(0).setCellValue("Total Loans");
        sheet.createRow(1).createCell(0).setCellValue("Average Margin");
        sheet.createRow(2).createCell(0).setCellValue("High Risk Loans");
        sheet.createRow(3).createCell(0).setCellValue("Transferable Loans");

        sheet.getRow(0).createCell(1).setCellValue(p.getTotalLoans());
        sheet.getRow(1).createCell(1).setCellValue(p.getAverageMargin());
        sheet.getRow(2).createCell(1).setCellValue(p.getHighRiskLoans());
        sheet.getRow(3).createCell(1).setCellValue(p.getTransferableLoans());

        return write(wb);
    }

    private byte[] write(Workbook wb) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        return out.toByteArray();
    }
}
