package com.github.stueberm1.riskmanager.types.risk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Optional;

/**
 *  = RiskIdentifier
 *
 *  The {@code RiskIdentifier} identifies a risk or technical dept uniquely. The identifier has a incrementing number and
 *  might be combined with a context.
 *
 * @since 0.0.1
 */
public abstract class RiskIdentifier implements Comparable<RiskIdentifier> {


    private final long currentNumber;

    /**
     *
     * @return
     */
    public final String id() {
        return idPrefix()
                .map(prefix -> prefix + '-')
                .map(prefix -> prefix + currentNumber)
                .orElse(String.valueOf(currentNumber));
    }

    /**
     *
     * @return
     */
    protected abstract Optional<String> idPrefix();

    protected RiskIdentifier(Builder<?> builder) {
        this.currentNumber = requiresGreaterZero(builder.currentNumber);
    }

    private static long requiresGreaterZero(long currentNumber) {
        if (currentNumber < 1) {
            throw new IllegalIdNumberException(currentNumber);
        }
        return currentNumber;
    }

    @Override
    public int compareTo(RiskIdentifier o) {
        return Long.compare(currentNumber, o.currentNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RiskIdentifier that = (RiskIdentifier) o;

        return new EqualsBuilder().append(currentNumber, that.currentNumber).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(currentNumber).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("currentNumber", currentNumber)
                .toString();
    }

    /**
     *
     * @param <T>
     */
    protected static abstract class Builder<T extends Builder<T>> {
        private long currentNumber;

        /**
         *
         * @param currentNumber
         * @return
         */
        public T withCurrentNumber(long currentNumber) {
            this.currentNumber = currentNumber;
            return self();
        }

        /**
         *
         * @return
         */
        protected abstract T self();

        /**
         *
         * @return
         * @throws com.github.stueberm1.riskmanager.types.risk.IllegalRiskIdentifierException
         *      If the `RiskIdentifier` does not meet the requirements.
         */
        public abstract RiskIdentifier build();
    }
}
