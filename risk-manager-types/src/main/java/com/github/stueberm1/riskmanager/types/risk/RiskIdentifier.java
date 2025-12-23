package com.github.stueberm1.riskmanager.types.risk;

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
    public String id() {
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

    private RiskIdentifier(Builder<?> builder) {
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
         */
        public abstract RiskIdentifier build();
    }
}
