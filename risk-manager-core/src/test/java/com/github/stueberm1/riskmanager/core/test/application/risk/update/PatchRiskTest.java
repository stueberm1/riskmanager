package com.github.stueberm1.riskmanager.core.test.application.risk.update;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.application.risk.update.DefaultPatchRisk;
import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatchRiskTest {

    @Mock
    private CreateRisk createRiskPersistenceAdapter;
    @Mock
    private RiskFinder riskReadingAdapter;

    @InjectMocks
    private DefaultPatchRisk patchRisk;

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
            .build();

    @BeforeEach
    void setUp() {
        given(riskReadingAdapter.find(any())).willReturn(Optional.of(TEST_RISK));
    }

    @Test
    void notFindingTheRiskWillThrowException() {
        reset(riskReadingAdapter);
        given(riskReadingAdapter.find(any())).willReturn(Optional.empty());

        assertThatExceptionOfType(RiskNotFoundException.class)
                .isThrownBy(() -> patchRisk.patchRiskIdentifiedBy(TEST_ID))
                .withMessage("risk to patch is not available")
                .extracting(RiskNotFoundException::getRiskIdentifier)
                .isEqualTo(TEST_ID);

        then(createRiskPersistenceAdapter).shouldHaveNoInteractions();
    }

    @Captor
    private ArgumentCaptor<Risk> riskArgumentCaptor;

    @Test
    void addContingencyPlanning() {


        RiskPatch patch = SimplePatch.builder().contingencyPlanning(CONTINGENCY_PLANNING).build();
        Risk patched = patchRisk.patchRiskIdentifiedBy(TEST_ID).with(patch);


        assertThat(patched).isNotNull()
                .isNotEqualTo(TEST_RISK)
                .hasFieldOrPropertyWithValue("id", TEST_ID)
                .hasFieldOrPropertyWithValue("description", DESCRIPTION)
                .extracting(Risk::contingencyPlanning)
                .asInstanceOf(optional(ContingencyPlanning.class))
                .isPresent()
                .get()
                .isEqualTo(CONTINGENCY_PLANNING);
        then(createRiskPersistenceAdapter).should(times(1)).save(riskArgumentCaptor.capture());
        assertThat(riskArgumentCaptor.getValue())
                .isNotNull()
                .isEqualTo(patched);
    }

    @Test
    void addMitigationStrategy() {
            RiskPatch patch = SimplePatch.builder().mitigationStrategy(MITIGATION_STRATEGY).build();
            Risk patched = patchRisk.patchRiskIdentifiedBy(TEST_ID).with(patch);

            assertThat(patched).isNotNull()
                    .isNotEqualTo(TEST_RISK)
                    .hasFieldOrPropertyWithValue("id", TEST_ID)
                    .hasFieldOrPropertyWithValue("description", DESCRIPTION)
                    .extracting(Risk::getMitigationStrategy)
                    .asInstanceOf(optional(MitigationStrategy.class))
                    .isPresent()
                    .get()
                    .isEqualTo(MITIGATION_STRATEGY);
            then(createRiskPersistenceAdapter).should(times(1)).save(riskArgumentCaptor.capture());
            assertThat(riskArgumentCaptor.getValue())
                    .isNotNull()
                    .isEqualTo(patched);
    }
}

