package com.github.stueberm1.riskmanager.data.jpa.risk;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;;

/// {@code RiskData} defines the object-relational mapping between data tables and the java code.
/// Instances of the {@code RiskData} represents a single row in the corresponding data table.
@Entity
@Table(name="risk_data")
public class RiskData {


    private String riskIdentifier;

    private Severity severity;

    private ProbabilityOfOccurrence probabilityOfOccurrence;

    private String description;

    private String details;

    private String contingencyPlanning;

    private String mitigationStrategy;

    @Id
    @NotNull
    @NotBlank
    @Column(name = "risk_id", unique = true, nullable = false, updatable = false)
    public String getRiskIdentifier() {
        return riskIdentifier;
    }

    public void setRiskIdentifier(String riskIdentifier) {
        this.riskIdentifier = riskIdentifier;
    }

    @NotNull
    @Column(name = "severity", nullable = false)
    @Enumerated(EnumType.STRING)
    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "probability_of_occurence")
    public ProbabilityOfOccurrence getProbabilityOfOccurrence() {
        return probabilityOfOccurrence;
    }

    public void setProbabilityOfOccurrence(ProbabilityOfOccurrence probabilityOfOccurrence) {
        this.probabilityOfOccurrence = probabilityOfOccurrence;
    }

    @NotNull
    @NotBlank
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank
    @Lob
    @Column(name = "details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Lob
    @Column(name = "contingency_planning")
    public String getContingencyPlanning() {
        return contingencyPlanning;
    }

    public void setContingencyPlanning(String contingencyPlanning) {
        this.contingencyPlanning = contingencyPlanning;
    }

    @Lob
    @Column(name = "mitigation_strategy")
    public String getMitigationStrategy() {
        return mitigationStrategy;
    }

    public void setMitigationStrategy(String mitigationStrategy) {
        this.mitigationStrategy = mitigationStrategy;
    }
}
