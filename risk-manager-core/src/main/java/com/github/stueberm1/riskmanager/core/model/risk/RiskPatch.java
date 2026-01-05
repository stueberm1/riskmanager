package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Optional;

public abstract class RiskPatch {
    private final Severity severity;
    private final ProbabilityOfOccurrence probabilityOfOccurrence;
    private final Details details;
    private final ContingencyPlanning contingencyPlanning;
    private final MitigationStrategy mitigationStrategy;

    public Optional<Severity> getSeverity() {
        return Optional.ofNullable(severity);
    }

    public Optional<ProbabilityOfOccurrence> getProbabilityOfOccurrence() {
        return Optional.ofNullable(probabilityOfOccurrence);
    }

    public Optional<Details> getDetails() {
        return Optional.ofNullable(details);
    }

    public Optional<ContingencyPlanning> getContingencyPlanning() {
        return Optional.ofNullable(contingencyPlanning);
    }

    public Optional<MitigationStrategy> getMitigationStrategy() {
        return Optional.ofNullable(mitigationStrategy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RiskPatch riskPatch = (RiskPatch) o;

        return new EqualsBuilder().append(severity, riskPatch.severity)
                .append(probabilityOfOccurrence, riskPatch.probabilityOfOccurrence)
                .append(details, riskPatch.details).append(contingencyPlanning, riskPatch.contingencyPlanning)
                .append(mitigationStrategy, riskPatch.mitigationStrategy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(severity)
                .append(probabilityOfOccurrence).append(details).append(contingencyPlanning)
                .append(mitigationStrategy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("contingencyPlanning", contingencyPlanning)
                .append("severity", severity)
                .append("probabilityOfOccurrence", probabilityOfOccurrence)
                .append("details", details)
                .append("mitigationStrategy", mitigationStrategy)
                .toString();
    }

    protected RiskPatch(Builder<?> builder) {
        this.contingencyPlanning = builder.contingencyPlanning;
        this.details = builder.details;
        this.severity = builder.severity;
        this.probabilityOfOccurrence = builder.probabilityOfOccurrence;
        this.mitigationStrategy = builder.mitigationStrategy;
    }

    protected static abstract class Builder<T extends Builder<T>> {

        private Severity severity;
        public T withSeverity(Severity severity) {
            this.severity = severity;
            return self();
        }

        private ProbabilityOfOccurrence probabilityOfOccurrence;
        public T  probabilityOfOccurrence(ProbabilityOfOccurrence probabilityOfOccurrence) {
            this.probabilityOfOccurrence = probabilityOfOccurrence;
            return self();
        }

        private Details details;
        public T withDetailedInformation(Details details) {
            this.details = details;
            return self();
        }

        private ContingencyPlanning contingencyPlanning;
        public T contingencyPlanning(ContingencyPlanning contingencyPlanning) {
            this.contingencyPlanning = contingencyPlanning;
            return self();
        }

        private MitigationStrategy mitigationStrategy;
        public T mitigationStrategy(MitigationStrategy mitigationStrategy) {
            this.mitigationStrategy = mitigationStrategy;
            return self();
        }

        ///
        /// Returns the concrete Builder itself. It allows the abstract builder to access the concrete realization without
        /// knowing them directly.
        ///
        /// @return typed instance of the concrete builder
        /// @implSpec
        /// Implementations **must** return the concrete Builder having the specified type.
        ///
        ///```java
        ///   public ConcreteBuilder self( {
        ///       return this;
        ///   }
        ///```
        ///
        protected abstract T self();

        protected abstract RiskPatch build();
    }
}
