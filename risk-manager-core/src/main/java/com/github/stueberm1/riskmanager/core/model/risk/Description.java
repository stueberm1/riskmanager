package com.github.stueberm1.riskmanager.core.model.risk;

import com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.requireNonNull;

/// The `Description` is a short introduction to the risk or technical dept.
/// It is also used to the headline (header) of the risk and provides a simple
/// background to get into a problem, without regarding the details.
///
/// A description of a [Risk] should have a length of at least ten characters and should not have more than
/// 50 characters. This should provide enough information about the risk without overloading the (short) description
/// with too much details.
///
/// The `Descriptio` is abstract so default rules can be modified by extending the root description.
public abstract class Description {

    /**
     * Default minimum length of the description as used by the validate operation.
     */
    protected static final long MINIMUM_DESCRIPTION_LENGTH = 10L;

    /**
     * Default maximum length of the description as used by the validate operation.
     */
    protected static final long MAXIMUM_DESCRIPTION_LENGTH = 50L;

    private final String content;

    /**
     * Initializes the description with the given content.
     * The constructor sets and checks the content against existing
     *
     * @param content text representation of the Description
     * @throws NullPointerException if the argument is null
     * @throws com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException
     *  if the argument does not meet a business constraint.
     */
    protected Description(final String content) {
        this.content = requireNonNull(content);
        List<EntityConstraintViolationException.EntityConstraintViolation> violations = validate(content);
        if (!violations.isEmpty()) {
            throw new EntityConstraintViolationException(entityType(), violations);
        }
    }

    public String value() {
        return content;
    }

    /// Type of the aggregate root (root entity of an aggregate) the `Description` belongs to.
    /// Because the `Description` is part of the [Risk](-aggregate, the default `entityType` is [Class<Risk>].
    /// @return Type of the aggregate root, this `Description` belongs to.
    /// @implSpec Concrete-descriptions can overwrite the default type, but they **must** ensure the value belongs to
    /// an aggregate-root (Resource in REST). Deviating behavior can corrupt the problem feedback sent to callers of the system, since
    /// the Constraint-Violations provides the path to the problem.
    protected Class<?> entityType() {
        return Risk.class;
    }


     /// The operation is called by the constructor to validate the value-string of the Description.
     /// The result is a potentially (and hopefully) empty List of {@link EntityConstraintViolationException}.
     ///
     /// @param value The string representation of the [Description]
     /// @return potentially list of [com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException.EntityConstraintViolation]
     /// @implNote The default implementation validates separately against [Description#MINIMUM_DESCRIPTION_LENGTH]
     ///        an [Description#MAXIMUM_DESCRIPTION_LENGTH]. It also assumes the default [Description#entityType()]
     ///        or [Class<Risk>]
     /// @implSpec  when the operation gets overridden, the implementation **must** ensure, that every constraint, gets validated
     ///        and reported separately, even if they are affecting the same field. Any violation **must** end in a
     ///        separate [com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException.EntityConstraintViolation]
     /// @apiNote The default implementation can be called by implementation or can be reimplemented completely.
     ///        if called, the default behavior get in charge.
    protected List<EntityConstraintViolationException.EntityConstraintViolation> validate(
            final String value)  {
        List<EntityConstraintViolationException.EntityConstraintViolation> violations = new CopyOnWriteArrayList<>();

        if (value.length() < MINIMUM_DESCRIPTION_LENGTH) {
            violations.add(new EntityConstraintViolationException.EntityConstraintViolation("#/description",
                    "Description must have at least 10 characters length"));
        }

        if (value.length() > MAXIMUM_DESCRIPTION_LENGTH) {
            violations.add(new EntityConstraintViolationException.EntityConstraintViolation("#/description",
                    "Description must have at most 50 characters length"));
        }

        return violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Description that = (Description) o;

        return new EqualsBuilder().append(content, that.content).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(content).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("content", content)
                .toString();
    }
}
