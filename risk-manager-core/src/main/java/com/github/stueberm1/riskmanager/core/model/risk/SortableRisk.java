package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SortableRisk extends Risk implements Comparable<SortableRisk> {

    private final long position;


    private SortableRisk(Builder builder) {
        super(builder);
        this.position = builder.position;
    }

    @Override
    protected void validate() throws EntityConstraintViolationException {

    }

    public long position() {
        return position;
    }

    @Override
    public int compareTo(SortableRisk o) {
        return Long.compare(position, o.position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SortableRisk that = (SortableRisk) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(position, that.position).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(position).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("position", position)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Risk.Builder<Builder> {
        private long position;

        public Builder position(final long position) {
            this.position = position;
            return self();
        }

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public Risk build() {
            return new SortableRisk(this);
        }
    }
}
