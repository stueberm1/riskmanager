package com.github.stueberm1.riskmanager.http.patch;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.http.model.patch.JsonPatch;
import com.github.stueberm1.riskmanager.http.service.RiskPatchFactory;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public class JsonPatchOperationProcessingRiskPatchFactory implements RiskPatchFactory {

    @Override
    public RiskPatchTO createPatch(RiskIdentifier id, JsonPatch jsonPatch) {
        RiskPatchBuilder builder = new RiskPatchBuilder();
        builder.setRiskIdentifier(id);
        jsonPatch.assignTo(builder);

        return builder.build();
    }
}
