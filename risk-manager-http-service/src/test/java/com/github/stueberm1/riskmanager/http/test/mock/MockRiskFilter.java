package com.github.stueberm1.riskmanager.http.test.mock;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.in.risk.SeverityFilterSpec;
import com.github.stueberm1.riskmanager.core.in.risk.filter.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

import java.util.Collections;
import java.util.List;


public class MockRiskFilter implements FilterSpec, FilterProbabilityOfOccurrenceStep, FilterDescriptionStep, FilterDetailsStep,
        FilterContingencyPlanningStep, FilterMitigationStrategyStep, FinalFilterStep {


    private  Severity severityIsEqualTo;

    private  ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo;

    private  String descriptionContains;

    private  String detailsContains;

    private  String contingencyPlanningContains;

    private  Boolean contingencyPlanningIsEmpty;

    private  String mitigationStrategyContains;

    private  Boolean mitigationStrategyIsEmpty;


    public String getContingencyPlanningContains() {
        return contingencyPlanningContains;
    }

    public Boolean getContingencyPlanningIsEmpty() {
        return contingencyPlanningIsEmpty;
    }

    public String getDescriptionContains() {
        return descriptionContains;
    }

    public String getDetailsContains() {
        return detailsContains;
    }

    public String getMitigationStrategyContains() {
        return mitigationStrategyContains;
    }

    public Boolean getMitigationStrategyIsEmpty() {
        return mitigationStrategyIsEmpty;
    }

    public ProbabilityOfOccurrence getProbabilityOfOccurrenceIsEqualTo() {
        return probabilityOfOccurrenceIsEqualTo;
    }

    public Severity getSeverityIsEqualTo() {
        return severityIsEqualTo;
    }

    public static FilterSpec newInstance() {
        return new MockRiskFilter();
    }

    private MockRiskFilter() {
        // use the static factory-method or  the builder
    }

    public static Builder builder() {
        return new Builder();
    }

    private MockRiskFilter(Builder builder) {
        this.severityIsEqualTo = builder.severityIsEqualTo;
        this.probabilityOfOccurrenceIsEqualTo = builder.probabilityOfOccurrenceIsEqualTo;
        this.descriptionContains = builder.descriptionContains;
        this.detailsContains = builder.detailsContains;
        this.contingencyPlanningContains = builder.contingencyPlanningContains;
        this.contingencyPlanningIsEmpty = builder.contingencyPlanningIsEmpty;
        this.mitigationStrategyContains = builder.contingencyPlanningContains;
        this.mitigationStrategyIsEmpty = builder.mitigationStrategyIsEmpty;
    }

    public static final class Builder {
        private Severity severityIsEqualTo;
        private ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo;
        private String descriptionContains;
        private String detailsContains;
        private String contingencyPlanningContains;
        private Boolean contingencyPlanningIsEmpty;
        private Boolean mitigationStrategyIsEmpty;
        private Boolean mitigationStrategyIsNotEmpty;

        public MockRiskFilter build() {
            return new MockRiskFilter(this);
        }
    }

    @Override
    public SeverityFilterSpec severity() {
        return new SeverityFilterConfigurer(this);
    }

    private static class SeverityFilterConfigurer implements SeverityFilterSpec {

        private final MockRiskFilter root;

        SeverityFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FilterProbabilityOfOccurrenceStep isEqualTo(Severity value) {
            root.severityIsEqualTo = value;
            return root;
        }

        @Override
        public FilterProbabilityOfOccurrenceStep isIgnored() {
            root.severityIsEqualTo = null;
            return root;
        }
    }

    @Override
    public ProbabilityOfOccurrenceFilterSpec andProbabilityOfOccurrence() {
        return new ProbabilityOfOccurrenceFilterConfigurer(this);
    }

    private static class ProbabilityOfOccurrenceFilterConfigurer implements ProbabilityOfOccurrenceFilterSpec {
        private final MockRiskFilter root;

        ProbabilityOfOccurrenceFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FilterDescriptionStep isEqualTo(ProbabilityOfOccurrence value) {
            root.probabilityOfOccurrenceIsEqualTo = value;
            return root;
        }

        @Override
        public FilterDescriptionStep isIgnored() {
            root.probabilityOfOccurrenceIsEqualTo = null;
            return root;
        }
    }

    @Override
    public DescriptionFilterSpec andDescription() {
        return null;
    }

    private static class DescriptionFilterConfigurer implements DescriptionFilterSpec {
        private final MockRiskFilter root;

        DescriptionFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FilterDetailsStep contains(String snippet) {
            root.descriptionContains = snippet;
            return root;
        }

        @Override
        public FilterDetailsStep isIgnored() {
            root.descriptionContains = null;
            return root;
        }
    }

    @Override
    public DetailsFilterSpec andDetails() {
        return new  DetailsFilterConfigurer(this);
    }
    private static class DetailsFilterConfigurer implements DetailsFilterSpec {

        private final MockRiskFilter root;

        DetailsFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FilterContingencyPlanningStep contains(String snippet) {
            root.detailsContains = snippet;
            return root;
        }

        @Override
        public FilterContingencyPlanningStep isIgnored() {
            root.detailsContains = null;
            return root;
        }
    }

    @Override
    public ContingencyPlanningFilterSpec andContingencyPlanning() {
        return new ContingencyPlanningFilterConfigurer(this);
    }

    private static class ContingencyPlanningFilterConfigurer implements ContingencyPlanningFilterSpec {
        private final MockRiskFilter root;

        ContingencyPlanningFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FilterMitigationStrategyStep contains(String snippet) {
            root.mitigationStrategyContains = snippet;
            return root;
        }

        @Override
        public FilterMitigationStrategyStep isEmpty() {
            root.mitigationStrategyIsEmpty = true;
            return root;
        }

        @Override
        public FilterMitigationStrategyStep isIgnored() {
            root.mitigationStrategyIsEmpty = null;
            root.severityIsEqualTo = null;
            return root;
        }
    }

    @Override
    public MitigationStrategyFilterSpec andMitigationStrategy() {
        return new MitigationStrategyFilterConfigurer(this);
    }

    private static class MitigationStrategyFilterConfigurer implements MitigationStrategyFilterSpec {
        private final MockRiskFilter root;

        MitigationStrategyFilterConfigurer(MockRiskFilter root) {
            this.root = root;
        }

        @Override
        public FinalFilterStep contains(String snippet) {
            root.mitigationStrategyContains = snippet;
            return root;
        }

        @Override
        public FinalFilterStep isEmpty() {
            root.mitigationStrategyIsEmpty = true;
            return root;
        }

        @Override
        public FinalFilterStep isIgnored() {
            root.mitigationStrategyIsEmpty = null;
            root.severityIsEqualTo = null;
            return root;
        }
    }

    @Override
    public List<RiskTO> toList() {
        return Collections.emptyList();
    }

}
