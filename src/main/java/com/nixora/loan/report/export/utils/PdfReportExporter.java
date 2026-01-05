package com.nixora.loan.report.export.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.nixora.loan.report.dto.HighRiskLoanRow;
import com.nixora.loan.report.dto.MaturityExposureRow;
import com.nixora.loan.report.dto.PortfolioSummaryDTO;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfReportExporter {

    public byte[] exportHighRisk(List<HighRiskLoanRow> rows) throws Exception {
        return simplePdf("High Risk Loans", rows.stream()
                .map(r -> "Loan " + r.getLoanId()
                        + " | Covenants: " + r.getCovenantCount()
                        + " | Defaults: " + r.getDefaultCount())
                .toList());
    }

    public byte[] exportMaturity(List<MaturityExposureRow> rows) throws Exception {
        return simplePdf("Maturity Exposure", rows.stream()
                .map(r -> r.getYear() + " : " + r.getLoanCount() + " loans")
                .toList());
    }

    public byte[] exportPortfolio(PortfolioSummaryDTO p) throws Exception {
        return simplePdf("Portfolio Summary", List.of(
                "Total Loans: " + p.getTotalLoans(),
                "Average Margin: " + p.getAverageMargin(),
                "High Risk Loans: " + p.getHighRiskLoans(),
                "Transferable Loans: " + p.getTransferableLoans()
        ));
    }

    private byte[] simplePdf(String title, List<String> lines) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph(title));
        document.add(new Paragraph(" "));

        for (String s : lines) {
            document.add(new Paragraph(s));
        }

        document.close();
        return out.toByteArray();
    }
}
