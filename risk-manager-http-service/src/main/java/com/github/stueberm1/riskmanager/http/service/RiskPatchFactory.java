package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.http.model.JsonPatch;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public interface RiskPatchFactory {

    RiskPatchTO createPatch(RiskIdentifier id, JsonPatch jsonPatch);
}
