package com.github.stueberm1.riskmanager.core.application.risk.create;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.model.risk.ContingencyPlanning;
import com.github.stueberm1.riskmanager.core.model.risk.MitigationStrategy;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;

/// The {@code CreateRiskPersistenceAdapter} decouples the use-case realization of {@link CreateRisk} from the
/// definition of the persistence layer and hides the internal realization of the domain-model ({@link Risk}) from the
/// persistence realization provided by a service provider.
///
/// This allows adaptions of the domain-model and -servies without affects of "third party"-realizations of the
/// {@link RiskDataAccessService}.
///
/// It does not grant access to all functionality of the {@code RiskDataAccessService}, but only the {@link RiskDataAccessService#save(RiskDao)}
/// operation, which is required by {@link CreateRisk}.
public final class CreateRiskPersistenceAdapter  implements CreateRisk {

    private final RiskDataAccessService riskDataAccessService;

    public CreateRiskPersistenceAdapter(RiskDataAccessService riskDataAccessService) {
        this.riskDataAccessService = requireNonNull(riskDataAccessService);
    }

    @Override
    public void save(Risk newRisk) {
        riskDataAccessService.save(convert(newRisk));
    }


    private static RiskDao convert(Risk risk) {
        SimpleRiskDao.Builder builder = SimpleRiskDao.builder()
                .hasId(risk.id())
                .withSeverity(risk.severity())
                .probabilityOfOccurrence(risk.probabilityOfOccurrence())
                .havingDescription(risk.description().value())
                .withDetailedInformation(risk.details().detailContent());
        risk.contingencyPlanning().map(ContingencyPlanning::plan).ifPresent(builder::contingencyPlanning);
        risk.getMitigationStrategy().map(MitigationStrategy::strategy).ifPresent(builder::mitigationStrategy);
        return builder.build();
    }
}
