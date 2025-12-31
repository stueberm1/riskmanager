package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskData;

public interface RiskDataConverter<T extends RiskDao, R extends RiskData> {

    R convert(T riskDao);

    T convert(R riskDao);

}
