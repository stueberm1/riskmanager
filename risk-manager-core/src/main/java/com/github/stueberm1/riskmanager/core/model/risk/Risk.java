package com.github.stueberm1.riskmanager.core.model.risk;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import java.util.Optional;

/**
 * = Risk
 *
 * A {@code Risk} in a (software-)project is regularly something which needs to get decided with a lack of information
 * or with a lack of capabilities in what need to do.
 *
 * It is defined as {@code risk = probabilityOfOccurrence * amountOfDamage}. Since the amount of damage is as hard to
 * calculate as the probability of occurrence the {@code Risk} defines the "occurrence probability" in an abstract way
 * as {@code low}, {@code medium} and {@code high} and the amount of damage abstract as {@code Severity}
 * also as {@code low}, {@code medium} and {@code high}.
 *
 * {@code OccurrenceProbability} is abstract as well as {@code Severity} so it can be implemented as needed by the projects
 * using the tool.
 *
 * A special case of the {@code Severity} is {@code occurred}. If a risk is already occurred it is not longer a risk, but
 * a problem. In this case the problem might get other rules then a simple risk.
 *
 *  A {@code Risk} has, in addition to the severity of the risk and the probability of occurrence, always the following
 *  mandatory fields: a description (headline), some more specific details and a unique id.
 *
 *  .constraints if a risk
 *  [cols="1a,1" options="header"]
 *  |===
 *  | Constraint (OCL) | Prose description
 *  | **context** Risk **inv**: self.id -> notNull() | A Risk has always an identifier
 *  |===
 */
public abstract class Risk {

    private final RiskIdentifier id;
    private final Description description;
    private final Details details;
    private final ContingenyPlanning contingencyPlanning;
    private final MitigationStrategy mitigationStrategy;


    public RiskIdentifier id() {
        return id;
    }

    public Description description() {
        return description;
    }

    public Details details() {
        return details;
    }

    public Optional<ContingenyPlanning> contingencyPlanning() {
        return Optional.of(contingencyPlanning);
    }

    public Optional<MitigationStrategy> getMitigationStrategy() {
        return Optional.of(mitigationStrategy);
    }

    protected Risk(Builder<?> builder) {
        this.id = requireNonNull(builder.id, "id");
        this.description = requireNonNull(builder.description, "description");
        this.details = requireNonNull(builder.details, "details");
        this.contingencyPlanning = builder.contingencyPlanning;
        this.mitigationStrategy = builder.mitigationStrategy;
    }

    protected abstract void validate();

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

        private Details details;
        public T withDetailedInformation(Details details) {
            this.details = details;
            return self();
        }

        private ContingenyPlanning contingencyPlanning;
        public T contingencyPlanning(ContingenyPlanning contingenyPlanning) {
            this.contingencyPlanning = contingenyPlanning;
            return self();
        }

        private MitigationStrategy mitigationStrategy;
        public T mitigationStrategy(MitigationStrategy mitigationStrategy) {
            this.mitigationStrategy = mitigationStrategy;
            return self();
        }

        protected abstract T self();

        protected abstract Risk build();
    }
}
