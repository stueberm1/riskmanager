package com.github.stueberm1.riskmanager.core.model.risk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.util.Objects.requireNonNull;

public class SimpleMitigationStrategyDescription implements MitigationStrategy {

    private final String strategy;

    private SimpleMitigationStrategyDescription(String strategy) {
        this.strategy = requireNonNull(strategy);
    }

    public static SimpleMitigationStrategyDescription ofValue(String strategy) {
        return new SimpleMitigationStrategyDescription(strategy);
    }

    @Override
    public String strategy() {
        return strategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleMitigationStrategyDescription that = (SimpleMitigationStrategyDescription) o;

        return new EqualsBuilder().append(strategy, that.strategy).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(strategy).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("strategy", strategy)
                .toString();
    }
}
