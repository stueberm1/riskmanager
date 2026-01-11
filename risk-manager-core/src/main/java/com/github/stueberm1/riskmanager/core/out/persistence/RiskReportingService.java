package com.github.stueberm1.riskmanager.core.out.persistence;

import java.util.List;

/// The {@code RiskReportingService} allows more dynamic specified queries then the {@link RiskDataAccessService}.
/// Since the {@link RiskDataAccessService} provides predefined and simple queries only, the {@code RiskReportingService}
/// allows to modify or customize queries as required by single use-cases without the requirement of implementing new features
///  in the service provider.
public interface RiskReportingService {

    /// A variant of {@link RiskDataAccessService#listAll()} which allows to filter the result by use-case required
    /// parameters ot attribute level.
    /// While {@link RiskDataAccessService#listAll()} requires to load all and filter in the core application
    /// this operation delegates the filtering to the persistence provider realization, so less information gets loaded
    /// and less processing is required.
    ///
    /// @param riskFilter the filtering specification of the list operation
    /// @return A (potentially empty) list of risk information matching the filter specification
    List<RiskDao> listRisksFiltered(RiskFilter riskFilter);
}
