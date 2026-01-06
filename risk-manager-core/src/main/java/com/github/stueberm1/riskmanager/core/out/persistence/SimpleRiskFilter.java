package com.github.stueberm1.riskmanager.core.out.persistence;

public class SimpleRiskFilter extends RiskFilter {


    private SimpleRiskFilter(Builder builder) {
        super(builder);
    }

    public static FilterSpec findRisksWhere() {
        return new Builder();
    }

    public interface FilterSpec extends RiskFilter.FilterSpec<Builder, SimpleRiskFilter> {}

    private static class Builder extends RiskFilter.Builder<Builder, SimpleRiskFilter> implements FilterSpec {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SimpleRiskFilter create() {
            return new SimpleRiskFilter(this);
        }
    }
}
