package com.github.stueberm1.riskmanager.data.jpa.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskFilter;
import com.github.stueberm1.riskmanager.data.jpa.provider.RiskDataConverterConfiguration;
import com.github.stueberm1.riskmanager.data.jpa.provider.RiskReportingServiceConfiguration;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskRepositoryAutoConfiguration;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

@DataJpaTest
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {RiskReportingServiceConfiguration.class, RiskRepositoryAutoConfiguration.class,
        RiskDataConverterConfiguration.class})
@Sql("/scripts/test-risks.sql") // import some test risks
class RiskReportingServiceTest {

    @Autowired
    private RiskReportingService riskReportingService;

    @ParameterizedTest
    @MethodSource("com.github.stueberm1.riskmanager.data.jpa.test.RiskReportingServiceTest#searchForSeverity")
    void searchForSeverity(Severity severity, List<RiskIdentifier> expectedRiskIdentifier) {
        RiskFilter filter = SimpleRiskFilter.findRisksWhere().severity().isEqualTo(severity).create();
        assertThat(riskReportingService.listRisksFiltered(filter))
                .isNotNull()
                .size().isEqualTo(expectedRiskIdentifier.size()).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf(expectedRiskIdentifier);
    }


    static Stream<Arguments> searchForSeverity() {
        return Stream.of(
           Arguments.of(Severity.HIGH,
                   List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build(),
                           SimpleNumericRiskIdentifier.builder().withCurrentNumber(4L).build())),
           Arguments.of(Severity.VERY_HIGH,
                   List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(2L).build()))
        );
    }

    @Test
    void probabilityOfOccurrenceIsMediumAndContingencyPlanningIsEmpty() {
        RiskFilter filter = SimpleRiskFilter.findRisksWhere()
                .probabilityOfOccurrence().isEqualTo(ProbabilityOfOccurrence.MEDIUM)
                .and().contingencyPlanning().isEmpty()
                .create();
        assertThat(riskReportingService.listRisksFiltered(filter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf(List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build()));
    }

    @Test
    void detailsContainingTheWordDeveloper() {
        RiskFilter filter = SimpleRiskFilter.findRisksWhere().details().contains("developer").create();
        assertThat(riskReportingService.listRisksFiltered(filter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf(List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(3L).build()));
    }

    @Test
    void descriptionContainingTheWordFrontend() {
        RiskFilter filter = SimpleRiskFilter.findRisksWhere().description().contains("Frontend").create();
        assertThat(riskReportingService.listRisksFiltered(filter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf(List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(3L).build()));
    }

    @Test
    void contingencyPlanningContainsTheWordCrying() {
        RiskFilter filter = SimpleRiskFilter.findRisksWhere().contingencyPlanning().contains("crying").create();
        assertThat(riskReportingService.listRisksFiltered(filter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf(List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(4L).build()));
    }

    @Test
    void mitigationStrategyIsEmpty() {
        RiskFilter riskFilter = SimpleRiskFilter.findRisksWhere().mitigationStrategy().isEmpty().create();
        assertThat(riskReportingService.listRisksFiltered(riskFilter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf( List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build(),
                        SimpleNumericRiskIdentifier.builder().withCurrentNumber(2L).build()));
    }

    @Test
    void mitigationStrategyContainsProgrammingInterface() {
        RiskFilter riskFilter = SimpleRiskFilter.findRisksWhere().mitigationStrategy().contains("programming interface").create();
        assertThat(riskReportingService.listRisksFiltered(riskFilter))
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1).returnToIterable()
                .extracting(RiskDao::id)
                .asInstanceOf(list(RiskIdentifier.class))
                .containsExactlyInAnyOrderElementsOf( List.of(SimpleNumericRiskIdentifier.builder().withCurrentNumber(3L).build()));
    }

    @Test
    void mitigationStrategyIsEmptyAndContainsTerm() {
        RiskFilter riskFilter = SimpleRiskFilter.findRisksWhere().mitigationStrategy().isEmpty().and().mitigationStrategy().contains("programming interface").create();
        assertThat(riskReportingService.listRisksFiltered(riskFilter))
                .isNotNull()
                .isEmpty();
    }
}
