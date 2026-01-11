package com.github.stueberm1.riskmanager.core.application.risk.list;

import static java.util.Objects.isNull;

import com.github.stueberm1.riskmanager.core.application.risk.RiskConverter;
import com.github.stueberm1.riskmanager.core.in.risk.*;
import com.github.stueberm1.riskmanager.core.in.risk.filter.*;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskFilter;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

import java.util.List;
import java.util.Objects;

public class RiskFilterBuilder implements FilterSpec, FilterProbabilityOfOccurrenceStep, FilterDescriptionStep, FilterDetailsStep,
        FilterContingencyPlanningStep, FilterMitigationStrategyStep, FinalFilterStep {


    private final RiskLister riskLister;
    private final RiskConverter converter;
    private final RiskFilter.FilterSpec<?, ?> filterSpec;
    private RiskFilter.DecisionSpec<?,?> decisionSpec;

    public RiskFilterBuilder(RiskLister riskLister, RiskConverter converter) {
        this.riskLister = riskLister;
        this.converter = converter;
        filterSpec = SimpleRiskFilter.findRisksWhere();
    }

    private static abstract class FilterConfigurer {
        final RiskFilterBuilder root;
        final RiskFilter.FilterSpec<?, ?> filterSpec;

        public FilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            this.filterSpec = filterSpec;
            this.root = root;
        }
    }

    private RiskFilter.FilterSpec<?,?> getFilterSpec() {
        if (isNull(this.decisionSpec)) {
            return this.filterSpec;
        }
        return decisionSpec.and();
    }

    @Override
    public SeverityFilterSpec severity() {
        return new SeverityFilterConfigurer(getFilterSpec(), this);
    }

    private static class SeverityFilterConfigurer extends FilterConfigurer implements SeverityFilterSpec {

        public SeverityFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FilterProbabilityOfOccurrenceStep isEqualTo(Severity value) {
            root.decisionSpec = root.filterSpec.severity().isEqualTo(value);
            return root;
        }

        @Override
        public FilterProbabilityOfOccurrenceStep isIgnored() {
            return root;
        }
    }

    @Override
    public ProbabilityOfOccurrenceFilterSpec andProbabilityOfOccurrence() {
        return new ProbabilityOfOccurrenceFilterConfigurer(getFilterSpec(), this);
    }

    private static class ProbabilityOfOccurrenceFilterConfigurer extends FilterConfigurer implements ProbabilityOfOccurrenceFilterSpec {

        public ProbabilityOfOccurrenceFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FilterDescriptionStep isEqualTo(ProbabilityOfOccurrence value) {
           root.decisionSpec = filterSpec.probabilityOfOccurrence().isEqualTo(value);
           return root;
        }

        @Override
        public FilterDescriptionStep isIgnored() {
            return root;
        }
    }

    @Override
    public DescriptionFilterSpec andDescription() {
        return new DescriptionFilterConfigurer(getFilterSpec(), this);
    }

    private static class DescriptionFilterConfigurer extends FilterConfigurer implements DescriptionFilterSpec {
        public DescriptionFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FilterDetailsStep contains(String snippet) {
            root.decisionSpec = filterSpec.description().contains(snippet);
            return root;
        }

        @Override
        public FilterDetailsStep isIgnored() {
            return root;
        }
    }

    @Override
    public DetailsFilterSpec andDetails() {
        return new DetailsFilterConfigurer(getFilterSpec(), this);
    }

    private static class DetailsFilterConfigurer extends FilterConfigurer implements DetailsFilterSpec {

        public DetailsFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FilterContingencyPlanningStep contains(String snippet) {
            root.decisionSpec = filterSpec.details().contains(snippet);
            return root;
        }

        @Override
        public FilterContingencyPlanningStep isIgnored() {
            return root;
        }
    }

    @Override
    public ContingencyPlanningFilterSpec andContingencyPlanning() {
        return new ContingencyPlanningFilterConfigurer(getFilterSpec(), this);
    }

    private static class ContingencyPlanningFilterConfigurer extends FilterConfigurer implements ContingencyPlanningFilterSpec {
        public ContingencyPlanningFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FilterMitigationStrategyStep contains(String snippet) {
           root.decisionSpec =  filterSpec.contingencyPlanning().contains(snippet);
           return root;
        }

        @Override
        public FilterMitigationStrategyStep isEmpty() {
           root.decisionSpec = filterSpec.contingencyPlanning().isEmpty();
           return root;
        }

        @Override
        public FilterMitigationStrategyStep isIgnored() {
            return root;
        }
    }

    @Override
    public MitigationStrategyFilterSpec andMitigationStrategy() {
        return new MitigationStrategyFilterConfigurer(getFilterSpec(), this);
    }

    private static class MitigationStrategyFilterConfigurer extends FilterConfigurer implements MitigationStrategyFilterSpec {

        public MitigationStrategyFilterConfigurer(RiskFilter.FilterSpec<?, ?> filterSpec, RiskFilterBuilder root) {
            super(filterSpec, root);
        }

        @Override
        public FinalFilterStep contains(String snippet) {
            root.decisionSpec = filterSpec.mitigationStrategy().contains(snippet);
            return root;
        }

        @Override
        public FinalFilterStep isEmpty() {
            root.decisionSpec = filterSpec.mitigationStrategy().isEmpty();
            return root;
        }

        @Override
        public FinalFilterStep isIgnored() {
            return root;
        }
    }

    @Override
    public List<RiskTO> toList() {
        if (isNull(decisionSpec)) {
            throw new InvalidFilterConfigurationException("At least one search-filter must be specified");
        }

        return riskLister.listWithFilter(decisionSpec.create()).stream()
                .filter(Objects::nonNull)
                .map(converter::convert)
                .toList();
    }
}
