package com.github.stueberm1.riskmanager.data.jpa.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;

import com.github.stueberm1.riskmanager.core.model.risk.MitigationStrategy;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.data.jpa.provider.RiskDataAccessAdapterConfiguration;
import com.github.stueberm1.riskmanager.data.jpa.provider.RiskDataConverterConfiguration;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskData;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskRepositoryAutoConfiguration;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@DataJpaTest
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {RiskDataAccessAdapterConfiguration.class, RiskRepositoryAutoConfiguration.class,
        RiskDataConverterConfiguration.class})
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
            assertThat(riskDataRepository.findById(TEST_ID.id())).isPresent()
                    .get()
                    .hasFieldOrPropertyWithValue("riskIdentifier", TEST_ID.id())
                    .hasFieldOrPropertyWithValue("severity", Severity.MEDIUM)
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", ProbabilityOfOccurrence.HIGH)
                    .hasFieldOrPropertyWithValue("description",  DESCRIPTION)
                    .hasFieldOrPropertyWithValue("details",   DETAILS)
                    .hasFieldOrPropertyWithValue("contingencyPlanning", CONTINGENCY_PLANNING)
                    .extracting(RiskData::getMitigationStrategy).isNull();
        }
    }

    @Nested
    class FindRiskData {

        @BeforeEach
        void setUp() {
            riskDataRepository.save(riskData(TEST_ID.id()));
        }
        @Test
        void findRiskDataIfAvailable() {
            assertThat(riskDataAccessService.find(TEST_ID))
                    .isNotNull()
                    .isPresent()
                    .get()
                    .hasFieldOrPropertyWithValue("id", TEST_ID)
                    .hasFieldOrPropertyWithValue("severity", Severity.MEDIUM)
                    .hasFieldOrPropertyWithValue("probabilityOfOccurrence", ProbabilityOfOccurrence.LOW)
                    .hasFieldOrPropertyWithValue("description",  DESCRIPTION)
                    .hasFieldOrPropertyWithValue("details",   DETAILS)
                    .hasFieldOrPropertyWithValue("contingencyPlanning", CONTINGENCY_PLANNING)
                    .extracting(RiskDao::getMitigationStrategy)
                    .asInstanceOf(optional(MitigationStrategy.class))
                    .isNotNull()
                    .isNotPresent();
        }

        @Test
        void serviceReturnsOptionalEmptyIfAbsent() {
            assertThat(riskDataAccessService.find(SimpleNumericRiskIdentifier.builder()
                            .withCurrentNumber(4L)
                    .build()))
                    .isNotPresent();
        }

    }

    static RiskData riskData(String riskIdentifier) {
        RiskData riskData = new RiskData();
        riskData.setRiskIdentifier(riskIdentifier);
        riskData.setSeverity(Severity.MEDIUM);
        riskData.setProbabilityOfOccurrence(ProbabilityOfOccurrence.LOW);
        riskData.setDescription(DESCRIPTION);
        riskData.setDetails(DETAILS);
        riskData.setContingencyPlanning(CONTINGENCY_PLANNING);
        return riskData;
    }

    @Nested
    class DeleteRiskData {

        @BeforeEach
        void setUp() {
            riskDataRepository.save(riskData(TEST_ID.id()));
        }

        @Test
        void deleteRiskData() {
            assertThat(riskDataRepository.findById(TEST_ID.id())).isPresent();

            //when
            riskDataAccessService.delete(TEST_ID);

            //then
            assertThat(riskDataRepository.findById(TEST_ID.id())).isNotPresent();
        }


    }

    @Nested
    class ListAllRiskData {

        @BeforeEach
        void setUp() {
           riskDataRepository.saveAll(riskDataStream().toList());
        }

        @Test
        void listAllRiskData() {

            assertThat(riskDataAccessService.listAll())
                    .isNotNull()
                    .isNotEmpty()
                    .size().isEqualTo(8).returnToIterable()
                    .extracting(RiskDao::id)
                    .asInstanceOf(list(RiskIdentifier.class))
                    .containsExactlyInAnyOrderElementsOf(riskIdentifierStream().toList());
        }
    }

    static Stream<RiskData> riskDataStream() {
        return riskIdNumbers().map(String::valueOf).map(RiskDataAccessServiceTest::riskData);
    }

    static Stream<RiskIdentifier> riskIdentifierStream() {
        return riskIdNumbers().map(RiskDataAccessServiceTest::convert);
    }
    static RiskIdentifier convert(Long number) {
        return SimpleNumericRiskIdentifier.builder()
                .withCurrentNumber(number)
                .build();
    }

    static Stream<Long> riskIdNumbers() {
        return LongStream.range(2, 10).boxed();
    }


}
