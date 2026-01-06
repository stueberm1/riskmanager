package com.github.stueberm1.riskmanager.http.model.patch;
import static java.util.Objects.requireNonNull;
import com.github.stueberm1.riskmanager.types.RiskManagerException;

public class UnsupportedJsonPatchOperationException extends RiskManagerException {

    private final JsonPatchOperation operation;
    public UnsupportedJsonPatchOperationException(String message, JsonPatchOperation operation) {
        super(message);
        this.operation = requireNonNull(operation, "operation");
    }

    public JsonPatchOperation getOperation() {
        return operation;
    }

    public String getOperationName() {
        return getOperation().getOp();
    }
}
