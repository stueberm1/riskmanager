package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskIdentifierAlreadyInUseException;
import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.http.model.ErrorDetail;
import com.github.stueberm1.riskmanager.http.model.ProblemDetails;
import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;
import com.github.stueberm1.riskmanager.types.risk.IllegalIdNumberException;
import com.github.stueberm1.riskmanager.types.risk.IllegalRiskIdentifierException;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.InvalidUrlException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestControllerAdvice
public class RiskControllerExceptionHandler {

    @ExceptionHandler(IllegalIdNumberException.class)
    public ResponseEntity<ProblemDetails> handleIllegalIdNumberException(IllegalIdNumberException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(IllegalIdNumberException ex)  {
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
        try {
            return UriComponentsBuilder.fromUriString(typeIdentifier).build().toUri();
        } catch (InvalidUrlException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(IllegalRiskIdentifierException.class)
    public ResponseEntity<ProblemDetails> handleIllegalRiskIdentifierException(IllegalRiskIdentifierException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(IllegalRiskIdentifierException ex)  {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-identifier"));
        problemDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetails.setTitle("Id is not a suitable RiskIdentifier");
        problemDetails.setDetail(ex.getMessage());
        return problemDetails;
    }

    @ExceptionHandler(EntityConstraintViolationException.class)
    public ResponseEntity<ProblemDetails> handleEntityConstraintViolationException(EntityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(EntityConstraintViolationException ex)  {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("invalid-risk-arguments"));
        problemDetails.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());
        problemDetails.setTitle("Some arguments of the risk are invalid.");
        problemDetails.setErrors(ex.violations().stream()
                .map(RiskControllerExceptionHandler::convertTo).toArray(ErrorDetail[]::new));
        return problemDetails;
    }

    private static ErrorDetail convertTo(EntityConstraintViolationException.EntityConstraintViolation violation) {
        return new ErrorDetail(violation.violation(), violation.path());
    }

    @ExceptionHandler(RiskIdentifierAlreadyInUseException.class)
    public ResponseEntity<ProblemDetails> handleRiskIdentifierAlreadyInUseException(RiskIdentifierAlreadyInUseException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(RiskIdentifierAlreadyInUseException ex)  {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("duplicate-identifier"));
        problemDetails.setStatus(HttpStatus.CONFLICT.value());
        problemDetails.setTitle("duplicated identifier");
        problemDetails.setDetail("The identifier is already in use by another risk and cannot used here.");
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }

    @ExceptionHandler(RiskNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleRiskNotFoundException(RiskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(convertTo(ex));
    }

    private static ProblemDetails convertTo(RiskNotFoundException ex)  {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(problemTypeFactory("risk-not-found"));
        problemDetails.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetails.setTitle("The requested risk was not found.");
        problemDetails.setDetail(String.format("The identifier %s does not exist.", ex.getRiskIdentifier().id()));
        URI uri = WebMvcLinkBuilder.linkTo(RiskController.class).slash(ex.getRiskIdentifier().id()).toUri();
        problemDetails.setInstance(uri.toString());
        return problemDetails;
    }
}
