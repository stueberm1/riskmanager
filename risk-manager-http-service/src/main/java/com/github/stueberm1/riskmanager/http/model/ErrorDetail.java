package com.github.stueberm1.riskmanager.http.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import tools.jackson.databind.annotation.JsonSerialize;

@JsonPropertyOrder({"detail", "pointer"})
public class ErrorDetail {

    private String detail;
    private JsonPointer pointer;

    public ErrorDetail() {
    }

    public ErrorDetail(String detail, JsonPointer pointer) {
        this.detail = detail;
        this.pointer = pointer;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @JsonSerialize(using = JsonPointerSerializer.class)
    public JsonPointer getPointer() {
        return pointer;
    }

    public void setPointer(JsonPointer pointer) {
        this.pointer = pointer;
    }
}
