package com.github.stueberm1.riskmanager.http.model.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.patch.RiskPatchBuilder;
import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.Severity;

public abstract class ValueSettingOperation  extends JsonPatchOperation {

    public ValueSettingOperation(JsonPointer path) {
        super(path);
    }

    @Override
    public void applyTo(final RiskPatchBuilder builder) {
        String rootToken = path.rootToken();
        switch (rootToken) {
            case "id" -> throw new IllegalValueModificationRequestException(
                    "Id is primary key of the resource and is immutable",
                    path, builder.getRiskIdentifier());
            case "description" -> throw new IllegalValueModificationRequestException("Description is immutable", path,
                    builder.getRiskIdentifier());
            case "severity" ->  builder.setSeverity(Severity.valueOf(getValue()));
            case "probabilityOfOccurrence" -> builder.setProbabilityOfOccurrence(ProbabilityOfOccurrence.valueOf(getValue()));
            case "details" -> builder.setDetails(getValue());
            case "contingencyPlanning" -> builder.setContingencyPlanning(getValue());
            case "mitigationStrategy" -> builder.setMitigationStrategy(getValue());
            default -> throw new InvalidJsonPointerException(builder.getRiskIdentifier(), path);
        }

    }

    protected abstract String getValue();
}
