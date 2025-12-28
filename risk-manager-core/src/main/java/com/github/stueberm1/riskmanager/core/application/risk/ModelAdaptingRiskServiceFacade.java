package com.github.stueberm1.riskmanager.core.application.risk;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import java.util.List;

public class ModelAdaptingRiskServiceFacade  implements RiskService {

    private final RiskFactory riskFactory;

    private final CreateRisk createRisk;

    @Override
    public void createRisk(RiskTO newRisk) {
        createRisk.save(riskFactory.create(newRisk));
    }

    @Override
    public RiskTO get(RiskIdentifier id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public List<RiskTO> listAll() {
        return List.of();
    }

    public static Builder builder() {
        return new Builder();
    }

    private ModelAdaptingRiskServiceFacade(Builder builder) {
        this.riskFactory = requireNonNull(builder.riskFactory, "riskFactory");
        this.createRisk = requireNonNull(builder.createRisk, "createRisk");
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

        public ModelAdaptingRiskServiceFacade build() {
            return new ModelAdaptingRiskServiceFacade(this);
        }
    }
}
