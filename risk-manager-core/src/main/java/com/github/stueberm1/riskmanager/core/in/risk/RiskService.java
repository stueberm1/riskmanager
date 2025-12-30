package com.github.stueberm1.riskmanager.core.in.risk;

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
}
