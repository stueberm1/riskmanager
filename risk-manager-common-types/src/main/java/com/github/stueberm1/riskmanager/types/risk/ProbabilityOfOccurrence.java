package com.github.stueberm1.riskmanager.types.risk;

/**
 * The {@code ProbabilityOfOccurrence#} represents the probability the risk becomes a problem
 * in levels from low to very high.
 *
 * {@code ENTERED} is a special case, which means the risk already becomes a problem.
 */
public enum ProbabilityOfOccurrence {

    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH,
    ENTERED;
}
