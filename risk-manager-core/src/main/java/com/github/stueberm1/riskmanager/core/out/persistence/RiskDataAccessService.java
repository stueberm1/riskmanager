package com.github.stueberm1.riskmanager.core.out.persistence;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.List;
import java.util.Optional;

/// Simple access to the persisted information of a {@code Risk} in a simple CRUD-behavior.
/// It defines the way it **requires** to communicate with the concrete persistence provider.
///  @implSpec Realizations of this interface **must** realize at least the rules and services defined by this
///     contract. They may realize more than defined by this interface, but it is absolutely mandatory, that realizations
///     meets the specifications of that interface.
public interface RiskDataAccessService {

    ///  Persists the information included in the data-access-object.
    ///
    /// @param risk The data access object containing the information about a specific risk
    void save(RiskDao risk);

    /// Orders the persistence provider to delete the risk identified by the given id.
    ///
    /// @param id Identifies the risk, which should be removed from the repository
    void delete(RiskIdentifier id);

    /// looks for the details about a risk identified by given identifier.
    /// If no risk was persisted with given id before, the operation **must** return an
    /// {@link Optional#empty()}.
    /// The application decides how to handle the empty-result.
    ///
    /// @param id Identifies the risk, the application require details for.
    /// @return the details about the risk as simple data access object, if available
    Optional<RiskDao> find(RiskIdentifier id);

    /// Lists the information of all risks persisted in the repository
    ///
    /// @return unfiltered list of all available risks
    List<RiskDao> listAll();
}
