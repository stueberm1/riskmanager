package com.github.stueberm1.riskmanager.core.test.application.risk.create;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRiskPersistenceAdapter;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/// Test of the {@link CreateRiskPersistenceAdapter}.
@ExtendWith(MockitoExtension.class)
class CreateRiskPersistenceAdapterTest {

    @Mock
    private RiskDataAccessService riskDataAccessService;

    @InjectMocks
    private CreateRiskPersistenceAdapter createRiskPersistenceAdapter;

    @Test
    @DisplayName("CreateRiskPersistenceAdapter requires a RiskDataAccessService to be operational")
    void creatingACreateRiskPersistenceAdapterWithoutRiskDataAccessServiceFailsWithNullPointerException() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CreateRiskPersistenceAdapter(null))
                .withMessage(null);
    }

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final Description DESCRIPTION = SimpleDescription.ofValue("A pretty test Risk");
    public static final Details DETAILS = SimpleDetails.ofValue("Some more specific details and consequences");
    public static final ContingencyPlanning CONTINGENCY_PLANNING = SimpleContingencyPlanningDescription
            .ofValue("Crying and panic");
    public static final MitigationStrategy MITIGATION_STRATEGY =
            SimpleMitigationStrategyDescription
                    .ofValue("We need to define limits and monitor the metrics, so that we can intervene at time");

    @Captor
    private ArgumentCaptor<RiskDao> riskDaoCaptor;

    @Test
    @DisplayName("A complete Riak with contingency planning and mitigation strategy gets converted into the defined dao")
    void adaptACallWithCompleteConfiguredSimpleRisk() {
        final Risk testRisk = SimpleRisk.builder()
                .hasId(TEST_ID)
                .withSeverity(Severity.VERY_HIGH)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                .havingDescription(DESCRIPTION)
                .withDetailedInformation(DETAILS)
                .contingencyPlanning(CONTINGENCY_PLANNING)
                .mitigationStrategy(MITIGATION_STRATEGY)
                .build();
        doNothing().when(riskDataAccessService).save(any());

        //when
        createRiskPersistenceAdapter.save(testRisk);

        then(riskDataAccessService).should(times(1)).save(riskDaoCaptor.capture());
        assertThat(riskDaoCaptor.getValue())
                .isNotNull()
                .isExactlyInstanceOf(SimpleRiskDao.class)
                .isEqualTo(SimpleRiskDao.builder()
                        .hasId(TEST_ID)
                        .withSeverity(Severity.VERY_HIGH)
                        .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                        .havingDescription(DESCRIPTION.value())
                        .withDetailedInformation(DETAILS.detailContent())
                        .contingencyPlanning(CONTINGENCY_PLANNING.plan())
                        .mitigationStrategy(MITIGATION_STRATEGY.strategy())
                        .build()
                );
    }

    @Test
    @DisplayName("A Risk without contingency planning and mitigation strategy gets converted into the defined dao")
    void adaptRiskWithoutContingencyPlanningAndMitigationStrategy() {
        final Risk testRisk = SimpleRisk.builder()
                .hasId(TEST_ID)
                .withSeverity(Severity.VERY_HIGH)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                .havingDescription(DESCRIPTION)
                .withDetailedInformation(DETAILS)
                .build();
        doNothing().when(riskDataAccessService).save(any());

        //when
        createRiskPersistenceAdapter.save(testRisk);

        then(riskDataAccessService).should(times(1)).save(riskDaoCaptor.capture());
        assertThat(riskDaoCaptor.getValue())
                .isNotNull()
                .isExactlyInstanceOf(SimpleRiskDao.class)
                .isEqualTo(SimpleRiskDao.builder()
                        .hasId(TEST_ID)
                        .withSeverity(Severity.VERY_HIGH)
                        .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                        .havingDescription(DESCRIPTION.value())
                        .withDetailedInformation(DETAILS.detailContent())
                        .build()
                );
    }

}
