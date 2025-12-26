package com.github.stueberm1.riskmanager.core.model.risk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.util.Objects.requireNonNull;

public class SimpleContingencyPlanningDescription implements ContingencyPlanning {

    private final String plan;

    private SimpleContingencyPlanningDescription(String plan) {
        this.plan = requireNonNull(plan);
    }

    public static SimpleContingencyPlanningDescription ofValue(String contingencyPlanning) {
        return new SimpleContingencyPlanningDescription(contingencyPlanning);
    }

    @Override
    public String plan() {
        return plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleContingencyPlanningDescription that = (SimpleContingencyPlanningDescription) o;

        return new EqualsBuilder().append(plan, that.plan).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(plan).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("plan", plan)
                .toString();
    }
}
