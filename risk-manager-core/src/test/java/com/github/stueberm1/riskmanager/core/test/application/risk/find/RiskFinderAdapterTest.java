package com.github.stueberm1.riskmanager.core.test.application.risk.find;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinderAdapter;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
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

import java.util.Optional;

///  Test of the {@link RiskFinderAdapter}
@ExtendWith(MockitoExtension.class)
public class RiskFinderAdapterTest {

    @Mock
    private RiskDataAccessService riskDataAccessService;
    @Mock
    private RiskFactory riskFactory;

    @InjectMocks
    private RiskFinderAdapter riskReaderAdapter;

    @Test
    @DisplayName("RiskReaderAdapter cannot initialized without riskDataAccessService")
    void riskReaderAdapterCreationFailsWithoutRiskDataAccessService() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new RiskFinderAdapter(null, riskFactory))
                .withMessage("riskDataAccessService");
    }

    @Test
    @DisplayName("RiskReaderAdapter cannot initialized without riskFactory")
    void riskReaderAdapterCreationFailsWithoutRiskFactory() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new RiskFinderAdapter(riskDataAccessService, null))
                .withMessage("riskFactory");
    }

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final String DESCRIPTION = "A pretty test Risk";
    public static final String DETAILS = "Some more specific details and consequences";
    public static final String CONTINGENCY_PLANNING = "Crying and panic";
    public static final String MITIGATION_STRATEGY =
           "We need to define limits and monitor the metrics, so that we can intervene at time";


    public static final Risk COMPLETE_EXPECTED_RISK = SimpleRisk.builder()
            .hasId(TEST_ID)
            .withSeverity(Severity.MEDIUM)
            .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
            .havingDescription(SimpleDescription.ofValue(DESCRIPTION))
            .withDetailedInformation(SimpleDetails.ofValue(DETAILS))
            .contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(CONTINGENCY_PLANNING))
            .mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(MITIGATION_STRATEGY))
            .build();

    @Captor
    public ArgumentCaptor<RiskDao> riskDaoCaptor;

    @Test
    @DisplayName("A complete risk can be can be fully restored")
    void givenACompletePersistentRisk_whenCallFind_thenTheRiskGetsFullyRestored() {
        RiskDao mockRiskDao = SimpleRiskDao.builder()
                .hasId(TEST_ID)
                .withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                .withDetailedInformation(DETAILS)
                .havingDescription(DESCRIPTION)
                .mitigationStrategy(MITIGATION_STRATEGY)
                .contingencyPlanning(CONTINGENCY_PLANNING)
                .build();
        given(riskDataAccessService.find(any())).willReturn(Optional.of(mockRiskDao));
        given(riskFactory.create(any(RiskDao.class))).willReturn(convert(mockRiskDao));

        //when
        riskReaderAdapter.find(TEST_ID);

        then(riskFactory).should(times(1)).create(riskDaoCaptor.capture());
        assertThat(riskDaoCaptor.getValue()).isInstanceOf(RiskDao.class).isEqualTo(mockRiskDao);
    }

    private static Risk convert(RiskDao inArgument) {
        SimpleRisk.Builder builder = SimpleRisk.builder()
                .hasId(inArgument.id())
                .withSeverity(inArgument.severity())
                .probabilityOfOccurrence(inArgument.probabilityOfOccurrence())
                .havingDescription(SimpleDescription.ofValue(inArgument.description()))
                .withDetailedInformation(SimpleDetails.ofValue(inArgument.details()));

        inArgument.contingencyPlanning().map(SimpleContingencyPlanningDescription::ofValue).ifPresent(builder::contingencyPlanning);
        inArgument.getMitigationStrategy().map(SimpleMitigationStrategyDescription::ofValue).ifPresent(builder::mitigationStrategy);
        return builder.build();
    }

}
