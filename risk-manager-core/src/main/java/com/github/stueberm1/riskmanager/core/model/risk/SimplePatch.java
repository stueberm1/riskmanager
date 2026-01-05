package com.github.stueberm1.riskmanager.core.model.risk;

public class SimplePatch extends RiskPatch {

    public static Builder builder() {
        return new Builder();
    }

    private SimplePatch(Builder builder) {
        super(builder);
    }

    public static class Builder extends RiskPatch.Builder<Builder> {

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public RiskPatch build() {
            return new SimplePatch(this);
        }
    }

}
