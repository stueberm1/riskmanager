package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.http.model.RiskJson;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class RiskTORiskModelConverter implements RiskModelConverter {
    @Override
    public RiskJson convertToHttpModel(RiskTO riskTO) {
        RiskJson riskJson = new RiskJson();
        riskJson.setId(Long.parseLong(riskTO.id().id()));
        riskJson.setSeverity(riskTO.severity());
        riskJson.setProbabilityOfOccurrence(riskTO.probabilityOfOccurrence());
        riskJson.setDescription(riskTO.description());
        riskJson.setDetails(riskTO.details());
        riskJson.setContingencyPlanning(riskTO.contingencyPlanning());
        riskJson.setMitigationStrategy(riskTO.mitigationStrategy());
        Link selfLink = linkTo(methodOn(RiskController.class).getRisk(riskJson.getId())).withSelfRel();
        Link editLink = linkTo(methodOn(RiskController.class).getRisk(riskJson.getId())).withRel(IanaLinkRelations.EDIT)
                .andAffordance(afford(methodOn(RiskController.class).patchRisk(riskJson.getId(), null)));
        Link riskListLink = linkTo(RiskController.class).withRel(IanaLinkRelations.COLLECTION);
        Link apiDesc = Link.of("/v3/api-docs").withRel("service-desc");
        Link apiDoc = linkTo(DocumentationPathController.class).withRel("service-doc");
        riskJson.add(selfLink,editLink, riskListLink, apiDesc, apiDoc);
        return riskJson;
    }

    @Override
    public RiskTO convertToRiskModel(RiskJson riskJson) {
        RiskIdentifier id = SimpleNumericRiskIdentifier.builder()
                .withCurrentNumber(riskJson.getId())
                .build();

        return new RiskTO(id, riskJson.getSeverity(), riskJson.getProbabilityOfOccurrence(),
                riskJson.getDescription(), riskJson.getDetails(), riskJson.getContingencyPlanning(),
                riskJson.getMitigationStrategy());
    }
}
