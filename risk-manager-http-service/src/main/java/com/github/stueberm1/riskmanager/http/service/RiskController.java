package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.http.model.ProblemDetails;
import com.github.stueberm1.riskmanager.http.model.RiskJson;
import com.github.stueberm1.riskmanager.http.model.JsonPatch;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/risk")
@OpenAPIDefinition(
        externalDocs = @ExternalDocumentation(
                 url = "/docs/index.html",
                description = "The detailed  static service description including workflow description."
        )
)
public class RiskController {


    private final RiskService riskService;

    private final RiskModelConverter riskConverter;

    private final RiskPatchFactory riskJsonPatchFactory;

    public RiskController(RiskService riskService, RiskModelConverter converter, RiskPatchFactory riskJsonPatchFactory) {
        this.riskService = riskService;
        this.riskConverter = converter;
        this.riskJsonPatchFactory = riskJsonPatchFactory;
    }

    @Operation(summary = "Creates a new risk by a complete description in in JSON-format"
            , requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                useParameterTypeSchema = true,
            description = "Description of a new risk",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RiskJson.class)
            )
    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The risk was created successful and can be read by the self-link provided by in the response object",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RiskJson.class)
                    )}
            ),
            @ApiResponse(responseCode = "400",
                    description = "The Id, provided with the request body, does not meet the specification",
                    content =  {@Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetails.class)
                    )}
            ),
            @ApiResponse(responseCode = "409",
            description = "The identifier provided in RequestBody is used by another object ",
            content =  {@Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetails.class)
            )}),
            @ApiResponse(responseCode = "422",
                    description = "The Object in the RequestBody violates a constraint",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetails.class)
                    )}
            )
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@Valid @RequestBody RiskJson riskJson) {
        riskService.createRisk(riskConverter.convertToRiskModel(riskJson));

        Link selfLink = linkTo(methodOn(RiskController.class).getRisk(riskJson.getId())).withSelfRel();
        Link apiDesc = Link.of("/v3/api-docs").withRel("service-desc");
        Link apiDoc = linkTo(DocumentationPathController.class).withRel("service-doc");
        Link editLink = linkTo(methodOn(RiskController.class).getRisk(riskJson.getId())).withRel(IanaLinkRelations.EDIT)
                .andAffordance(afford(methodOn(RiskController.class).patchRisk(riskJson.getId(), null)));
        Link riskListLink = linkTo(RiskController.class).withRel(IanaLinkRelations.COLLECTION);
        riskJson.add(selfLink, apiDesc, apiDoc, editLink, riskListLink);
        return ResponseEntity.ok().body(riskJson);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<RiskJson>> getRisks(@RequestParam(value = "severity", required = false) Severity severity,
                                                              @RequestParam(value = "probabilityOfOccurrence", required = false) ProbabilityOfOccurrence probabilityOfOccurrence,
                                                              @RequestParam(value = "description", required = false) String description,
                                                              @RequestParam(value = "details", required = false) String details,
                                                              @RequestParam(value = "contingencyPlanning", required = false) String contingencyPlanning,
                                                              @RequestParam(value = "mitigationStrategy",required = false) String mitigationStrategy) {
        QueryParameterEvaluator queryParameterEvaluator = QueryParameterEvaluator.builder()
                .severity(severity)
                .probabilityOfOccurrence(probabilityOfOccurrence)
                .description(description)
                .details(details)
                .contingencyPlanning(contingencyPlanning)
                .mitigationStrategy(mitigationStrategy)
                .riskService(riskService)
                .build();

        CollectionModel<RiskJson> response = CollectionModel.of(queryParameterEvaluator.performListRequest()
                .stream().filter(Objects::nonNull).map(riskConverter::convertToHttpModel).toList()).add(
            linkTo(methodOn(RiskController.class).getRisks(severity, probabilityOfOccurrence, description, details,
                    contingencyPlanning, mitigationStrategy)).withRel("filtered"),
                linkTo(DocumentationPathController.class).withRel("service-doc"),
                Link.of("/v3/api-docs").withRel("service-desc")
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{riskIdentifier}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@PathVariable("riskIdentifier") Long riskIdentifier,
                                               @Valid @RequestBody RiskJson riskJson) {
        if (!riskIdentifier.equals(riskJson.getId())) {
            throw new RiskIdentifierMisMatchException(riskIdentifier, riskJson.getId());
        }

        riskJson.setId(riskIdentifier);
        riskService.createRisk(riskConverter.convertToRiskModel(riskJson));
        Link selfLink = linkTo(methodOn(RiskController.class).getRisk(riskIdentifier)).withSelfRel();
        Link riskListLink = linkTo(RiskController.class).withRel(IanaLinkRelations.COLLECTION);
        Link apiDoc = linkTo(DocumentationPathController.class).withRel("service-doc");
        Link apiDesc = Link.of("/v3/api-docs").withRel("service-desc");
        riskJson.add(selfLink, riskListLink, apiDesc, apiDoc);
        return ResponseEntity.ok().body(riskJson);
    }

    @GetMapping(value = "/{riskIdentifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> getRisk(@PathVariable("riskIdentifier") Long riskIdentifier) {
        RiskIdentifier id = SimpleNumericRiskIdentifier.builder().withCurrentNumber(riskIdentifier).build();
        return ResponseEntity.ok().body(riskConverter.convertToHttpModel(riskService.get(id)));
    }

    @PatchMapping(path = "/{riskIdentifier}", consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiskJson> patchRisk(@PathVariable("riskIdentifier") Long riskIdentifier, @RequestBody JsonPatch jsonPatch) {
        RiskIdentifier id = SimpleNumericRiskIdentifier.builder().withCurrentNumber(riskIdentifier).build();
        RiskPatchTO riskPatchTO = riskJsonPatchFactory.createPatch(id, jsonPatch);
        return ResponseEntity.ok().body(riskConverter.convertToHttpModel(riskService.updateRisk(riskPatchTO)));
    }
}
