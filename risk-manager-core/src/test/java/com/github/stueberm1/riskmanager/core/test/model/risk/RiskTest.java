package com.github.stueberm1.riskmanager.core.test.model.risk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;

import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;


class RiskTest {

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final Description DESCRIPTION = SimpleDescription.ofValue("A pretty test Risk");
    public static final Details DETAILS = SimpleDetails.ofValue("Some more specific details and consequences");
    public static final ContingencyPlanning CONTINGENCY_PLANNING = SimpleContingencyPlanningDescription
            .ofValue("Crying and panic");
    public static final MitigationStrategy MITIGATION_STRATEGY =
            SimpleMitigationStrategyDescription
                    .ofValue("We need to define limits and monitor the metrics, so that we can intervene at time");



    @Nested
    class PatchTest {
        private Risk risk;

        @BeforeEach
        void setUp() {
            risk = SimpleRisk.builder()
                    .hasId(TEST_ID)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                    .build();
        }

        @ParameterizedTest
        @EnumSource(value = Severity.class, mode = EnumSource.Mode.EXCLUDE, names = {"MEDIUM"})
        void changeSeverityOfTheRisk(Severity neSeverity) {
            RiskPatch riskPatch = SimplePatch.builder().withSeverity(neSeverity).build();
            assertThat(risk.applyPatch(riskPatch))
                    .isNotEqualTo(risk)
                    .hasFieldOrPropertyWithValue("id", risk.id())
                    .hasFieldOrPropertyWithValue("severity", neSeverity)
                    .hasFieldOrPropertyWithValue("description", risk.description())
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", risk.probabilityOfOccurrence())
                    .hasFieldOrPropertyWithValue("details", risk.details());
        }

        @ParameterizedTest
        @EnumSource(value = ProbabilityOfOccurrence.class, mode = EnumSource.Mode.EXCLUDE, names = {"MEDIUM"})
        void changeProbabilityOfOccurrenceOfTheRisk(ProbabilityOfOccurrence probabilityOfOccurrence) {
            RiskPatch riskPatch = SimplePatch.builder().probabilityOfOccurrence(probabilityOfOccurrence).build();
            assertThat(risk.applyPatch(riskPatch))
                    .isNotEqualTo(risk)
                    .hasFieldOrPropertyWithValue("id", risk.id())
                    .hasFieldOrPropertyWithValue("severity", risk.severity())
                    .hasFieldOrPropertyWithValue("description", risk.description())
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", probabilityOfOccurrence)
                    .hasFieldOrPropertyWithValue("details", risk.details());
        }

        @Test
        void updateDetailsOfTheRisk() {
            SimpleDetails newDetails = SimpleDetails.ofValue("This will become a big problem.");
            RiskPatch riskPatch = SimplePatch.builder().withDetailedInformation(newDetails).build();
            assertThat(risk.applyPatch(riskPatch))
                    .isNotEqualTo(risk)
                    .hasFieldOrPropertyWithValue("id", risk.id())
                    .hasFieldOrPropertyWithValue("severity", risk.severity())
                    .hasFieldOrPropertyWithValue("description", risk.description())
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", risk.probabilityOfOccurrence())
                    .hasFieldOrPropertyWithValue("details", newDetails);
        }

        @Test
        void addConsistencyPlanningToTheRisk() {
            RiskPatch riskPatch = SimplePatch.builder().contingencyPlanning(CONTINGENCY_PLANNING).build();
            assertThat(risk.applyPatch(riskPatch))
                    .isNotEqualTo(risk)
                    .hasFieldOrPropertyWithValue("id", risk.id())
                    .hasFieldOrPropertyWithValue("severity", risk.severity())
                    .hasFieldOrPropertyWithValue("description", risk.description())
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", risk.probabilityOfOccurrence())
                    .hasFieldOrPropertyWithValue("details", risk.details())
                    .hasFieldOrPropertyWithValue("contingencyPlanning", CONTINGENCY_PLANNING)
                    .extracting(Risk::getMitigationStrategy)
                    .asInstanceOf(optional(MitigationStrategy.class))
                    .isNotPresent();
        }

        @Test
        void addMitigationStrategyToTheRisk() {
            RiskPatch riskPatch = SimplePatch.builder().mitigationStrategy(MITIGATION_STRATEGY).build();
            assertThat(risk.applyPatch(riskPatch))
                    .isNotEqualTo(risk)
                    .hasFieldOrPropertyWithValue("id", risk.id())
                    .hasFieldOrPropertyWithValue("severity", risk.severity())
                    .hasFieldOrPropertyWithValue("description", risk.description())
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", risk.probabilityOfOccurrence())
                    .hasFieldOrPropertyWithValue("details", risk.details())
                    .hasFieldOrPropertyWithValue("mitigationStrategy", Optional.of(MITIGATION_STRATEGY))
                    .extracting(Risk::contingencyPlanning)
                    .asInstanceOf(optional(ContingencyPlanning.class))
                    .isNotPresent();
        }
    }

    @Test
    void givenARiskWithConsistencyPlanning_whenPatchWithoutConsistencyPlanning_thenTheExistingConsistencyPlanningIsKept() {
        Risk riskWithConsistencyPlanning = SimpleRisk.builder()
                .hasId(TEST_ID)
                .havingDescription(DESCRIPTION)
                .withDetailedInformation(DETAILS)
                .withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                .contingencyPlanning(CONTINGENCY_PLANNING)
                .build();
        SimpleDetails newDetails = SimpleDetails.ofValue("This will become a big problem.");
        RiskPatch riskPatch = SimplePatch.builder().withDetailedInformation(newDetails).build();
        assertThat(riskWithConsistencyPlanning.applyPatch(riskPatch))
                .isNotEqualTo(riskWithConsistencyPlanning)
                .hasFieldOrPropertyWithValue("id", riskWithConsistencyPlanning.id())
                .hasFieldOrPropertyWithValue("severity", riskWithConsistencyPlanning.severity())
                .hasFieldOrPropertyWithValue("description", riskWithConsistencyPlanning.description())
                .hasFieldOrPropertyWithValue("probabilityOfOccurrence", riskWithConsistencyPlanning.probabilityOfOccurrence())
                .hasFieldOrPropertyWithValue("contingencyPlanning", CONTINGENCY_PLANNING)
                .extracting(Risk::getMitigationStrategy)
                .asInstanceOf(optional(MitigationStrategy.class))
                .isNotPresent();
    }

    @Test
    void givenARiskWithMitigationStrategy_whenPatchWithoutMitigationStrategy_thenTheExistingMitigationStrategyIsKept() {
        Risk riskWithConsistencyPlanning = SimpleRisk.builder()
                .hasId(TEST_ID)
                .havingDescription(DESCRIPTION)
                .withDetailedInformation(DETAILS)
                .withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                .mitigationStrategy(MITIGATION_STRATEGY)
                .build();
        SimpleDetails newDetails = SimpleDetails.ofValue("This will become a big problem.");
        RiskPatch riskPatch = SimplePatch.builder().withDetailedInformation(newDetails).build();
        assertThat(riskWithConsistencyPlanning.applyPatch(riskPatch))
                .isNotEqualTo(riskWithConsistencyPlanning)
                .hasFieldOrPropertyWithValue("id", riskWithConsistencyPlanning.id())
                .hasFieldOrPropertyWithValue("severity", riskWithConsistencyPlanning.severity())
                .hasFieldOrPropertyWithValue("description", riskWithConsistencyPlanning.description())
                .hasFieldOrPropertyWithValue("probabilityOfOccurrence", riskWithConsistencyPlanning.probabilityOfOccurrence())
                .hasFieldOrPropertyWithValue("mitigationStrategy", Optional.of(MITIGATION_STRATEGY))
                .extracting(Risk::contingencyPlanning)
                .asInstanceOf(optional(ContingencyPlanning.class))
                .isNotPresent();
    }
}
