package com.nixora.loan.LoanQueryEngine.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanQueryEngine.dto.LoanQueryRequest;
import com.nixora.loan.LoanQueryEngine.dto.LoanSnapshotDTO;
import com.nixora.loan.LoanQueryEngine.dto.LoanSnapshotMapper;
import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import com.nixora.loan.LoanQueryEngine.service.PortfolioQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans/query")
@RequiredArgsConstructor
public class PortfolioQueryController {

    private final PortfolioQueryService queryService;

    @PostMapping("/search-query")
    public ResponseEntity<List<LoanSnapshotDTO>> query(
            @RequestBody LoanQueryRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                queryService.query(request, user)
                        .stream()
                        .map(LoanSnapshotMapper::toDto)
                        .toList()
        );
    }
}
