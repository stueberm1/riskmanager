package com.github.stueberm1.riskmanager.data.jpa.risk;

import static org.springframework.data.jpa.domain.PredicateSpecification.allOf;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.nonNull;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/// Default implementation of the {@link RiskDataCriteriaSpecificationFactory}.
public class DefaultRiskDataCriteriaSpecificationFactory implements RiskDataCriteriaSpecificationFactory {

    @Override
    public PredicateSpecification<RiskData> buildSpecification(RiskFilter riskFilter) {
        return whereRiskData(new PredicateSpecificationBuilder(riskFilter));
    }

    private static PredicateSpecification<RiskData> whereRiskData(Supplier<List<PredicateSpecification<RiskData>>> specifications) {
        return allOf(specifications.get());
    }


    private static class PredicateSpecificationBuilder implements Supplier<List<PredicateSpecification<RiskData>>> {

        private final List<PredicateSpecification<RiskData>> predicateSpecifications = new CopyOnWriteArrayList<>();

        PredicateSpecificationBuilder(RiskFilter riskFilter) {
            requireNonNull(riskFilter);

            if(nonNull(riskFilter.probabilityOfOccurrenceIsEqualTo())) {
                predicateSpecifications.add(probabilityOfOccurrenceIsEqualTo(riskFilter.probabilityOfOccurrenceIsEqualTo()));
            }

            if (nonNull(riskFilter.severityIsEqualTo())) {
                predicateSpecifications.add(severityIsEqualTo(riskFilter.severityIsEqualTo()));
            }

            if (nonNull(riskFilter.descriptionContains())) {
                predicateSpecifications.add(descriptionContainsString(riskFilter.descriptionContains()));
            }

            if (nonNull(riskFilter.detailsContains()))  {
                predicateSpecifications.add(detailsContainsString(riskFilter.detailsContains()));
            }

            if (nonNull(riskFilter.contingencyPlanningIsEmpty())) {
                predicateSpecifications.add(contingencyPlanningIsEmpty());
            }

            if (nonNull(riskFilter.contingencyPlanningContains())) {
                predicateSpecifications.add(contingencyPlanningContains(riskFilter.contingencyPlanningContains()));
            }

            if (nonNull(riskFilter.mitigationStrategyIsEmpty())) {
                predicateSpecifications.add(mitigationStrategyIsEmpty());
            }

            if (nonNull(riskFilter.mitigationStrategyContains())) {
                predicateSpecifications.add(mitigationStrategyContains(riskFilter.mitigationStrategyContains()));
            }
        }

        @Override
        public List<PredicateSpecification<RiskData>> get() {
            return predicateSpecifications;
        }
    }

    private static PredicateSpecification<RiskData> severityIsEqualTo(Severity severity) {
        return (from,  criteriaBuilder)-> criteriaBuilder.equal(from.get("severity"), severity);
    }

    private static  PredicateSpecification<RiskData> probabilityOfOccurrenceIsEqualTo(ProbabilityOfOccurrence probabilityOfOccurrence) {
        return (from, criteriaBuilder) ->
                criteriaBuilder.equal(from.get("probabilityOfOccurrence"), probabilityOfOccurrence);
    }

    private static PredicateSpecification<RiskData> descriptionContainsString(String description) {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.like(riskData.get("description"), "%" + description + "%");
    }

    private static PredicateSpecification<RiskData> detailsContainsString(String details) {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.like(riskData.get("details"), "%" + details + "%");
    }

    public static PredicateSpecification<RiskData> contingencyPlanningContains(String contingencyPlanning) {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.like(riskData.get("contingencyPlanning"), "%" + contingencyPlanning + "%");
    }

    public static PredicateSpecification<RiskData> contingencyPlanningIsEmpty() {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.isNull(riskData.get("contingencyPlanning"));
    }

    public static PredicateSpecification<RiskData> mitigationStrategyContains(String mitigationStrategy) {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.like(riskData.get("mitigationStrategy"),
                        "%" + mitigationStrategy + "%");
    }

    public static PredicateSpecification<RiskData> mitigationStrategyIsEmpty() {
        return (riskData,  criteriaBuilder) ->
                criteriaBuilder.isNull(riskData.get("mitigationStrategy"));
    }
}
