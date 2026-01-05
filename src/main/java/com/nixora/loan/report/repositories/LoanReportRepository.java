package com.nixora.loan.report.repositories;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanReportRepository extends JpaRepository<LoanSnapshotEntity, UUID> {

    @Query("""
       SELECT COUNT(s) FROM LoanSnapshotEntity s
       WHERE s.uploadedBy = :user
    """)
    long totalLoans(User user);

    @Query("""
       SELECT AVG(s.margin) FROM LoanSnapshotEntity s
       WHERE s.uploadedBy = :user
    """)
    Double averageMargin(User user);

    @Query("""
       SELECT COUNT(s) FROM LoanSnapshotEntity s
       WHERE s.uploadedBy = :user AND s.defaultCount > 5
    """)
    long highRiskLoans(User user);

    @Query("""
       SELECT COUNT(s) FROM LoanSnapshotEntity s
       WHERE s.uploadedBy = :user AND LOWER(s.whiteList) LIKE '%free%'
    """)
    long transferableLoans(User user);

    @Query("""
SELECT
   EXTRACT(YEAR FROM s.maturityDate),
   COUNT(s)
FROM LoanSnapshotEntity s
WHERE s.uploadedBy = :user
  AND s.maturityDate IS NOT NULL
GROUP BY EXTRACT(YEAR FROM s.maturityDate)
ORDER BY 1
""")
    List<Object[]> maturityBuckets(@Param("user") User user);


    @Query("""
       SELECT s FROM LoanSnapshotEntity s
       WHERE s.uploadedBy = :user
       AND (s.defaultCount + s.covenantCount) > 10
    """)
    List<LoanSnapshotEntity> highRisk(User user);
}

