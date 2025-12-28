package com.github.stueberm1.riskmanager.core.application.risk.create;

import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import com.github.stueberm1.riskmanager.core.in.risk.RiskIdentifierAlreadyInUseException;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;

import java.util.Optional;

public class IdValidatingCreateRisk implements CreateRisk {

    private final CreateRisk createRiskPersistenceAdapter;
    private final RiskReader riskReadingAdapter;

    public IdValidatingCreateRisk(CreateRisk createRiskPersistenceAdapter, RiskReader riskReadingAdapter) {
        this.createRiskPersistenceAdapter = createRiskPersistenceAdapter;
        this.riskReadingAdapter = riskReadingAdapter;
    }

    @Override
    public void save(Risk newRisk) {

        Optional<Risk> existingRisk = riskReadingAdapter.read(newRisk.id());
        if(existingRisk.isPresent()) {
            handleDuplicateId(existingRisk.get(), newRisk);
        } else {
            createRiskPersistenceAdapter.save(newRisk);
        }
    }

    private static void handleDuplicateId(Risk existingRisk, Risk newRisk) {
        if (! (existingRisk.equals(newRisk))) {
            throw new RiskIdentifierAlreadyInUseException("The RiskIdentifier belongs to another Risk", newRisk.id());
        }
    }

}
