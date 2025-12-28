package com.github.stueberm1.riskmanager.core.test.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.domain.SimpleRiskFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.types.risk.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class RiskFactoryTest {

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();

    private final RiskFactory riskFactory = new SimpleRiskFactory();

    /// The `RiskFromTOTest` contains test-cases and steps for creating the Risk-domain-model from client requests.
    @Nested
    @DisplayName("Create Risks  by client requests using transfer object")
    class RiskFromTOTest {

        @Test
        void simpleRisksGetCreatedByTOValues() {
            RiskTO inputParam = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW, "A pretty test Risk",
                    "Some more specific details and consequences", "Crying and panic",
                    "We need to define limits and monitor the metrics, so that we can intervene at time");
            assertThat(riskFactory.create(inputParam))
                    .isNotNull()
                    .isExactlyInstanceOf(SimpleRisk.class)
                    .extracting(Risk::id, Risk::severity, Risk::probabilityOfOccurrence, Risk::description, Risk::details,
                            Risk::contingencyPlanning, Risk::getMitigationStrategy)
                    .containsExactly(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW,
                            SimpleDescription.ofValue("A pretty test Risk"),
                            SimpleDetails.ofValue("Some more specific details and consequences"),
                            Optional.of(SimpleContingencyPlanningDescription.ofValue("Crying and panic")),
                            Optional.of(
                                    SimpleMitigationStrategyDescription
                                            .ofValue("We need to define limits and monitor the metrics, so that we can intervene at time")));
        }

        @ParameterizedTest
        @MethodSource("com.github.stueberm1.riskmanager.core.test.domain.RiskFactoryTest#shortStringDescriptions")
        void serviceRejectsDescriptionsBeingToShortToBeAGoodDescription(String description) {
            RiskTO inputParam = new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW, description,
                    "Some more specific details and consequences", "Crying and panic",
                    "We need to define limits and monitor the metrics, so that we can intervene at time");
            assertThatExceptionOfType(EntityConstraintViolationException.class)
                    .isThrownBy(() -> riskFactory.create(inputParam))
                    .withMessage(EntityConstraintViolationException.MESSAGE)
                    .hasFieldOrPropertyWithValue("entityType", Risk.class)
                    .extracting(EntityConstraintViolationException::violations)
                    .asInstanceOf(list(EntityConstraintViolationException.EntityConstraintViolation.class))
                    .isNotNull()
                    .size().isEqualTo(1).returnToIterable()
                    .allMatch(violation -> "risk.description".equalsIgnoreCase(violation.path()))
                    .allMatch(violation -> "Description must have at least 10 characters length".equalsIgnoreCase(violation.violation()));
        }
    }

    static Stream<String> shortStringDescriptions() {
        return IntStream.iterate(1, in -> in + 3)
                .limit(4L)
                .filter(index -> index < 10)
                .mapToObj(RiskFactoryTest::stringOfLength);
    }

    static String stringOfLength(int stringLength) {
        return StringUtils.repeat("*", stringLength);
    }

    @Nested
    @DisplayName("Create Risks  from persistence provider using the data access object")
    class RiskFromDaoTest {

        @Test
        void simpleRisksGetCreatedByDaoValues() {
            RiskDao inputParam = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.LOW)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.HIGH)
                    .havingDescription("A pretty test Risk")
                    .withDetailedInformation("Some more specific details and consequences")
                    .contingencyPlanning("Crying and panic")
                    .mitigationStrategy("We need to define limits and monitor the metrics, so that we can intervene at time")
                    .build();

            assertThat(riskFactory.create(inputParam))
                    .isNotNull()
                    .isExactlyInstanceOf(SimpleRisk.class)
                    .extracting(Risk::id, Risk::severity, Risk::probabilityOfOccurrence, Risk::description, Risk::details,
                            Risk::contingencyPlanning, Risk::getMitigationStrategy)
                    .containsExactly(TEST_ID, Severity.LOW, ProbabilityOfOccurrence.HIGH,
                            SimpleDescription.ofValue("A pretty test Risk"),
                            SimpleDetails.ofValue("Some more specific details and consequences"),
                            Optional.of(SimpleContingencyPlanningDescription.ofValue("Crying and panic")),
                            Optional.of(
                                    SimpleMitigationStrategyDescription
                                            .ofValue("We need to define limits and monitor the metrics, so that we can intervene at time")));
        }
    }
}
