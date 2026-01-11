package com.github.stueberm1.riskmanager.core.test.application.risk.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.stueberm1.riskmanager.core.application.risk.DefaultRiskConverter;
import com.github.stueberm1.riskmanager.core.application.risk.list.RiskListerAdapter;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.application.risk.list.RisksDelegator;
import com.github.stueberm1.riskmanager.core.domain.SimpleRiskFactory;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RisksTest {

    @Mock
    private RiskDataAccessService riskDataAccessService;

    @Mock
    private RiskReportingService riskReportingService;

    private Risks risks;

    @BeforeEach
    void setUp() {
        risks = new RisksDelegator(
                new RiskListerAdapter(new SimpleRiskFactory(), riskDataAccessService, riskReportingService),
                new DefaultRiskConverter());
    }

    @Captor
    private ArgumentCaptor<RiskFilter> riskFilterCaptor;

    @Nested
    class ListFiltered {

        @BeforeEach
        void setUp() {
            // Most important is the translation of the inbound port arguments into the outbound port argument
            given(riskReportingService.listRisksFiltered(any())).willReturn(Collections.emptyList());
        }

        @Test
        void testWithFiltersOnAllProperties() {
            // when
            risks.listFilteredWith().severity().isEqualTo(Severity.MEDIUM)
                    .andProbabilityOfOccurrence().isEqualTo(ProbabilityOfOccurrence.MEDIUM)
                    .andDescription().contains("Medium Risk")
                    .andDetails().contains("Framework")
                    .andContingencyPlanning().isEmpty()
                    .andMitigationStrategy().isEmpty()
                    .toList();

            then(riskReportingService).should(times(1)).listRisksFiltered(riskFilterCaptor.capture());
            assertThat(riskFilterCaptor.getValue())
                    .isNotNull()
                    .returns(Severity.MEDIUM, RiskFilter::severityIsEqualTo)
                    .returns(ProbabilityOfOccurrence.MEDIUM, RiskFilter::probabilityOfOccurrenceIsEqualTo)
                    .returns("Medium Risk", RiskFilter::descriptionContains)
                    .returns("Framework", RiskFilter::detailsContains)
                    .returns(Boolean.TRUE, RiskFilter::contingencyPlanningIsEmpty)
                    .returns(Boolean.TRUE, RiskFilter::mitigationStrategyIsEmpty);

        }
    }
}
