package com.github.stueberm1.riskmanager.core.out.persistence;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

import static java.util.Objects.requireNonNull;


/// The {@code RiskDao} is a simple container object (bean) which allows to send the information  of a specific risk to
/// and retrieve them from a concrete persistence provider.
///
/// The {@code RiskDo} is abstract to make later changes easier without more impact to core as well as for persistence providers
/// as possible. Using this, modifications can be realized as required.
///
///  | Constraint (OCL)                                                     | Prose description                             |
///  |----------------------------------------------------------------------|-----------------------------------------------|
///  | **context** Risk **inv**: self.id -> notNull()                       | A Risk has always an identifier               |
///  | **context** Risk **inv**: self.severity -> notNull()                 | A Risk has always a severity                  |
///  | **context** Risk **inv**: self.probabilityOfOccurrence -> notNull()  | A Risk has always a probability of occurrence |
///
public abstract class RiskDao {

    private final RiskIdentifier id;
    private final Severity severity;
    private final ProbabilityOfOccurrence probabilityOfOccurrence;
    private final String description;
    private final String details;
    private final String contingencyPlanning;
    private final String mitigationStrategy;


    public RiskIdentifier id() {
        return id;
    }

    public Severity severity() {
        return severity;
    }

    public ProbabilityOfOccurrence probabilityOfOccurrence() {
        return probabilityOfOccurrence;
    }

    public String description() {
        return description;
    }

    public String details() {
        return details;
    }

    public Optional<String> contingencyPlanning() {
        return Optional.ofNullable(contingencyPlanning);
    }

    public Optional<String> getMitigationStrategy() {
        return Optional.ofNullable(mitigationStrategy);
    }

    /// The abstract constructor takes a realization of the abstract {@link Builder} as argument to create a consistent
    /// {@code RiskDao}.
    ///
    /// Realizations of the abstract {@code Risk} **must** also implement the abstract {@link Builder} to create an instance
    /// of the {@code RiskDao}. The idea is, that the constructor takes a concrete realization of the abstract Builder
    /// to configure a consistent {@code RiskDao}.
    ///
    /// @param builder a concrete realization of the abstract {@link Builder}.
    ///
    /// @throws NullPointerException if one of the mandatory fields (id, severity, probabilityOfOccurrence, description
    /// and/or details) are missing.
    /// @implSpec
    /// Realizations must call the super-constructor with the concrete builder before setting potential additional fields.
    /// ``````java
    /// public ConcreteRiskDao(ConcreteBuilder concreteRiskBuilder) {
    ///     super(concreteRiskBuilder);
    ///     this.additionalField = concreteRiskBuilder.additionalField;
    /// }
    ///
    /// ``````
    protected RiskDao(Builder<?> builder) {
        this.id = requireNonNull(builder.id, "id");
        this.severity = requireNonNull(builder.severity, "severity");
        this.description = requireNonNull(builder.description, "description");
        this.details = requireNonNull(builder.details, "details");
        this.contingencyPlanning = builder.contingencyPlanning;
        this.mitigationStrategy = builder.mitigationStrategy;
        this.probabilityOfOccurrence =  requireNonNull(builder.probabilityOfOccurrence, "probability of occurrence");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RiskDao riskDao = (RiskDao) o;

        return new EqualsBuilder()
                .append(id, riskDao.id)
                .append(severity, riskDao.severity)
                .append(probabilityOfOccurrence, riskDao.probabilityOfOccurrence)
                .append(description, riskDao.description)
                .append(details, riskDao.details)
                .append(contingencyPlanning, riskDao.contingencyPlanning)
                .append(mitigationStrategy, riskDao.mitigationStrategy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(severity)
                .append(probabilityOfOccurrence)
                .append(description)
                .append(details)
                .append(contingencyPlanning)
                .append(mitigationStrategy)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("severity", severity)
                .append("probabilityOfOccurrence", probabilityOfOccurrence)
                .append("description", description)
                .append("details", details)
                .append("contingencyPlanning", contingencyPlanning)
                .append("mitigationStrategy", mitigationStrategy)
                .toString();
    }

    /// The {@code Builder} enforces the business rules for the abstract {@link RiskDao}. It is a container for
    /// all configuration parameters which are required to build a {@code RiskDao}.
    ///
    /// It provides an extensible and generic mechanism to create consistent Risks using a typesafe fluid api. Concrete builders
    /// can extend this abstract builder registering themselves as type.
    ///
    /// ## Extending the Builder
    ///
    /// Extensions of the abstract Builder **must** Register themselves as type so the fluid api gets operational
    ///
    /// ```java
    /// public static final class ConcreteBuilder extends Builder<ConcreteBuilder> {
    /// }
    /// ```
    ///
    /// The fluid-api in the constructor uses the abstract operation The operation {@link Builder#self()} which
    ///  **must** return the concrete builder instance (`this`). This is necessary because the fluid configuration
    /// operation would return an instance typed with the abstract builder otherwise.
    ///
    /// The ```build()```-operation might be overridden in a way, it returns an instance of the concrete `Risk`.
    /// ```java
    ///     public ConcreteRisk build() {
    ///         return new ConcreteRisk(this)
    ///    }
    /// ```
    ///
    /// @param <T> Type of the concrete Builder (implementation)
    ///
    protected static abstract class Builder<T extends Builder<T>> {
        private RiskIdentifier id;
        public T hasId(RiskIdentifier id) {
            this.id = id;
            return self();
        }

        private String description;
        public T havingDescription(String description) {
            this.description = description;
            return self();
        }

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

        private String details;
        public T withDetailedInformation(String details) {
            this.details = details;
            return self();
        }

        private String contingencyPlanning;
        public T contingencyPlanning(String contingencyPlanning) {
            this.contingencyPlanning = contingencyPlanning;
            return self();
        }

        private String mitigationStrategy;
        public T mitigationStrategy(String mitigationStrategy) {
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

        protected abstract RiskDao build();
    }
}
