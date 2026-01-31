package com.github.stueberm1.riskmanager.http.test.service;

import com.github.stueberm1.riskmanager.core.in.risk.*;
import com.github.stueberm1.riskmanager.core.in.risk.filter.FilterSpec;
import com.github.stueberm1.riskmanager.http.model.JsonPatch;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.RiskJson;
import com.github.stueberm1.riskmanager.http.patch.*;
import com.github.stueberm1.riskmanager.http.service.RiskController;
import com.github.stueberm1.riskmanager.http.service.RiskControllerExceptionHandler;
import com.github.stueberm1.riskmanager.http.service.RiskManagerHttpConfiguration;
import com.github.stueberm1.riskmanager.http.test.mock.MockRiskFilter;
import com.github.stueberm1.riskmanager.types.risk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@WebMvcTest(controllers = {RiskController.class, RiskControllerExceptionHandler.class})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebAppConfiguration
@ContextConfiguration(classes = {RiskManagerHttpConfiguration.class, RiskController.class, RiskControllerExceptionHandler.class})
class RestControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private RiskService riskService;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final RiskIdentifier TEST_ID = SimpleNumericRiskIdentifier.builder().withCurrentNumber(1L).build();
    private static final String DESCRIPTION = "A pretty test Risk";
    private static final String DETAILS = "Some more specific details and consequences";
    private static final String CONTINGENCY_PLANNING = "Crying and panic";
    private static final String MITIGATION_STRATEGY = "Monitor the system and react, when necessary";


    private static final String API_BASE = "/api/v1/risk";


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Nested
    class CreateRiskByPost {


        @Test
        void aValidRiskGetsProcessedByCore() throws Exception {
            doNothing().when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", TEST_ID.id(), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(post(API_BASE).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(TEST_ID.id()))
                    .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("post-risk"));

            then(riskService).should(times(1)).createRisk(new RiskTO(SimpleNumericRiskIdentifier.builder()
                    .withCurrentNumber(1)
                    .build(), Severity.MEDIUM, ProbabilityOfOccurrence.MEDIUM, DESCRIPTION, DETAILS, null, null)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0} )
        void aNegativeIdNumberLeadsIntoBadRequest(int idNumber) throws Exception {
            doNothing().when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", String.valueOf(idNumber), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(post(API_BASE).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("invalid-id-number")))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.title").value("Id number of an RiskIdentifier must be a positive integer"))
                    .andExpect(jsonPath("$.detail").value("ID must be greater than zero, but was " + idNumber))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/" + idNumber))
                    .andDo(document("post-request-invalid-id"));
        }

        @Test
        void requestConstraintViolations() throws Exception {
            doNothing().when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", "1", "description", "too short", "details", DETAILS,
                     "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(post(API_BASE).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isUnprocessableContent())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("invalid-risk-arguments")))
                    .andExpect(jsonPath("$.status").value("422"))
                    .andDo(document("post-request-constraint-violation"));
        }

        @Test
        void aDuplicatedIdLeadsIntoAConflictResponse() throws Exception {
            doThrow(new RiskIdentifierAlreadyInUseException("Duplicated Id", TEST_ID)).when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", TEST_ID.id(), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(post(API_BASE).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("duplicate-identifier")))
                    .andExpect(jsonPath("$.status").value("409"))
                    .andExpect(jsonPath("$.title").value("duplicated identifier"))
                    .andExpect(jsonPath("$.detail")
                            .value("The identifier is already in use by another risk and cannot used here."))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("conflict-post-risk-request"));
        }
    }

    private static final String PROBLEMS_BASE = "http://localhost:8080/docs/problems/";
    private static String resolveToProblemUriString(String problemIdentifier) {
        return  UriComponentsBuilder.fromUriString(PROBLEMS_BASE).path(problemIdentifier).toUriString();
    }

    @Nested
    class GetRiskList {

        @Test
        void getRiskListWithoutFilter() throws Exception {
            given(riskService.listAll()).willReturn(knownRisks().toList());

            //when
            mockMvc.perform(get(API_BASE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$._embedded.riskJsonList.length()").value(knownRisks().toList().size()))
                    .andDo(document("get-list"));
            then(riskService).should(times(1)).listAll();
            then(riskService).shouldHaveNoMoreInteractions();
        }

        @Test
        void startingAListRequestForSeverityAndProbabilityOfOccurrenceAndMitigationPlanningWillRunAFilterSpecRequest() throws Exception {
            FilterSpec mockFilterSpec = MockRiskFilter.newInstance();

            given(riskService.listFilteredWith()).willReturn(mockFilterSpec);

            // when
            mockMvc.perform(get(API_BASE)
                    .param("severity", "MEDIUM")
                    .param("probabilityOfOccurrence", "LOW")
                    .param("contingencyPlanning", "isEmpty()")
                    .param("mitigationStrategy", "praying")
            ).andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(document("get-list-with-filter"));

            then(riskService).should(times(1)).listFilteredWith();
            then(riskService).shouldHaveNoMoreInteractions();

            MockRiskFilter expectedRequest = MockRiskFilter.builder()
                    .severityIsEqualTo(Severity.MEDIUM)
                    .probabilityOfOccurrence(ProbabilityOfOccurrence.LOW)
                    .contingencyPlanningIsEmpty(true)
                    .mitigationStrategyContains("praying")
                    .build();
            assertThat(mockFilterSpec).isEqualTo(expectedRequest);

        }

    }

    static Stream<RiskTO> knownRisks() {
        return longStream().map(RestControllerTest::getRisk);
    }

    static RiskTO getRisk(Long id) {
        RiskIdentifier riskIdentifier = SimpleNumericRiskIdentifier.builder().withCurrentNumber(id).build();
        return new RiskTO(riskIdentifier, Severity.MEDIUM, ProbabilityOfOccurrence.MEDIUM, DESCRIPTION, DETAILS,
                CONTINGENCY_PLANNING, MITIGATION_STRATEGY);
    }

    static Stream<Long> longStream() {
        return LongStream.rangeClosed(1, 10).boxed();
    }

    private static final String RESOURCE_URI = API_BASE + "/{riskIdentifier}";

    @Nested
    class PutRisk {
        @Test
        void aValidRiskGetsProcessedByCore() throws Exception {
            doNothing().when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", TEST_ID.id(), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(put(RESOURCE_URI, TEST_ID.id()).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(TEST_ID.id()))
                    .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("put-risk", pathParameters(parameterWithName("riskIdentifier").description("Unique identifier of a risk"))));

            then(riskService).should(times(1)).createRisk(new RiskTO(SimpleNumericRiskIdentifier.builder()
                    .withCurrentNumber(1)
                    .build(), Severity.MEDIUM, ProbabilityOfOccurrence.MEDIUM, DESCRIPTION, DETAILS, null, null)
            );
        }

        @Test
        void differencesBetweenPathAndObjectIdRunIntoAnException() throws Exception {
            doNothing().when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", TEST_ID.id(), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(put(RESOURCE_URI, 2L).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isUnprocessableContent())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("identifier-mismatch")))
                    .andExpect(jsonPath("$.status").value("422"))
                    .andExpect(jsonPath("$.title").value("Object id do not match the request path"))
                    .andExpect(jsonPath("$.detail")
                            .value("Object path requires payload with id 2, but was 1"))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/2"))
                    .andDo(document("conflict-risk-identifier"));;

            then(riskService).should(never()).createRisk(any());
        }

        @Test
        void serviceValidationErrorsProducesUnprocessableContentDetails() throws Exception {
            doNothing().when(riskService).createRisk(any());
            RiskJson riskJson = new RiskJson();
            riskJson.setId(1L);
            riskJson.setDescription("four");
            riskJson.setDetails(DETAILS);
            riskJson.setProbabilityOfOccurrence(ProbabilityOfOccurrence.MEDIUM);

            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(riskJson);
            mockMvc.perform(put(RESOURCE_URI, TEST_ID.id()).contentType(MediaType.APPLICATION_JSON).content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isUnprocessableContent())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("invalid-risk-arguments")))
                    .andExpect(jsonPath("$.status").value("422"))
                    .andDo(document("put-risk-invalid-risk-arguments"));
        }

        @Test
        void internalValidationErrorProducesUnprocessableContentDetails() throws Exception {
            doThrow(new EntityConstraintViolationException(RiskTO.class, List.of(
                    new EntityConstraintViolationException.EntityConstraintViolation("#/description", "description too long"))))
                    .when(riskService).createRisk(any());

            Map<String, Object> map = Map.of("id", TEST_ID.id(), "description", DESCRIPTION, "details", DETAILS,
                    "severity", Severity.MEDIUM, "probabilityOfOccurrence", ProbabilityOfOccurrence.MEDIUM);
            // when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(map);
            mockMvc.perform(put(RESOURCE_URI, TEST_ID.id()).contentType(MediaType.APPLICATION_JSON).content(requestAsString))
                    .andExpect(status().isUnprocessableContent())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("invalid-risk-arguments")))
                    .andExpect(jsonPath("$.status").value("422"))
                    .andExpect(jsonPath("$.title").value("Some arguments of the risk are invalid."))
                    .andExpect(jsonPath("$.errors.length()").value(1))
                    .andExpect(jsonPath("$.errors[0].detail").value("description too long"))
                    .andExpect(jsonPath("$.errors[0].pointer").value("#/description"))
                    .andDo(document("put-constraint-violation-problem"));
        }
    }


    @Nested
    class GetRisk {

        @Test
        void provideTheJsonRepresentationIfTheRiskExists() throws Exception {

            given(riskService.get(any())).willReturn(getRisk(1L));

            //when
            mockMvc.perform(get(RESOURCE_URI, 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(TEST_ID.id()))
                    .andExpect(jsonPath("$.description").value(DESCRIPTION))
                    .andExpect(jsonPath("$.severity").value(Severity.MEDIUM.name()))
                    .andExpect(jsonPath("$.probabilityOfOccurrence").value(ProbabilityOfOccurrence.MEDIUM.name()))
                    .andExpect(jsonPath("$.details").value(DETAILS))
                    .andExpect(jsonPath("$.contingencyPlanning").value(CONTINGENCY_PLANNING))
                    .andExpect(jsonPath("$.mitigationStrategy").value(MITIGATION_STRATEGY))
                    .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("get-risk", links(
                        linkWithRel("self").optional().ignored()
                    ),pathParameters(parameterWithName("riskIdentifier").description("Unique identifier of a risk"))));
        }

        @Test
        void provideNotFoundDetailsIfTheRiskDoesNotExist() throws Exception {
            given(riskService.get(any())).willThrow(new RiskNotFoundException("not found", TEST_ID));
            //when
            mockMvc.perform(get(RESOURCE_URI, 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("risk-not-found")))
                    .andExpect(jsonPath("$.status").value("404"))
                    .andExpect(jsonPath("$.title").value("The requested risk was not found."))
                    .andExpect(jsonPath("$.detail").value("The identifier 1 does not exist."))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("get-risk-failed"));
        }

    }

    @Nested
    class PatchRisk {

        @Test
        void addConsistencyPlanningToRisk() throws Exception {
            given(riskService.updateRisk(any())).willReturn(new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW,
                    DESCRIPTION, DETAILS, CONTINGENCY_PLANNING, null));

            JsonPatch jsonPatch = new JsonPatch(new JsonPatchOperation[]{new AddOperation(new JsonPointer("/contingencyPlanning"), CONTINGENCY_PLANNING)});
            //when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(jsonPatch);
            mockMvc.perform(patch(RESOURCE_URI, 1L).contentType("application/json-patch+json")
                            .content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(document("patch-risk-add-value"));

            then(riskService).should(times(1)).updateRisk(new RiskPatchTO(TEST_ID, null,
                    null, null, CONTINGENCY_PLANNING, null));
        }

        @Test
        void replaceDetailsWithUpdate() throws Exception {
            final String replacedDetails = "Very evil risk";
            given(riskService.updateRisk(any())).willReturn(new RiskTO(TEST_ID, Severity.MEDIUM, ProbabilityOfOccurrence.LOW,
                    DESCRIPTION, replacedDetails, CONTINGENCY_PLANNING, null));
            JsonPatch jsonPatch = new JsonPatch(new JsonPatchOperation[]{new ReplaceOperation(new JsonPointer("/details"),
                    replacedDetails)});

            //when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(jsonPatch);
            mockMvc.perform(patch(RESOURCE_URI, 1L).contentType("application/json-patch+json")
                            .content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(document("patch-risk-replace-value"));
            then(riskService).should(times(1)).updateRisk(new RiskPatchTO(TEST_ID, null,
                    null, replacedDetails, null, null));
        }

        @Test
        void returnABadRequestWithProblemDetailsIfPatchingANonExistingAttribute() throws Exception {
            JsonPatch jsonPatch = new JsonPatch(new JsonPatchOperation[]{new AddOperation(new JsonPointer("/unknown"), "a change")});

            //when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(jsonPatch);
            mockMvc.perform(patch(RESOURCE_URI, 1L).contentType("application/json-patch+json")
                            .content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("invalid-json-pointer")))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.title").value("Invalid JSON Pointer"))
                    .andExpect(jsonPath("$.detail").value("The JSON Pointer /unknown points to a non-existing property."))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/1"))
                    .andDo(document("patch-risk-invalid-pointer"));


            then(riskService).shouldHaveNoInteractions();
        }

        @ParameterizedTest
        @MethodSource("com.github.stueberm1.riskmanager.http.test.service.RestControllerTest#unsupportedOperations")
        void returnABadRequestWithProblemDetailsIfUsingAProhibitedJsonPatchOperation(JsonPatchOperation operation,
                                                                                     String expectedDetail) throws Exception {
            JsonPatch jsonPatch = new JsonPatch(new JsonPatchOperation[]{operation});

            //when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(jsonPatch);
            mockMvc.perform(patch(RESOURCE_URI, 1L).contentType("application/json-patch+json")
                            .content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("illegal-json-patch-operation")))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.title").value("Illegal JSON Patch Operation"))
                    .andExpect(jsonPath("$.detail").value(expectedDetail))
                    .andDo(document("patch-risk-illegal-patch-operation"));
        }

        @ParameterizedTest
        @MethodSource("com.github.stueberm1.riskmanager.http.test.service.RestControllerTest#unsupportedAttributes")
        void returnABadRequestWithProblemDetailsIfChangingAnImmutableJsonProperty(JsonPointer path, String expectedDetail)
                throws Exception {
            JsonPatch jsonPatch = new JsonPatch(new JsonPatchOperation[]{new ReplaceOperation(path,
                    "new value")});
            //when
            final String requestAsString = OBJECT_MAPPER.writeValueAsString(jsonPatch);
            mockMvc.perform(patch(RESOURCE_URI, 1L).contentType("application/json-patch+json")
                            .content(requestAsString).characterEncoding(StandardCharsets.UTF_8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                    .andExpect(jsonPath("$.type").value(resolveToProblemUriString("illegal-value-modification")))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.title").value("Illegal value modification"))
                    .andExpect(jsonPath("$.instance").value("http://localhost:8080/api/v1/risk/1"))
                    .andExpect(jsonPath("$.errors.length()").value(1))
                    .andExpect(jsonPath("$.errors[0].detail").value(expectedDetail))
                    .andExpect(jsonPath("$.errors[0].pointer").value(path.getRawPath()))
                    .andDo(document("patch-risk-illegal-argument-modification"));

        }

    }

    static Stream<Arguments> unsupportedOperations() {
        return Stream.of(
                Arguments.of(
                       new MoveOperation(new JsonPointer("/details"), new JsonPointer("/mitigationStrategy")),
                               "Json-Patch move is not supported. Reason: Business constraints does not allow to remove an attribute value"),
                Arguments.of(new RemoveOperation(new JsonPointer("/details")),
                        "Json-Patch remove is not supported. Reason: Business constraints does not allow to remove an attribute value")
                );
    }

    static Stream<Arguments> unsupportedAttributes() {
        return Stream.of(
                Arguments.of( new JsonPointer("/id"), "Id is primary key of the resource and is immutable"),
                Arguments.of(new JsonPointer("/description"), "Description is immutable")
        );
    }

}
