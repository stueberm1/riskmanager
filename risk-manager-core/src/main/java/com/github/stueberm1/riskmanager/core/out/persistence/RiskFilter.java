package com.github.stueberm1.riskmanager.core.out.persistence;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

/// The {@code RiskFilter} defines abstract some filter options of a {@link RiskDao}. Since the {@link RiskDao} is
/// abstract for the reasons described in {@code RiskDao}s Javadoc, the Riskfilter needs also to be abstract, so it can
/// get extended as required.
///
/// Each attribute of the {@code RiskFilter} defines a filter option in the way its attribute name describes.
///
/// @see "https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle"
/// @see "https://www.dbvis.com/thetable/a-complete-guide-to-the-sql-like-operator"
/// @implSpec The attributes itself does not describe, how to combine them. The definition how to aggregate them is defined
///     by {@link DecisionSpec}: The attributes are aggregated in a 'and'-relation, so the result **must** meet **all**
///     of the specified filters.
///     {@code contains}-operations **must** work like the like operation {@code %expression%} in ansi-sql, while
///     {@code isEqualTo}-operations **must** return the exact value (ignore case)
public abstract class RiskFilter {

    private final Severity severityIsEqualTo;

    private final ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo;

    private final String descriptionContains;

    private final String detailsContains;

    private final String contingencyPlanningContains;

    private final Boolean contingencyPlanningIsEmpty;

    private final String mitigationStrategyContains;

    private final Boolean mitigationStrategyIsEmpty;


    /// The contingence plan **must** contain the given string in a like behavior.
    /// @return part string of the contingency planning
    public String contingencyPlanningContains() {
        return contingencyPlanningContains;
    }

    /// The contingency plan **must** be unspecified yet
    public Boolean contingencyPlanningIsEmpty() {
        return contingencyPlanningIsEmpty;
    }

    public String descriptionContains() {
        return descriptionContains;
    }

    public String detailsContains() {
        return detailsContains;
    }

    public String mitigationStrategyContains() {
        return mitigationStrategyContains;
    }

    public Boolean mitigationStrategyIsEmpty() {
        return mitigationStrategyIsEmpty;
    }

    public ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo() {
        return probabilityOfOccurrenceIsEqualTo;
    }

    public Severity severityIsEqualTo() {
        return severityIsEqualTo;
    }

    protected RiskFilter(Builder<?, ? extends RiskFilter> builder) {
        this.severityIsEqualTo = builder.severityIsEqualTo;
        this.contingencyPlanningContains = builder.contingencyPlanningContains;
        this.contingencyPlanningIsEmpty = builder.contingencyPlanningIsEmpty;
        this.probabilityOfOccurrenceIsEqualTo = builder.probabilityOfOccurrenceIsEqualTo;
        this.descriptionContains = builder.descriptionContains;
        this.detailsContains = builder.detailsContains;
        this.mitigationStrategyContains = builder.mitigationStrategyContains;
        this.mitigationStrategyIsEmpty = builder.mitigationStrategyIsEmpty;
    }

    public interface FilterSpec<T, R extends RiskFilter> {
        SeverityFilterSpec<T, R> severity();
        ProbabilityOfOccurrenceFilterSpec<T, R> probabilityOfOccurrence();
        MitigationStrategySpec<T, R> mitigationStrategy();
        ContingencyPlanningSpec<T, R> contingencyPlanning();
        DescriptionSpec<T,R> description();
        DetailsSpec<T,R> details();
    }

    public interface DecisionSpec<T, R extends RiskFilter> {
        FilterSpec<T, R> and();
        R create();
    }

    public interface SeverityFilterSpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> isEqualTo(Severity severity);
    }

    private static class SeverityFilterSpecImpl<T extends Builder<T, R>, R extends RiskFilter>
            implements SeverityFilterSpec<T, R> {

        final Builder<T, R> builder;

        public SeverityFilterSpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> isEqualTo(Severity severity) {
            builder.severityIsEqualTo = severity;
            return builder;
        }
    }

    public interface ProbabilityOfOccurrenceFilterSpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> isEqualTo(ProbabilityOfOccurrence probabilityOfOccurrence);
    }

    private static class ProbabilityOfOccurrenceFilterSpecImpl<T extends Builder<T, R>, R extends RiskFilter>
            implements ProbabilityOfOccurrenceFilterSpec<T, R> {

        final Builder<T, R> builder;

        public ProbabilityOfOccurrenceFilterSpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> isEqualTo(ProbabilityOfOccurrence probabilityOfOccurrence) {
            builder.probabilityOfOccurrenceIsEqualTo = probabilityOfOccurrence;
            return builder;
        }
    }

    public interface MitigationStrategySpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> contains(String mitigationStrategy);
        DecisionSpec<T, R> isEmpty();
    }

    private static class MitigationStrategySpecImpl<T extends Builder<T, R>, R extends RiskFilter> implements MitigationStrategySpec<T, R> {
        final Builder<T, R> builder;

        public MitigationStrategySpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> contains(String mitigationStrategy) {
            builder.mitigationStrategyContains = mitigationStrategy;
            return builder;
        }

        @Override
        public DecisionSpec<T, R> isEmpty() {
            builder.mitigationStrategyIsEmpty = true;
            return builder;
        }
    }

    public interface ContingencyPlanningSpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> contains(String contingencyPlanningContains);
        DecisionSpec<T, R> isEmpty();
    }

    private static class ContingencyPlanningSpecImpl<T extends Builder<T, R>, R extends RiskFilter> implements ContingencyPlanningSpec<T, R> {
        final Builder<T, R> builder;

        public ContingencyPlanningSpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> contains(String contingencyPlanningContains) {
            builder.contingencyPlanningContains = contingencyPlanningContains;
            return builder;
        }

        @Override
        public DecisionSpec<T, R> isEmpty() {
            builder.contingencyPlanningIsEmpty = true;
            return builder;
        }
    }

    public interface DescriptionSpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> contains(String description);
    }

    private static class DescriptionSpecImpl<T extends Builder<T, R>, R extends RiskFilter> implements DescriptionSpec<T, R> {
        final Builder<T, R> builder;

        public DescriptionSpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> contains(String description) {
            builder.descriptionContains = description;
            return builder;
        }
    }

    public interface DetailsSpec<T, R extends RiskFilter> {
        DecisionSpec<T, R> contains(String details);
    }

    private static class DetailsSpecImpl<T extends Builder<T, R>, R extends RiskFilter> implements DetailsSpec<T, R> {
        final Builder<T, R> builder;

        public DetailsSpecImpl(Builder<T, R> builder) {
            this.builder = builder;
        }

        @Override
        public DecisionSpec<T, R> contains(String details) {
            builder.detailsContains = details;
            return builder;
        }
    }


    protected static abstract class Builder<T extends Builder<T,R>, R extends RiskFilter>
            implements FilterSpec<T, R>,  DecisionSpec<T, R> {

        private Severity severityIsEqualTo;

        private ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo;

        private String descriptionContains;

        private String detailsContains;

        private String contingencyPlanningContains;

        private Boolean contingencyPlanningIsEmpty;

        private String mitigationStrategyContains;

        private Boolean mitigationStrategyIsEmpty;

        @Override
        public FilterSpec<T, R> and() {
            return self();
        }

        @Override
        public ProbabilityOfOccurrenceFilterSpec<T, R> probabilityOfOccurrence() {
            return new ProbabilityOfOccurrenceFilterSpecImpl<>(self());
        }

        @Override
        public SeverityFilterSpec<T, R> severity() {
            return new SeverityFilterSpecImpl<>(self());
        }

        @Override
        public MitigationStrategySpec<T, R> mitigationStrategy() {
            return new MitigationStrategySpecImpl<>(self());
        }

        @Override
        public ContingencyPlanningSpec<T, R> contingencyPlanning() {
            return new ContingencyPlanningSpecImpl<>(self());
        }

        @Override
        public DescriptionSpec<T, R> description() {
            return new DescriptionSpecImpl<>(self());
        }

        @Override
        public DetailsSpec<T, R> details() {
            return new DetailsSpecImpl<>(self());
        }

        protected abstract T self();

    }

}
