package com.github.stueberm1.riskmanager.core.model.risk;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import java.util.Optional;

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
        private Description description;
        private Details details;
        private ContingenyPlanning contingencyPlanning;
        private MitigationStrategy mitigationStrategy;

        protected abstract T self();

        protected abstract Risk build();
    }
}
