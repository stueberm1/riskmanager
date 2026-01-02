package com.github.stueberm1.riskmanager.http.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.net.URI;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "status", "title", "detail", "errors"})
public class ProblemDetails {

    private URI type;
    private int status;
    private String title;
    private String instance;
    private String detail;
    private ErrorDetail[] errors;

    public URI getType() {
        return type;
    }

    public void setType(URI type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ErrorDetail[] getErrors() {
        return errors;
    }

    public void setErrors(ErrorDetail[] errors) {
        this.errors = errors;
    }
}
