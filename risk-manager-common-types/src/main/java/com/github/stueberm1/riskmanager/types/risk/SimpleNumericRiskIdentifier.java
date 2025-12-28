package com.github.stueberm1.riskmanager.types.risk;

import java.util.Optional;

public class SimpleNumericRiskIdentifier  extends RiskIdentifier {


    @Override
    protected Optional<String> idPrefix() {
        return Optional.empty();
    }



    public static Builder builder() {
        return new Builder();
    }

    private SimpleNumericRiskIdentifier(Builder builder) {
        super(builder);
    }

    public static final class Builder extends RiskIdentifier.Builder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SimpleNumericRiskIdentifier build() {
            return new SimpleNumericRiskIdentifier(this);
        }
    }
}
