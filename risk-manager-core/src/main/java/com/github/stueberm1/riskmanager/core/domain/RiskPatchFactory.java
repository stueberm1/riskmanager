package com.github.stueberm1.riskmanager.core.domain;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.core.model.risk.RiskPatch;

public interface RiskPatchFactory {

    RiskPatch create(RiskPatchTO riskPatch);
}
