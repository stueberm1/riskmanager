package com.github.stueberm1.riskmanager.data.jpa.risk;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

/// The {@code RiskDataCriteriaSpecificationFactory} takes the {@link RiskFilter} provided by the modules requester and
/// transform it in an equivalent {@link PredicateSpecification} as required by spring-data's
/// {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}.
public interface RiskDataCriteriaSpecificationFactory {

    /// Performs the transformation.
    /// @param riskFilter inbound filter attributes and values
    /// @return equivalent {@link PredicateSpecification}
    /// @throws NullPointerException if the {@link RiskFilter} object is {@code null}
    PredicateSpecification<RiskData> buildSpecification(RiskFilter riskFilter);
}
