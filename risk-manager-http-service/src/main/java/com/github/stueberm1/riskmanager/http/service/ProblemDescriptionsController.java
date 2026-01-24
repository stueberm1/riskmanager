package com.github.stueberm1.riskmanager.http.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

///  The `ProblemDescriptionController` provides online access to problem details description. Separating the static
/// REST-api description as provided by the [DocumentationPathController] from the problem details allows simply to move
/// the problem details to a path different from static `/docs` path, if required.
///
/// THe Controller provides static HTML, generated from the asciidoc-documentation
@Controller
@RequestMapping(path = "/docs/problems", produces = MediaType.TEXT_HTML_VALUE)
public class ProblemDescriptionsController {

    @GetMapping
    public String getOverview() {
        return "/docs/problems/index.html";
    }

    @GetMapping(path = "/duplicate-identifier")
    public String getDuplicateIdentifier() {
        return "/docs/problems/duplicate-identifier.html";
    }


    @GetMapping(path = "/identifier-mismatch")
    public String getIdentifierMismatch() {
        return "/docs/problems/identifier-mismatch.html";
    }

    @GetMapping(path = "/illegal-json-patch-operation")
    public String getIllegalJsonPatchOperation() {
        return "/docs/problems/illegal-json-patch-operation.html";
    }

    @GetMapping(path = "/illegal-value-modification")
    public String getIllegalValueModification() {
        return "/docs/problems/illegal-value-modification.html";
    }

    @GetMapping(path = "/invalid-identifier")
    public String getInvalidIdentifier() {
        return "/docs/problems/invalid-identifier.html";
    }

    @GetMapping(path = "/invalid-json-pointer")
    public String getInvalidJsonPointer() {
        return "/docs/problems/invalid-json-pointer.html";
    }

    @GetMapping(path = "/invalid-risk-arguments")
    public String getInvalidRiskArguments() {
        return "/docs/problems/invalid-risk-arguments.html";
    }

    @GetMapping(path = "/risk-not-found")
    public String getRiskNotFound() {
        return "/docs/problems/risk-not-found.html";
    }

}
