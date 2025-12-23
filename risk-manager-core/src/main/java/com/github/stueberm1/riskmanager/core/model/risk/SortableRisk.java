package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;

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

    public static final class Builder extends Risk.Builder<Builder> {
        private long position;

        public Builder position(final long position) {
            this.position = position;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        protected Risk build() {
            return new SortableRisk(this);
        }
    }
}
