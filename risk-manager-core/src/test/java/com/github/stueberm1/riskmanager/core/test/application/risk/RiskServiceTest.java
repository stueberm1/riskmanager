package com.github.stueberm1.riskmanager.core.test.application.risk;

import com.github.stueberm1.riskmanager.core.application.risk.ModelAdaptingRiskServiceFacade;
import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {

    @Mock
    private RiskFactory riskFactory;

    @Mock
    private CreateRisk createRisk;

    @Mock
    private RiskReader riskReader;

    @Mock
    private Risks risks;

    private ModelAdaptingRiskServiceFacade modelAdaptingRiskServiceFacade;

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final String DESCRIPTION = "A pretty test Risk";
    public static final String DETAILS = "Some more specific details and consequences";
    public static final String CONTINGENCY_PLANNING = "Crying and panic";
    public static final String MITIGATION_STRATEGY =
            "We need to define limits and monitor the metrics, so that we can intervene at time";

    @BeforeEach
    public void setUp() {
        modelAdaptingRiskServiceFacade = ModelAdaptingRiskServiceFacade.builder()
                .riskFactory(riskFactory)
                .createRisk(createRisk)
                .riskReader(riskReader)
                .risks(risks)
                .build();
    }

    @Captor
    private ArgumentCaptor<Risk> riskCaptor;

    @Nested
    class CreateRiskTest {

        @BeforeEach
        void setUp() {
            // On invocation on Mock convert argument to risk and return result
            given(riskFactory.create(any(RiskTO.class))).willAnswer(invocationOnMock ->  {
                RiskTO riskTO = invocationOnMock.getArgument(0);
                return convert(riskTO);
            });

            doNothing().when(createRisk).save(any());
        }

        @Test
        void testCreateRisk() {
            RiskTO inArgument = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.MEDIUM, DESCRIPTION, DETAILS,
                    CONTINGENCY_PLANNING, MITIGATION_STRATEGY);

            // when
            modelAdaptingRiskServiceFacade.createRisk(inArgument);

            then(createRisk).should(times(1)).save(riskCaptor.capture());
            assertThat(riskCaptor.getValue())
                    .isNotNull()
                    .isExactlyInstanceOf(SimpleRisk.class)
                    .extracting(Risk::id)
                    .isEqualTo(TEST_ID);
        }
    }

    private static Risk convert(RiskTO inArgument) {
        return SimpleRisk.builder()
                .hasId(inArgument.id())
                .withSeverity(inArgument.severity())
                .probabilityOfOccurrence(inArgument.probabilityOfOccurrence())
                .havingDescription(SimpleDescription.ofValue(inArgument.description()))
                .withDetailedInformation(SimpleDetails.ofValue(inArgument.details()))
                .contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(inArgument.contingencyPlanning()))
                .mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(inArgument.mitigationStrategy()))
                .build();

    }



    public static final Risk TEST_RISK = SimpleRisk.builder()
            .hasId(TEST_ID)
            .withSeverity(Severity.VERY_HIGH)
            .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
            .havingDescription(SimpleDescription.ofValue(DESCRIPTION))
            .withDetailedInformation(SimpleDetails.ofValue(DETAILS))
            .contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(CONTINGENCY_PLANNING))
            .mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(MITIGATION_STRATEGY))
            .build();

    @Nested
    class ReadRisk {

        @Test
        void testReadExistingRisk() {
            given(riskReader.read(TEST_ID)).willReturn(TEST_RISK);
            assertThat(modelAdaptingRiskServiceFacade.get(TEST_ID)).isEqualTo(convert(TEST_RISK));
        }

    }

    static RiskTO convert(final Risk risk) {
        return new RiskTO(risk.id(), risk.severity(), risk.probabilityOfOccurrence(), risk.description().value(),
                risk.details().detailContent(), risk.contingencyPlanning().map(ContingencyPlanning::plan).orElse(null),
                risk.getMitigationStrategy().map(MitigationStrategy::strategy).orElse(null));
    }
}
