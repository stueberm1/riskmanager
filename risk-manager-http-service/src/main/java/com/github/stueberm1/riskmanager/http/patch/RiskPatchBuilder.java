package com.github.stueberm1.riskmanager.http.patch;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;


public class RiskPatchBuilder {

    private RiskIdentifier riskIdentifier;
    private Severity severity;
    private ProbabilityOfOccurrence probabilityOfOccurrence;
    private String details;
    private String contingencyPlanning;
    private String MitigationStrategy;

    public void setContingencyPlanning(String contingencyPlanning) {
        this.contingencyPlanning = contingencyPlanning;
    }

    public RiskIdentifier getRiskIdentifier() {
        return riskIdentifier;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setMitigationStrategy(String mitigationStrategy) {
        MitigationStrategy = mitigationStrategy;
    }

    public void setProbabilityOfOccurrence(ProbabilityOfOccurrence probabilityOfOccurrence) {
        this.probabilityOfOccurrence = probabilityOfOccurrence;
    }

    public void setRiskIdentifier(RiskIdentifier riskIdentifier) {
        this.riskIdentifier = riskIdentifier;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public RiskPatchTO build() {
        return new RiskPatchTO(riskIdentifier, severity, probabilityOfOccurrence, details, contingencyPlanning, MitigationStrategy);
    }
}
