package com.github.stueberm1.riskmanager.core.application.risk.update;

import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.model.risk.RiskPatch;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class DefaultPatchRisk implements PatchRisk {


    private final CreateRisk createRiskPersistenceAdapter;
    private final RiskFinder riskReadingAdapter;

    public DefaultPatchRisk(CreateRisk createRiskPersistenceAdapter, RiskFinder riskReadingAdapter) {
        this.createRiskPersistenceAdapter = requireNonNull(createRiskPersistenceAdapter, "createRiskPersistenceAdapter");
        this.riskReadingAdapter = requireNonNull(riskReadingAdapter, "riskReadingAdapter");
    }

    @Override
    public PatchSpecification patchRiskIdentifiedBy(RiskIdentifier riskIdentifier) {
        Optional<Risk> risk = riskReadingAdapter.find(riskIdentifier);

        return new PatchExecutor(createRiskPersistenceAdapter::save, risk.orElseThrow(() -> new RiskNotFoundException(
                "risk to patch is not available", riskIdentifier)));
    }

    private static class PatchExecutor implements PatchSpecification {

        private final Risk risk;

        private final Consumer<Risk> persister;

        public PatchExecutor(Consumer<Risk> persister, Risk risk) {
            this.persister = persister;
            this.risk = risk;
        }

        @Override
        public Risk with(RiskPatch riskPatch) {
            Risk patched = risk.applyPatch(riskPatch);
            persister.accept(patched);
            return patched;
        }
    }
}
