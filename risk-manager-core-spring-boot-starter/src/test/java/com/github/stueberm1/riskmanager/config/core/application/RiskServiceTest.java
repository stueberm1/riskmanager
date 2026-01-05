package com.github.stueberm1.riskmanager.config.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.stueberm1.riskmanager.config.core.application.create.CreateRiskAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskFinderAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskReaderAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.list.RisksAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.update.UpdateRiskAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.domain.RiskFactoryAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.domain.RiskPatchFactoryAutoConfiguration;
import com.github.stueberm1.riskmanager.core.in.risk.*;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SpringBootTest
@ContextConfiguration(classes = {RiskFactoryAutoConfiguration.class, RiskPatchFactoryAutoConfiguration.class,
        CreateRiskAutoConfiguration.class, UpdateRiskAutoConfiguration.class, RiskFinderAutoConfiguration.class,
        RiskReaderAutoConfiguration.class, RisksAutoConfiguration.class, RiskServiceAutoConfiguration.class})
class RiskServiceTest {

    @MockitoBean
    private RiskDataAccessService riskDataAccessService;

    @Autowired
    private RiskService riskService;

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final String DESCRIPTION = "A pretty test Risk";
    public static final String DETAILS = "Some more specific details and consequences";
    public static final String CONTINGENCY_PLANNING = "Crying and panic";
    public static final String MITIGATION_STRATEGY =
            "We need to define limits and monitor the metrics, so that we can intervene at time";

    @Captor
    private ArgumentCaptor<RiskDao> riskDaoCaptor;


    @Nested
    class CreateRisks {

        @BeforeEach
        void setUp() {
            doNothing().when(riskDataAccessService).save(any());
        }

        @Test
        void informationOfAValidRiskWillBeSendToThePersistenceImplementation() {
            RiskTO validRisk = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW, DESCRIPTION, DETAILS,
                    CONTINGENCY_PLANNING, MITIGATION_STRATEGY);

            // when
            riskService.createRisk(validRisk);

            RiskDao expectedRiskDao = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .contingencyPlanning(CONTINGENCY_PLANNING)
                    .mitigationStrategy(MITIGATION_STRATEGY)
                    .build();

            then(riskDataAccessService).should(times(1)).save(expectedRiskDao);
        }

        @Test
        void reusingRiskIdentifierForAnotherRiskLeadsIntoException() {
            RiskDao existingRiskDao = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.HIGH)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.VERY_HIGH)
                    .havingDescription("A Risk already exists")
                    .withDetailedInformation(DETAILS)
                    .build();
            given(riskDataAccessService.find(any())).willReturn(Optional.of(existingRiskDao));

            RiskTO validRisk = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW, DESCRIPTION, DETAILS,
                    CONTINGENCY_PLANNING, MITIGATION_STRATEGY);

            assertThatExceptionOfType(RiskIdentifierAlreadyInUseException.class).isThrownBy(() -> riskService.createRisk(validRisk))
                    .extracting(RiskIdentifierAlreadyInUseException::getRiskIdentifier)
                    .isEqualTo(TEST_ID);
        }
    }


    @Nested
    class FindRisk {
        @Test
        void ifThereIsInformationAboutThwRiskIsPersistedTheServiceReturnsTheInformation() {

            RiskDao existingRiskDao = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .contingencyPlanning(CONTINGENCY_PLANNING)
                    .mitigationStrategy(MITIGATION_STRATEGY)
                    .build();
            given(riskDataAccessService.find(any())).willReturn(Optional.of(existingRiskDao));

            //when
            RiskTO expected = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW,DESCRIPTION, DETAILS,
                    CONTINGENCY_PLANNING, MITIGATION_STRATEGY);
            assertThat(riskService.get(TEST_ID)).isNotNull().isInstanceOf(RiskTO.class).isEqualTo(expected);
        }

        @Test
        void ifTheRiskInformationIsNotFoundTheRequestLeadsIntoAnException() {

            given(riskDataAccessService.find(any())).willReturn(Optional.empty());

            //when
            assertThatExceptionOfType(RiskNotFoundException.class).isThrownBy(() -> riskService.get(TEST_ID))
                    .extracting(RiskNotFoundException::getRiskIdentifier)
                    .isEqualTo(TEST_ID);
        }
    }

    @Nested
    class ListAllRisks {

        @Test
        void listAllRisks() {
            given(riskDataAccessService.listAll()).willReturn(persistedRisks().toList());

            assertThat(riskService.listAll())
                    .isNotNull()
                    .isNotEmpty()
                    .size().isEqualTo(10L).returnToIterable()
                    .extracting(RiskTO::id)
                    .extracting(RiskIdentifier::id)
                    .asInstanceOf(list(String.class))
                    .containsExactlyInAnyOrder("1", "2", "3","4", "5", "6", "7", "8", "9", "10");
        }
    }


    static Stream<RiskDao> persistedRisks() {
        AtomicLong counter = new AtomicLong(1);
        return LongStream.generate(counter::getAndIncrement)
                .limit(10L)
                .mapToObj(RiskServiceTest::riskIdentifier)
                .map(RiskServiceTest::createByIdentifier);
    }

    static RiskIdentifier riskIdentifier(long number) {
        return SimpleNumericRiskIdentifier.builder().withCurrentNumber(number).build();
    }

    static RiskDao createByIdentifier(RiskIdentifier riskIdentifier) {
        return SimpleRiskDao.builder()
                .hasId(riskIdentifier)
                .withSeverity(Severity.MEDIUM)
                .probabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM)
                .havingDescription(DESCRIPTION)
                .withDetailedInformation(DETAILS)
                .build();
    }

    @Nested
    class UpdateRisk {

        @Test
        void RiskNotFoundExceptionIsThrownWhenRiskIsNotFound() {
            given(riskDataAccessService.find(any())).willReturn(Optional.empty());

            RiskPatchTO patch = new RiskPatchTO(TEST_ID, Severity.VERY_HIGH,null, null,
                    CONTINGENCY_PLANNING, null);

            // when
            assertThatExceptionOfType(RiskNotFoundException.class).isThrownBy(() -> riskService.updateRisk(patch))
                    .extracting(RiskNotFoundException::getRiskIdentifier)
                    .isNotNull()
                    .isEqualTo(TEST_ID);

            then(riskDataAccessService).should(times(1)).find(TEST_ID);
            then(riskDataAccessService).shouldHaveNoMoreInteractions();

        }

        @Test
        void updateSeverityAndContingencyPlanningOfTheRisk() {

            RiskDao existingRiskDao = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .build();
            given(riskDataAccessService.find(any())).willReturn(Optional.of(existingRiskDao));

            // when
            RiskPatchTO patch = new RiskPatchTO(TEST_ID, Severity.VERY_HIGH,null, null,
                    CONTINGENCY_PLANNING, null);
            riskService.updateRisk(patch);

            RiskDao expectedAfterAction = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.VERY_HIGH)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .contingencyPlanning(CONTINGENCY_PLANNING)
                    .build();
            then(riskDataAccessService).should(times(1)).save(expectedAfterAction);
        }

        @Test
        void serviceReturnsTheUpdatedRisk() {
            RiskDao existingRiskDao = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .build();
            given(riskDataAccessService.find(any())).willReturn(Optional.of(existingRiskDao));

            // when
            RiskPatchTO patch = new RiskPatchTO(TEST_ID, Severity.VERY_HIGH,null, null,
                    CONTINGENCY_PLANNING, null);
            RiskTO risk =riskService.updateRisk(patch);

            RiskTO expected = new RiskTO(TEST_ID, Severity.VERY_HIGH, ProbabilityOfOccurrence.LOW, DESCRIPTION, DETAILS,
                    CONTINGENCY_PLANNING, null);
            assertThat(risk).isNotNull().isEqualTo(expected);
        }
    }
}
