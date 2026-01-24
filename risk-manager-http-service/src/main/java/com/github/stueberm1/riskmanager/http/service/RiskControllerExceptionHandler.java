package com.github.stueberm1.riskmanager.http.service;

import static java.lang.String.format;

import com.github.stueberm1.riskmanager.core.in.risk.RiskIdentifierAlreadyInUseException;
import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.http.model.ErrorDetail;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.ProblemDetails;
import com.github.stueberm1.riskmanager.http.patch.IllegalValueModificationRequestException;
import com.github.stueberm1.riskmanager.http.patch.InvalidJsonPointerException;
import com.github.stueberm1.riskmanager.http.patch.UnsupportedJsonPatchOperationException;
import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;
import com.github.stueberm1.riskmanager.types.risk.IllegalIdNumberException;
import com.github.stueberm1.riskmanager.types.risk.IllegalRiskIdentifierException;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.InvalidUrlException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestControllerAdvice
public class RiskControllerExceptionHandler {

    @ExceptionHandler(value = IllegalIdNumberException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleIllegalIdNumberException(IllegalIdNumberException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(IllegalIdNumberException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-id-number"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Id number of an RiskIdentifier must be a positive integer");
        problemDetails.setDetail(String.format("ID must be greater than zero, but was %d", ex.getIllegalCurrentNumber()));
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getIllegalCurrentNumber()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    private static URI problemTypeFactory(String typeIdentifier) {

        return WebMvcLinkBuilder.linkTo(DocumentationPathController.class).slash("problems").slash(typeIdentifier).toUri();

    }

    @ExceptionHandler(value = RiskIdentifierMisMatchException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleRiskIdentifierMisMatchException(RiskIdentifierMisMatchException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(RiskIdentifierMisMatchException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("identifier-mismatch"));
        problemDetails.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());
        problemDetails.setTitle("Object id do not match the request path");
        problemDetails.setDetail(String.format("Object path requires payload with id %s, but was %s", ex.getPathId(),
                ex.getObjectId()));
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getPathId()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    @ExceptionHandler(value = IllegalRiskIdentifierException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleIllegalRiskIdentifierException(IllegalRiskIdentifierException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(IllegalRiskIdentifierException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-identifier"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Id is not a suitable RiskIdentifier");
        problemDetails.setDetail(ex.getMessage());
        return problemDetails;
    }

    @ExceptionHandler(value = EntityConstraintViolationException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleEntityConstraintViolationException(EntityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(EntityConstraintViolationException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-risk-arguments"));
        problemDetails.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());
        problemDetails.setTitle("Some arguments of the risk are invalid.");
        problemDetails.setErrors(ex.violations().stream()
                .map(RiskControllerExceptionHandler::convertTo).toArray(ErrorDetail[]::new));
        return problemDetails;
    }

    private static ErrorDetail convertTo(EntityConstraintViolationException.EntityConstraintViolation violation) {
        return new ErrorDetail(violation.violation(), new JsonPointer(violation.path()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(MethodArgumentNotValidException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-risk-arguments"));
        problemDetails.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());
        problemDetails.setTitle(ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        problemDetails.setErrors(bindingResult.getFieldErrors().stream().map(RiskControllerExceptionHandler::convertTo).toArray(ErrorDetail[]::new));
        return problemDetails;
    }

    private static ErrorDetail convertTo(FieldError violation) {
        return new ErrorDetail(violation.getCode(), new JsonPointer("#/" + violation.getField()));
    }

    @ExceptionHandler(value = RiskIdentifierAlreadyInUseException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleRiskIdentifierAlreadyInUseException(RiskIdentifierAlreadyInUseException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(RiskIdentifierAlreadyInUseException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("duplicate-identifier"));
        problemDetails.setStatus(HttpStatus.CONFLICT.value());
        problemDetails.setTitle("duplicated identifier");
        problemDetails.setDetail("The identifier is already in use by another risk and cannot used here.");
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    @ExceptionHandler(value = RiskNotFoundException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleRiskNotFoundException(RiskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(RiskNotFoundException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("risk-not-found"));
        problemDetails.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetails.setTitle("The requested risk was not found.");
        problemDetails.setDetail(String.format("The identifier %s does not exist.", ex.getRiskIdentifier().id()));
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    @ExceptionHandler(value = InvalidJsonPointerException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleInvalidJsonPointerException(InvalidJsonPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(InvalidJsonPointerException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-json-pointer"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Invalid JSON Pointer");
        problemDetails.setDetail(format("The JSON Pointer %s points to a non-existing property.",
                ex.getJsonPointer().getRawPath()));
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    @ExceptionHandler(value = UnsupportedJsonPatchOperationException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleUnsupportedJsonPatchOperationException(UnsupportedJsonPatchOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(UnsupportedJsonPatchOperationException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("illegal-json-patch-operation"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Illegal JSON Patch Operation");
        problemDetails.setDetail(format("Json-Patch %s is not supported. Reason: %s", ex.getOperationName(), ex.getMessage()));
        return  problemDetails;
    }

    @ExceptionHandler(value = IllegalValueModificationRequestException.class, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetails> handleIllegalValueModificationRequestException(IllegalValueModificationRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(IllegalValueModificationRequestException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("illegal-value-modification"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Illegal value modification");
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        problemDetails.setErrors(new ErrorDetail[] {new ErrorDetail(ex.getMessage(), ex.getPath())});
        return problemDetails;
    }

}
