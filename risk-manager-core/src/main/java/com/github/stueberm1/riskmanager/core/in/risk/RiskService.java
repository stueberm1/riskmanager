package com.github.stueberm1.riskmanager.core.in.risk;

import com.github.stueberm1.riskmanager.core.in.risk.filter.FilterSpec;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.List;

public interface RiskService {

    ///
    ///
    /// @throws RiskIdentifierAlreadyInUseException
    ///         if the client tries to define a risk with a blocked id
    /// @throws com.github.stueberm1.riskmanager.types.risk.EntityConstraintViolationException
    ///     if one or more values of the Risk violates a given business constraint
    void createRisk(RiskTO newRisk);

    /// @throws RiskNotFoundException if there is no risk persisted having the requested {@link RiskIdentifier}
    RiskTO get(RiskIdentifier id);
    List<RiskTO> listAll();

    /// Allows to list available risks using a customized filter. The Operation provides a {@link FilterSpec}
    /// which is the entry point of defining the filter and perform the list operation using a domain-specific language
    /// (dsl).
    ///
    /// @return a new instance of a {@link FilterSpec} to define a filtered list-operation
    /// @see com.github.stueberm1.riskmanager.core.in.risk.filter
    FilterSpec listFilteredWith();

    RiskTO updateRisk(RiskPatchTO riskPatch);
}
