package com.github.stueberm1.riskmanager.types.risk;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.types.RiskManagerException;

import java.util.LinkedList;
import java.util.List;

/// The {@code EntityConstraintViolationException} is inspired by the {@code jakarta.validation.ConstraintViolationException}
/// introduced with JPA 2.0  and the bean validation framework.
/// In opposite to the original {@code ConstraintViolationException} it is designed for getting used in plain old java objects
/// (POJOs) without dependencies to complex validation frameworks as hibernate-validator.
///
/// The {@link EntityConstraintViolation} gets two argument:
///
/// 1. The type of the aggregate root which gets validated
/// 2. A list of ({@link EntityConstraintViolation})
///
/// The List of {@link EntityConstraintViolation} allows to inform clients about all violations in one exception instead
/// of informing them violation by violation., so they can achieve there goals with less pain.
/// @see "https://beanvalidation.org/4.0/"
/// @see "https://jakarta.ee/specifications/bean-validation/4.0/apidocs/jakarta.validation/jakarta/validation/constraintviolationexception"
public class EntityConstraintViolationException extends RiskManagerException {

    /**
     * the Defined message string of the {@code EntityConstraintViolationException}. If not replaced by exception handling,
     * this is also the log message.
     */
    public static final String MESSAGE = "operation violates a defined internal constraint of the entity";

    private final Class<?> entityType;

    private final List<EntityConstraintViolation> violations;

    /// Defines the {@code EntityConstraintViolationException} together with all the found {@link EntityConstraintViolation}.
    /// Beside the aggregate root it **must** contain at least one {@link EntityConstraintViolation}.
    /// @param entityType The type of the concrete aggregate root
    /// @param violations List of constraint violations
    /// @throws NullPointerException if the {@code entityType} or the {@code violations} list is null
    /// @throws IllegalArgumentException if the {@code violations} is empty
    public  EntityConstraintViolationException(Class<?> entityType, List<EntityConstraintViolation> violations) {
        super(MESSAGE);
        this.entityType = requireNonNull(entityType, "entity type");
        this.violations = new LinkedList<>(requireNonNull(violations, "violations"));
        if (violations.isEmpty()) {
            throw new IllegalArgumentException("At lease one  constraint violation must be provided");
        }
    }

    public String entityName() {
        return entityType.getName();
    }

    public List<EntityConstraintViolation> violations() {
        return new LinkedList<>(violations);
    }

    /// The @code {@link EntityConstraintViolation} describes a single violation of a (business-)constraint in
    /// the hierarchy of an aggregate (resource).
    /// It is inspired by the {@code jakarta.validation.ConstraintViolation} but with much less complexity.
    ///  Beside the violation itself, only some kind of path to identify the concrete violation is required. This
    /// Enables simple java-objects (POJOs) to define the constraint violation without binding them to high complex
    /// frameworks as hibernate-validator.
    ///
    /// @see "https://beanvalidation.org/4.0/"
    /// @see "https://jakarta.ee/specifications/bean-validation/4.0/apidocs/jakarta.validation/jakarta/validation/constraintviolation"
    /// @see "https://hibernate.org/validator/"
    public  static final class EntityConstraintViolation {
        private final String path;
        private final String violation;

        /// Creates a new constraint violation in the context of an aggregate root. It **must** contain
        /// a path to the violated field in the aggregate relative to the path from the root of the aggregate and a
        /// description of the current violation.
        /// Each defined violation needs its own instance of the {@code EntityConstraintViolation} so clients are able
        /// to fix the request in one action.
        ///
        /// The syntax of the {@code path} is not specified (yet). But to simplify the presentation of problem details,
        /// it should conform to one of the well-known formats XPATh, json-path, or the OPEN-API specification, so that
        /// the predefined exception handling can process it accordingly.
        ///
        /// @param path The path to the constraint violation within the context of the aggregate root
        /// @param violation the concrete violation of a business constraint
        /// @throws NullPointerException if one of the parameters are null. The violation is specified by field
        public EntityConstraintViolation(String path, String violation) {
            this.path = requireNonNull(path, "path");
            this.violation = requireNonNull(violation, "violation message");
        }

        public String path() {
            return path;
        }

        public String violation() {
            return violation;
        }
    }

}
