package com.github.stueberm1.riskmanager.core.test.application.risk.create;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.create.IdValidatingCreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.in.risk.RiskIdentifierAlreadyInUseException;
import com.github.stueberm1.riskmanager.core.model.risk.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateRiskTest {

    @Mock
    private CreateRisk createRiskPersistenceAdapter;
    @Mock
    private RiskFinder riskReadingAdapter;

    @InjectMocks
    private IdValidatingCreateRisk idValidatingCreateRisk;


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

    @Captor
    private ArgumentCaptor<RiskIdentifier> riskIdentifierCaptor;

    @Test
    @DisplayName("Create Risk checks for existing Risk with same Id")
    void checkRiskIdentifierIsUnique() {
        given(riskReadingAdapter.find(any())).willReturn(Optional.empty());

        //when
        idValidatingCreateRisk.save(TEST_RISK);

        then(riskReadingAdapter)
                .should(times(1))
                .find(riskIdentifierCaptor.capture());
        assertThat(riskIdentifierCaptor.getValue()).isNotNull().isEqualTo(TEST_ID);
    }

    @Captor
    private ArgumentCaptor<Risk> riskCaptor;

    @Test
    @DisplayName("The Risk gets saved, if no Risk with same Id exists")
    void aValidRiskUniqueIdentifierGetsSaved() {
        given(riskReadingAdapter.find(any())).willReturn(Optional.empty());

        //when
        idValidatingCreateRisk.save(TEST_RISK);

        then(createRiskPersistenceAdapter).should(times(1)).save(riskCaptor.capture());
        assertThat(riskCaptor.getValue()).isNotNull().isEqualTo(TEST_RISK);
    }

    @Test
    @DisplayName("Calling create risk twice with identical object arguments success without action")
    void existingInstanceWillNotBeOverriddenByIdenticalValues() {
        Risk mockResponse = SimpleRisk.builder().hasId(TEST_RISK.id()).withSeverity(TEST_RISK.severity())
                        .probabilityOfOccurrence(TEST_RISK.probabilityOfOccurrence()).havingDescription(TEST_RISK.description())
                        .withDetailedInformation(TEST_RISK.details()).contingencyPlanning(TEST_RISK.contingencyPlanning().orElse(null))
                        .mitigationStrategy(TEST_RISK.getMitigationStrategy().orElse(null))
                                .build();

        given(riskReadingAdapter.find(any())).willReturn(Optional.of(mockResponse));

        // when
        idValidatingCreateRisk.save(TEST_RISK);

        then(createRiskPersistenceAdapter).should(never()).save(any());
    }

    @Test
    @DisplayName("Calling create risk with an identifier identical to an existing risk lead into exception.")
    void duplicateIdentifierToDifferentObjectsLeadsIntoException() {
        Risk mockResponse = SimpleRisk.builder().hasId(TEST_RISK.id()).withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM).havingDescription(TEST_RISK.description())
                .withDetailedInformation(TEST_RISK.details())
                .build();

        given(riskReadingAdapter.find(any())).willReturn(Optional.of(mockResponse));

        //when
        assertThatExceptionOfType(RiskIdentifierAlreadyInUseException.class)
                .isThrownBy(() ->idValidatingCreateRisk.save(TEST_RISK))
                .withMessage("The RiskIdentifier belongs to another Risk")
                .extracting(RiskIdentifierAlreadyInUseException::getRiskIdentifier)
                .isEqualTo(TEST_ID);

        then(createRiskPersistenceAdapter).should(never()).save(any());

    }
}
