package com.github.stueberm1.riskmanager.http.service;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.in.risk.SeverityFilterSpec;
import com.github.stueberm1.riskmanager.core.in.risk.filter.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

import java.util.List;

public class QueryParameterEvaluator {
    private final Severity severity;
    private final ProbabilityOfOccurrence probabilityOfOccurrence;
    private final String description;
    private final String details;
    private final String contingencyPlanning;
    private final String mitigationStrategy;
    private final RiskService riskService;

    private boolean isAnyQueryParameterSet() {
        return nonNull(severity) ||
                nonNull(probabilityOfOccurrence) ||
                nonNull(description) ||
                nonNull(details) ||
                nonNull(contingencyPlanning) ||
                nonNull(mitigationStrategy);
    }

    public List<RiskTO> performListRequest() {
        if (isAnyQueryParameterSet()) {
            return specifyMitigationStrategy(
                    specifyContingencyPlanningFilter(
                            specifyDetailsFilter(specifyDescriptionFilter(
                                            specifyProbabilityOfOccurrenceFilter(
                                                    specifySeverityFilter(
                                                            riskService.listFilteredWith().severity()
                                                    )
                                            )
                                    )
                            )
                    )
            ).toList();
        }
        return riskService.listAll();
    }

    private FilterProbabilityOfOccurrenceStep specifySeverityFilter(SeverityFilterSpec severityFilterSpec) {
        if (nonNull(severity)) {
            return severityFilterSpec.isEqualTo(severity);
        }
        return severityFilterSpec.isIgnored();
    }

    private FilterDescriptionStep specifyProbabilityOfOccurrenceFilter(FilterProbabilityOfOccurrenceStep probabilityOfOccurrenceStep) {
        ProbabilityOfOccurrenceFilterSpec filterSpec = probabilityOfOccurrenceStep.andProbabilityOfOccurrence();
        if (nonNull(probabilityOfOccurrence)) {
            return filterSpec.isEqualTo(probabilityOfOccurrence);
        }
        return filterSpec.isIgnored();
    }

    private FilterDetailsStep specifyDescriptionFilter(FilterDescriptionStep filterDescriptionStep) {
        DescriptionFilterSpec filterSpec =  filterDescriptionStep.andDescription();
        if (nonNull(description)) {
            return filterSpec.contains(description);
        }
        return filterSpec.isIgnored();
    }

    private FilterContingencyPlanningStep specifyDetailsFilter(FilterDetailsStep filterDetailsStep) {
        DetailsFilterSpec filterSpec =  filterDetailsStep.andDetails();
        if (nonNull(details)) {
            return filterSpec.contains(details);
        }
        return filterSpec.isIgnored();
    }

    private FilterMitigationStrategyStep specifyContingencyPlanningFilter(FilterContingencyPlanningStep filterContingencyPlanningStep) {
        ContingencyPlanningFilterSpec filterSpec = filterContingencyPlanningStep.andContingencyPlanning();
        return switch (contingencyPlanning) {
            case "isEmpty()" -> filterSpec.isEmpty();
            case null -> filterSpec.isIgnored();
            default -> filterSpec.contains(contingencyPlanning);
        };
    }

    private FinalFilterStep specifyMitigationStrategy(FilterMitigationStrategyStep filterMitigationStrategyStep) {
       MitigationStrategyFilterSpec mitigationStrategyFilterSpec = filterMitigationStrategyStep.andMitigationStrategy();
       return switch (mitigationStrategy) {
           case "isEmpty()" -> mitigationStrategyFilterSpec.isEmpty();
           case null -> mitigationStrategyFilterSpec.isIgnored();
           default -> mitigationStrategyFilterSpec.contains(mitigationStrategy);
       };
    }

    private QueryParameterEvaluator(Builder builder) {
        this.riskService = requireNonNull(builder.riskService,  "riskService must not be null");
        this.severity = builder.severity;
        this.probabilityOfOccurrence = builder.probabilityOfOccurrence;
        this.description = builder.description;
        this.details = builder.details;
        this.contingencyPlanning = builder.contingencyPlanning;
        this.mitigationStrategy = builder.mitigationStrategy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Severity severity;

        public Builder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        private ProbabilityOfOccurrence probabilityOfOccurrence;
        public Builder probabilityOfOccurrence(ProbabilityOfOccurrence probability) {
            this.probabilityOfOccurrence = probability;
            return this;
        }

        private String description;
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        private String details;
        public Builder details(String details) {
            this.details = details;
            return this;
        }
        private String contingencyPlanning;
        public Builder contingencyPlanning(String contingencyPlanning) {
            this.contingencyPlanning = contingencyPlanning;
            return this;
        }
        private String mitigationStrategy;
        public Builder mitigationStrategy(String mitigationStrategy) {
            this.mitigationStrategy = mitigationStrategy;
            return this;
        }

        private RiskService riskService;
        public Builder riskService(RiskService riskService) {
            this.riskService = riskService;
            return this;
        }

        public QueryParameterEvaluator build() {
            return new QueryParameterEvaluator(this);
        }
    }
}
