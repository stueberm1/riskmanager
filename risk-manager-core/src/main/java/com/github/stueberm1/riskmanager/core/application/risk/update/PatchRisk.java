package com.github.stueberm1.riskmanager.core.application.risk.update;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.model.risk.RiskPatch;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public interface PatchRisk {

    PatchSpecification patchRiskIdentifiedBy(RiskIdentifier riskIdentifier);

   interface PatchSpecification {
        Risk with(RiskPatch riskPatch);
    }
}
