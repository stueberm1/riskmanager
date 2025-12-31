package com.github.stueberm1.riskmanager.data.jpa.risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskDataRepository extends JpaRepository<RiskData, String>,
        JpaSpecificationExecutor<RiskData> {


}
