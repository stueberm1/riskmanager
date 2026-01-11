/// The package {@code com.github.stueberm1.riskmanager.core.in.risk.filter} contains the elements of the domain
///  specific language (dsl) to define filters on the list of saved risks.
///
/// The interface {@link com.github.stueberm1.riskmanager.core.in.risk.filter.FilterSpec} is the entry-point
/// to define a new filter. New instances of the {@code FilterSpec} are provided by
/// {@link com.github.stueberm1.riskmanager.core.in.risk.RiskService#listFilteredWith()}.
///
/// The dsl leads the client over all attributes of the risk and enforce the client to define how to handle them. This
/// might include to ignore the attribute on searches, but that must also be defined explicitly. The dsl-enables the
/// support of auto-completion of any integrated development environment (ide) to configure a valid filter.
///
/// The last attribute-specification returns a
/// {@link com.github.stueberm1.riskmanager.core.in.risk.filter.FinalFilterStep}-instance which allows to perform the
/// request for lists matching the specified filter.
///
/// The different filter options on an attribute are mutually exclusive, so only one option can be selected per attribute.
///
/// ##Usage
///
/// ```java
/// List<RiskTO> risks = riskService.listFilteredWith()
///                     .severity().isIgnored()
///                     .andProbabilityOfOccurrence().isEqualTo(ProbabilityOfOccurrence.HIGH)
///                     .andDescription().isIgnored()
///                     .andDetails().isIgnored()
///                     .andContingencyPlanning().contains("Framework")
///                     .andMitigationStrategy().isEmpty()
///                     .toList();
/// ```
package com.github.stueberm1.riskmanager.core.in.risk.filter;