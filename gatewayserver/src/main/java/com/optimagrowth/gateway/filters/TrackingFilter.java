package com.optimagrowth.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Order(1)
@Component
@Slf4j
public class TrackingFilter implements GlobalFilter {

	@Autowired
	FilterUtils filterUtils;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange,
			GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(requestHeaders)) {
			log.debug("tmx-correlation-id found in tracking filter: {}. ",
					filterUtils.getCorrelationId(requestHeaders));
		} else {
			String correlationID = generateCorrelationId();
			exchange = filterUtils.setCorrelationId(exchange, correlationID);
			log.debug("tmx-correlation-id generated in tracking filter: {}.",
					correlationID);
		}

		return chain.filter(exchange);
	}

	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		if (filterUtils.getCorrelationId(requestHeaders) != null) {
			return true;
		} else {
			return false;
		}
	}

	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}

}