package com.github.stueberm1.riskmanager.core.test.application.risk.find;

import com.github.stueberm1.riskmanager.core.application.risk.find.ReadRiskDelegator;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RiskReaderTest {

    @Mock
    private RiskFinder riskFinder;

    @InjectMocks
    private ReadRiskDelegator readRiskDelegator;

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final Description DESCRIPTION = SimpleDescription.ofValue("A pretty test Risk");
    public static final Details DETAILS = SimpleDetails.ofValue("Some more specific details and consequences");
    public static final ContingencyPlanning CONTINGENCY_PLANNING = SimpleContingencyPlanningDescription
            .ofValue("Crying and panic");
    public static final MitigationStrategy MITIGATION_STRATEGY =
            SimpleMitigationStrategyDescription
                    .ofValue("We need to define limits and monitor the metrics, so that we can intervene at time");
    public static final Risk TEST_RISK = SimpleRisk.builder()
            .hasId(TEST_ID)
            .withSeverity(Severity.VERY_HIGH)
            .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
            .havingDescription(DESCRIPTION)
            .withDetailedInformation(DETAILS)
            .contingencyPlanning(CONTINGENCY_PLANNING)
            .mitigationStrategy(MITIGATION_STRATEGY)
            .build();

    @Test
    @DisplayName("If the Risk is available, the service returns the risk.")
    void getRiskAsAvailable() {
        given(riskFinder.find(TEST_ID)).willReturn(Optional.of(TEST_RISK));
        assertThat(readRiskDelegator.read(TEST_ID)).isEqualTo(TEST_RISK);
    }

    @Test
    @DisplayName("If the Risk is not available, a qualified exception is thrown")
    void throwExceptionIfTheRiskIsNotAvailable() {
        given(riskFinder.find(any())).willReturn(Optional.empty());
        assertThatExceptionOfType(RiskNotFoundException.class)
                .isThrownBy(() -> readRiskDelegator.read(TEST_ID))
                .withMessage("Not found")
                .extracting(RiskNotFoundException::getRiskIdentifier)
                .isEqualTo(TEST_ID);
    }
}
