package com.github.stueberm1.riskmanager.data.jpa.risk;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface RiskDataRepository extends JpaRepository<RiskData, Long>,
        JpaSpecificationExecutor<RiskData> {

    Optional<RiskData> findByRiskIdentifier(String riskIdentifier);

    void deleteByRiskIdentifier(String riskIdentifier);

}
