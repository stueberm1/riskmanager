package com.github.stueberm1.riskmanager.http.model.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.patch.RiskPatchBuilder;

public abstract class DualPathOperation extends JsonPatchOperation {
    protected final JsonPointer from;

    public DualPathOperation( JsonPointer path, JsonPointer from) {
        super(path);
        this.from = from;
    }

    @Override
    public void applyTo(RiskPatchBuilder builder) {
        // do nothing
    }
}
