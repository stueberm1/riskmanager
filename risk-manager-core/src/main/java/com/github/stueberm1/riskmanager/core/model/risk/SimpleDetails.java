package com.github.stueberm1.riskmanager.core.model.risk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.util.Objects.requireNonNull;

public class SimpleDetails implements Details {

    private final String details;

    public SimpleDetails(String details) {
        this.details = requireNonNull(details);
    }

    public static SimpleDetails ofValue(String details) {
        return new SimpleDetails(details);
    }

    @Override
    public String detailContent() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleDetails that = (SimpleDetails) o;

        return new EqualsBuilder().append(details, that.details).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(details).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("details", details)
                .toString();
    }
}
