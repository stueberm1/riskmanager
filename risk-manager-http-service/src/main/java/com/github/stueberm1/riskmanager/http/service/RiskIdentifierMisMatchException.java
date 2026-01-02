package com.github.stueberm1.riskmanager.http.service;

public class RiskIdentifierMisMatchException extends RuntimeException {

    private final String pathId;
    private final String objectId;
    public RiskIdentifierMisMatchException(String message, String pathId, String objectId) {
        super(message);
        this.pathId = pathId;
        this.objectId = objectId;
    }

    public RiskIdentifierMisMatchException(String pathId, String objectId) {
        this(null, pathId, objectId);
    }

    public RiskIdentifierMisMatchException(Long pathId,Long objectId) {
        this(pathId.toString(), objectId.toString());
    }

    public String getPathId() {
        return pathId;
    }

    public String getObjectId() {
        return objectId;
    }
}
