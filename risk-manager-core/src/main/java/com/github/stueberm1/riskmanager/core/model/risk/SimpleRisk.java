package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;

public class SimpleRisk extends Risk {

    @Override
    protected void validate() throws EntityConstraintViolationException {

    }

    public SimpleRisk(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder extends Risk.Builder<Builder> {

        private Builder() {}
        @Override
        public Builder self() {
            return this;
        }

        @Override
        public Risk build() {
            return new SimpleRisk(this);
        }
    }
}
