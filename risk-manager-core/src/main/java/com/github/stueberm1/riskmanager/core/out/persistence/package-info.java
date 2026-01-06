/// The package {@code com.github.stueberm1.riskmanager.core.out.persistence} defines the required port of the core
/// providing persistence operations.
/// The interfaces and types in this package describing service provider interfaces (SPI), which need to be implemented
/// externally (by other building blocks/modules), which gets injected everywhere the interfaces are used in the
/// risk-manager-core module.
///
/// There are no specification on how these injections gets organized. Everything from
/// hard-coding in module main, over java service provides up to fully featured dependency frameworks are possible.
///
/// @see "https://en.wikipedia.org/wiki/Service_provider_interface"
/// @see "https://www.baeldung.com/java-spi"
/// @see "https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html"
package com.github.stueberm1.riskmanager.core.out.persistence;
