package com.nixora.loan.report.export.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.report.export.utils.ExcelReportExporter;
import com.nixora.loan.report.export.utils.PdfReportExporter;
import com.nixora.loan.report.service.LoanReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/export")
@RequiredArgsConstructor
public class ReportExportController {

    private final LoanReportService reports;
    private final ExcelReportExporter excel;
    private final PdfReportExporter pdf;

    @GetMapping("/high-risk/excel")
    public ResponseEntity<byte[]> exportExcel(Authentication auth) throws Exception {

        User user = (User) auth.getPrincipal();
        byte[] file = excel.exportHighRisk(reports.highRiskLoans(user));
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=high-risk.xlsx")
                .body(file);
    }

    @GetMapping("/high-risk/pdf")
    public ResponseEntity<byte[]> exportPdf(Authentication auth) throws Exception {
        User user = (User) auth.getPrincipal();
        byte[] file = pdf.exportHighRisk(reports.highRiskLoans(user));
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=high-risk.pdf")
                .body(file);
    }

    @GetMapping("/maturity/pdf")
    public ResponseEntity<byte[]> maturityPdf(Authentication auth) throws Exception {
        User u = (User) auth.getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=maturity.pdf")
                .body(pdf.exportMaturity(reports.maturityExposure(u)));
    }

    @GetMapping("/maturity/excel")
    public ResponseEntity<byte[]> maturityExcel(Authentication auth) throws Exception {
        User u = (User) auth.getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header("Content-Disposition", "attachment; filename=maturity.xlsx")
                .body(excel.exportMaturity(reports.maturityExposure(u)));
    }

    @GetMapping("/portfolio/pdf")
    public ResponseEntity<byte[]> portfolioPdf(Authentication auth) throws Exception {
        User u = (User) auth.getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=portfolio.pdf")
                .body(pdf.exportPortfolio(reports.portfolioSummary(u)));
    }

    @GetMapping("/portfolio/excel")
    public ResponseEntity<byte[]> portfolioExcel(Authentication auth) throws Exception {
        User u = (User) auth.getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header("Content-Disposition", "attachment; filename=portfolio.xlsx")
                .body(excel.exportPortfolio(reports.portfolioSummary(u)));
    }

}
