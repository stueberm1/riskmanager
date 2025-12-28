package com.github.stueberm1.riskmanager.core.out.persistence;

public class SimpleRiskDao extends RiskDao {

    private SimpleRiskDao(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends RiskDao.Builder<Builder> {

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public SimpleRiskDao build() {
            return new SimpleRiskDao(this);
        }
    }
}
