package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

///  
/// A {@code Risk} in a (software-)project is regularly something which needs to get decided with a lack of information
/// or with a lack of capabilities in what need to do.
///
/// It is defined as {@code risk = probabilityOfOccurrence * amountOfDamage}. Since the amount of damage is as hard to
/// calculate as the probability of occurrence the {@code Risk} defines the "occurrence probability" in an abstract way
/// as {@code low}, {@code medium} and {@code high} and the amount of damage abstract as {@code Severity}
/// also as {@code low}, {@code medium} and {@code high}.
///
/// {@code OccurrenceProbability} is abstract as well as {@code Severity} so it can be implemented as needed by the projects
/// using the tool.
///
/// A special case of the {@code Severity} is {@code occurred}. If a risk is already occurred it is not longer a risk, but
/// a problem. In this case the problem might get other rules then a simple risk.
///
///  A {@code Risk} has, in addition to the severity of the risk and the probability of occurrence, always the following
///  mandatory fields: a description (headline), some more specific details and a unique id.
///
///
///  | Constraint (OCL)                                                     | Prose description                             |
///  |----------------------------------------------------------------------|-----------------------------------------------|
///  | **context** Risk **inv**: self.id -> notNull()                       | A Risk has always an identifier               |
///  | **context** Risk **inv**: self.severity -> notNull()                 | A Risk has always a severity                  |
///  | **context** Risk **inv**: self.probabilityOfOccurrence -> notNull()  | A Risk has always a probability of occurrence |
///
/// ## Concepts
///  The Risk is abstract and immutable. The abstractness enables extensions of the risk as needed without modifying the core-risk.
/// Keeping the {@code Risk} immutable enables the risk to be processed in a thread safe way.
///
///  If one of the values (e.g. contingency planning or mitigation strategy) needs to be changed (updated) a new {@code Risk}
/// gets created with the values of the "former" {@code Risk} except the new values.
///
/// THe Risk defines the  values that can be changed by abstract operations. All of these abstract operations uses (undocumented)
/// javadoc tags e.g. implSpec to describe, how to implement them.
///
/// A nested {@link Builder} class together with a protected constructor enforces that the object invariants and preconditions
/// are fulfilled.
public abstract class Risk {

    private final RiskIdentifier id;
    private final Severity severity;
    private final ProbabilityOfOccurrence probabilityOfOccurrence;
    private final Description description;
    private final Details details;
    private final ContingencyPlanning contingencyPlanning;
    private final MitigationStrategy mitigationStrategy;


    public RiskIdentifier id() {
        return id;
    }

    public Severity severity() {
        return severity;
    }

    public ProbabilityOfOccurrence probabilityOfOccurrence() {
        return probabilityOfOccurrence;
    }

    public Description description() {
        return description;
    }

    public Details details() {
        return details;
    }

    public Optional<ContingencyPlanning> contingencyPlanning() {
        return Optional.ofNullable(contingencyPlanning);
    }

    public Optional<MitigationStrategy> getMitigationStrategy() {
        return Optional.ofNullable(mitigationStrategy);
    }

    /// The abstract constructor takes a realization of the abstract {@link Builder} as argument to create a consistent
    /// {@code Risk}.
    ///
    /// Realizations of the abstract {@code Risk} **must** also implement the abstract {@link Builder} to create an instance
    /// of the {@code Risk}. The idea is, that the constructor takes a concrete realization of the abstract Builder
    /// to configure a consistent {@code Risk}.
    ///
    /// @param builder a concrete realization of the abstract {@link Builder}.
    ///
    /// @throws NullPointerException if one of the mandatory fields (id, severity, probabilityOfOccurrence, description
    /// and/or details) are missing.
    /// @throws EntityConstraintViolationException If a business constraint got violated when creating the {@code Risk}
    ///
    /// @implSpec
    /// Realizations must call the super-constructor with the concrete builder before setting potential additional fields.
    /// ``````java
    /// public ConcreteRisk(ConcreteBuilder concreteRiskBuilder) {
    ///     super(concreteRiskBuilder);
    ///     this.additionalField = concreteRiskBuilder.additionalField;
    /// }
    ///
    /// ``````
    protected Risk(Builder<?> builder) {
        this.id = requireNonNull(builder.id, "id");
        this.severity = requireNonNull(builder.severity, "severity");
        this.description = requireNonNull(builder.description, "description");
        this.details = requireNonNull(builder.details, "details");
        this.contingencyPlanning = builder.contingencyPlanning;
        this.mitigationStrategy = builder.mitigationStrategy;
        this.probabilityOfOccurrence =  requireNonNull(builder.probabilityOfOccurrence, "probability of occurrence");
        validate();
    }

    /// This is an extension point for realizations, which allows realizations of a concrete {@code Risk} to add additional
    /// constraints to the abstract core {@code Risk}.
    ///
    /// @implNote The operation is called while creating the abstract core {@code Risk} to one can neither remove
    /// constraints from the {@code Risk} nor it is possible to validate additional fields with that operation.
    /// the operation can also check fields known by the abstract {@code Risk}.
    ///
    /// @implSpec The {@link EntityConstraintViolationException} is mandatory in case of an additional constraint
    /// gets violated
    protected abstract void validate() throws EntityConstraintViolationException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Risk risk = (Risk) o;

        return new EqualsBuilder().append(id, risk.id)
                .append(severity, risk.severity).append(probabilityOfOccurrence, risk.probabilityOfOccurrence)
                .append(description, risk.description).append(details, risk.details)
                .append(contingencyPlanning, risk.contingencyPlanning)
                .append(mitigationStrategy, risk.mitigationStrategy).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                .append(severity).append(probabilityOfOccurrence).append(description)
                .append(details).append(contingencyPlanning).append(mitigationStrategy).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("severity", severity)
                .append("probabilityOfOccurrence", probabilityOfOccurrence)
                .append("description", description)
                .append("details", details)
                .append("contingencyPlanning", contingencyPlanning)
                .append("mitigationStrategy", mitigationStrategy)
                .toString();
    }

    /// The {@code Builder} enforces the business rules for the abstract {@link Risk}. It is a container for
    /// all configuration parameters which are required to build a {@code Risk}.
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

        private Description description;
        public T havingDescription(Description description) {
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

        protected abstract Risk build();
    }
}
