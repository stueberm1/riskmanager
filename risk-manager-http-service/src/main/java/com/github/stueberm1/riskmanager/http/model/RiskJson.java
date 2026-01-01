package com.github.stueberm1.riskmanager.http.model;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import org.springframework.hateoas.RepresentationModel;

public class RiskJson extends RepresentationModel<RiskJson> {

    private RiskIdentifier id;
    private Severity severity;
    private ProbabilityOfOccurrence probabilityOfOccurrence;
    private String description;
    private String details;
    private String contingencyPlanning;
    private String mitigationStrategy;

    public RiskIdentifier getId() {
        return id;
    }

    public void setId(RiskIdentifier id) {
        this.id = id;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public ProbabilityOfOccurrence getProbabilityOfOccurrence() {
        return probabilityOfOccurrence;
    }

    public void setProbabilityOfOccurrence(ProbabilityOfOccurrence probabilityOfOccurrence) {
        this.probabilityOfOccurrence = probabilityOfOccurrence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContingencyPlanning() {
        return contingencyPlanning;
    }

    public void setContingencyPlanning(String contingencyPlanning) {
        this.contingencyPlanning = contingencyPlanning;
    }

    public String getMitigationStrategy() {
        return mitigationStrategy;
    }

    public void setMitigationStrategy(String mitigationStrategy) {
        this.mitigationStrategy = mitigationStrategy;
    }
}
