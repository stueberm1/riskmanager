package com.github.stueberm1.riskmanager.core.out.persistence;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import java.util.List;
import java.util.Optional;

/// Simple access to the persisted information of a {@code Risk} in a simple CRUD-behaviour.
/// It defines the way it **requires** to communicate with the concrete persistence provider.
///  @implSpec Realizations of this interface **must** realize at least the rules and services defined by this
///     contract. They may realize more than defined by this interface, but it is absolutely mandatory, that realizations
///     meets the specifications of that interface.
public interface RiskDataAccessService {
    void save(RiskDao risk);
    void delete(RiskIdentifier id);
    Optional<RiskDao> read(RiskIdentifier id);
    List<RiskDao> listAll();
}
