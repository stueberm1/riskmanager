package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.http.model.RiskJson;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("/risk")
public class RiskController {


    private final  RiskService riskService;

    private final RiskModelConverter converter;

    public RiskController(RiskService riskService, RiskModelConverter converter) {
        this.riskService = riskService;
        this.converter = converter;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@RequestBody  RiskJson riskJson) {
        riskService.createRisk(converter.convertToRiskModel(riskJson));

        riskJson.add(Link.of("/{riskIdentifier}").withSelfRel().expand(riskJson.getId()));
        return ResponseEntity.ok().body(riskJson);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<RiskJson>> getRisks() {
        CollectionModel<RiskJson> response = CollectionModel.of(riskService.listAll()
                .stream().filter(Objects::nonNull).map(converter::convertToHttpModel).toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{riskIdentifier}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> createRisk(@PathVariable("riskIdentifier") RiskIdentifier riskIdentifier,
                                               @RequestBody  RiskJson riskJson) {
        riskService.createRisk(converter.convertToRiskModel(riskJson));

        riskJson.add(Link.of("/{riskIdentifier}").withSelfRel().expand(riskJson.getId()));
        return ResponseEntity.ok().body(riskJson);
    }

    @GetMapping(value = "/{riskIdentifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RiskJson> getRisk(@PathVariable("riskIdentifier") RiskIdentifier riskIdentifier) {
        return ResponseEntity.ok().body(converter.convertToHttpModel(riskService.get(riskIdentifier)));
    }
}
