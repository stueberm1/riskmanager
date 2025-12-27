package com.github.stueberm1.riskmanager.core.test.application.risk.find;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReaderAdapter;
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

///  Test of the {@link com.github.stueberm1.riskmanager.core.application.risk.find.RiskReaderAdapter}
@ExtendWith(MockitoExtension.class)
public class RiskReaderAdapterTest {

    @Mock
    private RiskDataAccessService riskDataAccessService;
    @Mock
    private RiskFactory riskFactory;

    @InjectMocks
    private RiskReaderAdapter riskReaderAdapter;

    @Test
    @DisplayName("RiskReaderAdapter cannot initialized without riskDataAccessService")
    void riskReaderAdapterCreationFailsWithoutRiskDataAccessService() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new RiskReaderAdapter(null, riskFactory))
                .withMessage("riskDataAccessService");
    }

    @Test
    @DisplayName("RiskReaderAdapter cannot initialized without riskFactory")
    void riskReaderAdapterCreationFailsWithoutRiskFactory() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new RiskReaderAdapter(riskDataAccessService, null))
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
    void givenACompletePersistentRisk_whenCallRead_thenTheRiskGetsFullyRestored() {
        RiskDao mockRiskDao = SimpleRiskDao.builder()
                .hasId(TEST_ID)
                .withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                .withDetailedInformation(DETAILS)
                .havingDescription(DESCRIPTION)
                .mitigationStrategy(MITIGATION_STRATEGY)
                .contingencyPlanning(CONTINGENCY_PLANNING)
                .build();
        given(riskDataAccessService.read(any())).willReturn(Optional.of(mockRiskDao));
        given(riskFactory.create(any())).willReturn()

        //when
        riskReaderAdapter.read(TEST_ID);

    }



}
