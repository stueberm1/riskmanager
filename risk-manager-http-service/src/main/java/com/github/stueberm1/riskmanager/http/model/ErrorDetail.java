package com.github.stueberm1.riskmanager.http.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"detail", "pointer"})
public class ErrorDetail {

    private String detail;
    private String pointer;

    public ErrorDetail() {
    }

    public ErrorDetail(String detail, String pointer) {
        this.detail = detail;
        this.pointer = pointer;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }
}
