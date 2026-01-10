package com.github.stueberm1.riskmanager.core.out.persistence;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

/// The {@code RiskFilter} defines abstract some filter options of a {@link RiskDao}. Since the {@link RiskDao} is
/// abstract for the reasons described in {@code RiskDao}s Javadoc, the Riskfilter needs also to be abstract, so it can
/// get extended as required.
///
/// Each attribute of the {@code RiskFilter} defines a filter option in the way its attribute name describes.
///
/// ## Usage
/// The {@code RiskFilter} provides an extensible, self-explaining fluid api to configure the filter in a proper way,
/// without any misinterpretable orders.
///
/// Ideally, extensions of the abstract {@code Riskfilter} provides a static factory operation returning a {@link FilterSpec}
/// instance. The {@link FilterSpec} is backed by a realization of the abstract {@link Builder}.
///
/// ```java
/// public static FilterSpec findRisksWhere() {
///         return new Builder();
///     }
/// ```
///
/// Having such a factory, the configuration of a concrete {@code RiskFilter} is easy and descriptive.
///
/// ```java
/// RiskFilter filter = SimpleRiskFilter.findRisksWhere().severity()isEqualTo(Severity.MEDIUM)
///                                     .and().mitigationStrategy().isEmpty()
///                                     .create();
/// ```
///
///
/// @see "https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle"
/// @see "https://www.dbvis.com/thetable/a-complete-guide-to-the-sql-like-operator"
/// @apiNote  The attributes itself does not describe, how to combine them. The definition how to aggregate them is defined
///     by {@link DecisionSpec}: The attributes are aggregated in a 'and'-relation, so the result **must** meet **all**
///     of the specified filters.
///     {@code contains}-operations **must** work like the like-operation (){@code where value like %expression%}) in ansi-sql,
///  while {@code isEqualTo}-operations **must** return the exact value (ignore case)
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

    /// The contingency plan **must** be unspecified yet.
    /// @return if true the contingency planning **must** be unset or be irrelevant for the query aka {@code null}
    public Boolean contingencyPlanningIsEmpty() {
        return contingencyPlanningIsEmpty;
    }

    ///  The description (headline) of the risk **must** contain the given text.
    /// @return the partial text the system is looking for
    public String descriptionContains() {
        return descriptionContains;
    }

    /// The details section of the risk **must** contain the given text.
    /// @return the partial text the system is looking for
    public String detailsContains() {
        return detailsContains;
    }

    /// The mitigation strategy **must** contain the configured text.
    /// @return the text the provider must looking for in the mitigation strategy
    public String mitigationStrategyContains() {
        return mitigationStrategyContains;
    }

    ///  If set, the mitigation strategy **must** be unset yet.
    /// @return true, if the mitigation strategy should  be empty
    public Boolean mitigationStrategyIsEmpty() {
        return mitigationStrategyIsEmpty;
    }

    /// The resulting {@link RiskDao} **must** all have the given probability of occurrence.
    /// @return search filter
    public ProbabilityOfOccurrence probabilityOfOccurrenceIsEqualTo() {
        return probabilityOfOccurrenceIsEqualTo;
    }

    /// The resulting {@link RiskDao} **must** all have the given severity.
    /// @return search filter
    public Severity severityIsEqualTo() {
        return severityIsEqualTo;
    }

    /// The {@code FilterSpec} specifies the attributes of the {@link RiskDao} the persistence provider **must*
    /// be able to look for.
    ///
    /// Each attribute-operation (such as {@link FilterSpec#severity()}) must return the filter-operations specified for
    /// that  attribute.
    /// To provide the fluent api described in the javadoc header of {@link RiskFilter}, each filter-operation **must**
    /// return a {@link DecisionSpec}.
    ///
    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
    public interface FilterSpec<T, R extends RiskFilter> {
        SeverityFilterSpec<T, R> severity();
        ProbabilityOfOccurrenceFilterSpec<T, R> probabilityOfOccurrence();
        MitigationStrategySpec<T, R> mitigationStrategy();
        ContingencyPlanningSpec<T, R> contingencyPlanning();
        DescriptionSpec<T,R> description();
        DetailsSpec<T,R> details();
    }

    /// The {@code DecisionSpec} allows clients of the api to decide either to add another constraint to the
    /// {@link RiskFilter} or to create the final {@code RiskFilter}.
    ///
    /// It is used as the result of a filter-operation definition.
    ///
    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
    public interface DecisionSpec<T, R extends RiskFilter> {
        /// Adds another and-conjunct filter-option to the {@link RiskFilter}.
        /// @return Specification of the next filter
        FilterSpec<T, R> and();

        ///  Returns a new instance of the {@link RiskFilter} with the current configuration
        /// @return The final configured {@link RiskFilter}
        R create();
    }

    /// Specification of the Severity filter options.
    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
    public interface SeverityFilterSpec<T, R extends RiskFilter> {

        /// Adds an isEqualTo filter on severity to the {@link RiskFilter}.
        /// @param severity the required severity
        /// @return the decision point in the configuration flow
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

    /// Specification of the probability of occurrence filter options.
    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
    public interface ProbabilityOfOccurrenceFilterSpec<T, R extends RiskFilter> {

        /// Adds an isEqualTo filter on probability of occurrence to the {@link RiskFilter}.
        /// @param probabilityOfOccurrence the required probability of occurrence
        /// @return the decision point in the configuration flow
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

    /// Specification of the mitigation strategy filter options.
    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
    public interface MitigationStrategySpec<T, R extends RiskFilter> {

        ///  Looks for risks containing the given string in the mitigation strategy
        /// @param mitigationStrategy a part of the mitigation strategy description
        /// @return the decision point in the configuration flow
        DecisionSpec<T, R> contains(String mitigationStrategy);

        ///  Looks for risks without a mitigation strategy.
        /// @return the decision point in the configuration flow
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

    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
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

    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
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

    /// @param <T> Type of the concrete {@link FilterSpec} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
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

    ///
    /// @param builder basic configuration parameters of the new filter
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


    /// The {@code Builder} is the abstract realization of the {@link FilterSpec} as well as of the {@link DecisionSpec}.
    /// Concrete {@link RiskFilter} realization also **must** extend this builder and **must** at least realize
    /// {@link DecisionSpec#create()}.
    ///
    /// @param <T> Type of the concrete {@link Builder} realization
    /// @param <R> Type of the resulting {@link RiskFilter} realization
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

        /// Returns the concrete Builder itself. It allows the abstract builder to access the concrete realization without
        /// knowing them directly.
        ///
        /// @return typed instance of the concrete builder
        /// @implSpec
        /// Implementations **must** return the concrete Builder having the specified type.
        ///
        ///```java
        ///   @Override
        ///   public ConcreteBuilder self( {
        ///       return this;
        ///   }
        ///```
        protected abstract T self();

    }

}
