package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.requireNonNull;

/// The {@code Description} is a short introduction to the risk or technical dept.
/// It is also used to the headline (header) of the risk and provides a simple
/// background to get into a problem, without regarding the details.
///
/// A description of a {@link Risk} should have a length of at least ten characters and should not have more than
/// 50 characters. This should provide enough information about the risk without overloading the (short) description
/// with too much details.
public abstract class Description {

    protected static final long MINIMUM_DESCRIPTION_LENGTH = 10L;
    protected static final long MAXIMUM_DESCRIPTION_LENGTH = 50L;

    private final String content;

    /**
     * Initializes the description with the given content.
     * The constructor sets and checks the content against existing
     * @param content text representation of the Description
     * @throws NullPointerException if the argument is null
     * @throws com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException
     *  if the argument does not meet a business constraint.
     */
    protected Description(final String content) {
        this.content = requireNonNull(content);
        List<EntityConstraintViolationException.EntityConstraintViolation> violations = validate(content);
        if (!violations.isEmpty()) {
            throw new EntityConstraintViolationException(Risk.class, violations);
        }
    }

    public String value() {
        return content;
    }

    protected List<EntityConstraintViolationException.EntityConstraintViolation> validate(
            final String value) throws EntityConstraintViolationException {
        List<EntityConstraintViolationException.EntityConstraintViolation> violations = new CopyOnWriteArrayList<>();

        if (value.length() < MINIMUM_DESCRIPTION_LENGTH) {
            violations.add(new EntityConstraintViolationException.EntityConstraintViolation("risk.description",
                    "Description must have at least 10 characters length"));
        }

        if (value.length() > MAXIMUM_DESCRIPTION_LENGTH) {
            violations.add(new EntityConstraintViolationException.EntityConstraintViolation("risk.description",
                    "Description must have at most 50 characters length"));
        }

        return violations;
    }
}
