package com.github.stueberm1.riskmanager.http.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.http.model.RiskJson;
import com.github.stueberm1.riskmanager.http.model.patch.JsonPatch;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/risk")
public class RiskController {


    private final RiskService riskService;

    private final RiskModelConverter riskConverter;

    private final RiskPatchFactory riskPatchFactory;

    public RiskController(RiskService riskService, RiskModelConverter converter, RiskPatchFactory riskPatchFactory) {
        this.riskService = riskService;
        this.riskConverter = converter;
        this.riskPatchFactory = riskPatchFactory;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@Valid @RequestBody RiskJson riskJson) {
        riskService.createRisk(riskConverter.convertToRiskModel(riskJson));

        Link selfLink = linkTo(methodOn(RiskController.class).getRisk(riskJson.getId())).withSelfRel();
        riskJson.add(selfLink);
        return ResponseEntity.ok().body(riskJson);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<RiskJson>> getRisks() {
        CollectionModel<RiskJson> response = CollectionModel.of(riskService.listAll()
                .stream().filter(Objects::nonNull).map(riskConverter::convertToHttpModel).toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{riskIdentifier}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@PathVariable("riskIdentifier") Long riskIdentifier,
                                               @Valid @RequestBody RiskJson riskJson) {
        if (!riskIdentifier.equals(riskJson.getId())) {
            throw new RiskIdentifierMisMatchException(riskIdentifier, riskJson.getId());
        }

        riskService.createRisk(riskConverter.convertToRiskModel(riskJson));
        Link selfLink = linkTo(methodOn(RiskController.class).getRisk(riskIdentifier)).withSelfRel();
        riskJson.add(selfLink);
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
        RiskPatchTO riskPatchTO = riskPatchFactory.createPatch(id, jsonPatch);
        return ResponseEntity.ok().body(riskConverter.convertToHttpModel(riskService.updateRisk(riskPatchTO)));
    }
}
