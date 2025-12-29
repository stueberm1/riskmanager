package com.github.stueberm1.riskmanager.core.application.risk;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.ContingencyPlanning;
import com.github.stueberm1.riskmanager.core.model.risk.MitigationStrategy;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ModelAdaptingRiskServiceFacade  implements RiskService {

    private final RiskFactory riskFactory;

    private final CreateRisk createRisk;

    private final RiskReader riskReader;

    private final Risks risks;

    @Override
    public void createRisk(RiskTO newRisk) {
        createRisk.save(riskFactory.create(newRisk));
    }

    @Override
    public RiskTO get(RiskIdentifier id) {
        return convert(riskReader.read(id));
    }

    private static RiskTO convert(final Risk risk) {
        return new RiskTO(risk.id(), risk.severity(), risk.probabilityOfOccurrence(), risk.description().value(),
                risk.details().detailContent(), risk.contingencyPlanning().map(ContingencyPlanning::plan).orElse(null),
                risk.getMitigationStrategy().map(MitigationStrategy::strategy).orElse(null));
    }

    @Override
    public List<RiskTO> listAll() {
        return risks.listAll().stream().map(ModelAdaptingRiskServiceFacade::convert).toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    private ModelAdaptingRiskServiceFacade(Builder builder) {
        this.riskFactory = requireNonNull(builder.riskFactory, "riskFactory");
        this.createRisk = requireNonNull(builder.createRisk, "createRisk");
        this.riskReader = requireNonNull(builder.riskReader, "riskReader");
        this.risks = requireNonNull(builder.risks, "risks");
    }

    public static final class Builder {
        private RiskFactory riskFactory;

        public Builder riskFactory(RiskFactory riskFactory) {
            this.riskFactory = riskFactory;
            return this;
        }

        private CreateRisk createRisk;
        public Builder createRisk(CreateRisk createRisk) {
            this.createRisk = createRisk;
            return this;
        }

        private RiskReader riskReader;
        public Builder riskReader(RiskReader riskReader) {
            this.riskReader = riskReader;
            return this;
        }

        private Risks risks;
        public Builder risks(Risks risks) {
            this.risks = risks;
            return this;
        }

        public ModelAdaptingRiskServiceFacade build() {
            return new ModelAdaptingRiskServiceFacade(this);
        }
    }
}
