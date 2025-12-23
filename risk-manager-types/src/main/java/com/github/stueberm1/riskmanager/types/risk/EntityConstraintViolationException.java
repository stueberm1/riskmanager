package com.github.stueberm1.riskmanager.types.risk;
import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.List;

public class EntityConstraintViolationException extends RuntimeException {

    public static final String MESSAGE = "operation violates a defined internal constraint of the entity";

    private final Class<?> entityType;

    private final List<EntityConstraintViolation> violations;

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

    public  static final class EntityConstraintViolation {
        private final String path;
        private final String violation;

        public EntityConstraintViolation(String path, String violation) {
            this.path = path;
            this.violation = violation;
        }

        public String path() {
            return path;
        }

        public String violation() {
            return violation;
        }
    }

}
