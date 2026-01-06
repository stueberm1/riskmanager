package com.github.stueberm1.riskmanager.http.model;

import java.util.List;

/// A [RFC-6901](https://www.rfc-editor.org/rfc/rfc6901.txt) compliant pointer to a property in a Json-Object
public class JsonPointer  {

    private final String rawPath;

    public JsonPointer(String rawPath) {
        this.rawPath = rawPath;
    }

    public String getRawPath() {
        return rawPath;
    }

    public String[] referenceTokens() {
        return rawPath.split("/");
    }

    public String rootToken() {
        return referenceTokens()[1];
    }
}
