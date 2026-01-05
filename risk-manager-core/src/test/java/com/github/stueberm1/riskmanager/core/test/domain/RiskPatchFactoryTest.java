package com.github.stueberm1.riskmanager.core.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.stueberm1.riskmanager.core.domain.RiskPatchFactory;
import com.github.stueberm1.riskmanager.core.domain.SimpleRiskPatchFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.core.model.risk.SimpleContingencyPlanningDescription;
import com.github.stueberm1.riskmanager.core.model.risk.SimpleDetails;
import com.github.stueberm1.riskmanager.core.model.risk.SimpleMitigationStrategyDescription;
import com.github.stueberm1.riskmanager.core.model.risk.SimplePatch;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.Test;

class RiskPatchFactoryTest {

    private static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();

    private final RiskPatchFactory riskPatchFactory = new SimpleRiskPatchFactory();

    public static final String DETAILS = "Some more specific details and consequences";
    public static final String CONTINGENCY_PLANNING = "Crying and panic";
    public static final String MITIGATION_STRATEGY =
            "We need to define limits and monitor the metrics, so that we can intervene at time";

    @Test
    void createRiskPatchSeverity() {
        RiskPatchTO riskPatchTO = new RiskPatchTO(TEST_ID, Severity.LOW, null, null, null, null);
        assertThat(riskPatchFactory.create(riskPatchTO)).isNotNull().isExactlyInstanceOf(SimplePatch.class)
                .isEqualTo(SimplePatch.builder().withSeverity(Severity.LOW).build());
    }

    @Test
    void createRiskPatchProbabilityOfOccurrence() {
        RiskPatchTO riskPatchTO = new RiskPatchTO(TEST_ID, null, ProbabilityOfOccurrence.HIGH, null, null, null);
        assertThat(riskPatchFactory.create(riskPatchTO)).isNotNull().isExactlyInstanceOf(SimplePatch.class)
                .isEqualTo(SimplePatch.builder().probabilityOfOccurrence(ProbabilityOfOccurrence.HIGH).build());
    }

    @Test
    void createRiskPatchDetails() {
        RiskPatchTO riskPatchTO = new RiskPatchTO(TEST_ID, null, null, DETAILS, null, null);
        assertThat(riskPatchFactory.create(riskPatchTO)).isNotNull().isExactlyInstanceOf(SimplePatch.class)
                .isEqualTo(SimplePatch.builder().withDetailedInformation(SimpleDetails.ofValue(DETAILS)).build());
    }

    @Test
    void createRiskPatchContingencyPlanting() {
        RiskPatchTO riskPatchTO = new RiskPatchTO(TEST_ID, null, null, null, CONTINGENCY_PLANNING, null);
        assertThat(riskPatchFactory.create(riskPatchTO)).isNotNull().isExactlyInstanceOf(SimplePatch.class)
                .isEqualTo(SimplePatch.builder().contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(CONTINGENCY_PLANNING)).build());
    }

    @Test
    void createRiskPatchMitigationStrategy() {
        RiskPatchTO riskPatchTO = new RiskPatchTO(TEST_ID, null, null, null, null, MITIGATION_STRATEGY);
        assertThat(riskPatchFactory.create(riskPatchTO)).isNotNull().isExactlyInstanceOf(SimplePatch.class)
                .isEqualTo(SimplePatch.builder()
                        .mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(MITIGATION_STRATEGY)).build());
    }


}
