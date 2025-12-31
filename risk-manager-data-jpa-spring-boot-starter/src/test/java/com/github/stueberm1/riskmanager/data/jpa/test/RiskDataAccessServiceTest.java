package com.github.stueberm1.riskmanager.data.jpa.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.data.jpa.provider.RiskDataAccessAdapterConfiguration;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskRepositoryAutoConfiguration;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {RiskDataAccessAdapterConfiguration.class, RiskRepositoryAutoConfiguration.class})
class RiskDataAccessServiceTest {

    @Autowired
    private RiskDataRepository riskDataRepository;

    @Autowired
    private RiskDataAccessService riskDataAccessService;

    public static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    public static final String DESCRIPTION = "A pretty test Risk";
    public static final String DETAILS = "Some more specific details and consequences";
    public static final String CONTINGENCY_PLANNING = "Crying and panic";


    @Nested
    class SaveRiskData {



        @Test
        void saveRiskData() {
            // given
            SimpleRiskDao input = SimpleRiskDao.builder()
                    .hasId(TEST_ID)
                    .withSeverity(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.HIGH)
                    .havingDescription(DESCRIPTION)
                    .withDetailedInformation(DETAILS)
                    .contingencyPlanning(CONTINGENCY_PLANNING)
                    .build();

            //when
            riskDataAccessService.save(input);

            //then
            assertThat(riskDataRepository.findByRiskIdentifier(TEST_ID.id())).isPresent();


        }
    }
}
