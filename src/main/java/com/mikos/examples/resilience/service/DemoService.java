package com.mikos.examples.resilience.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mikos.examples.resilience.model.Status;

import reactor.core.publisher.Mono;
import reactor.retry.Retry;

@Service
public class DemoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoService.class);

	private WebClient rest;

	public DemoService(WebClient.Builder builder) {
		this.rest = builder
				.baseUrl("http://localhost:8080")
				.build();
	}

	public Mono<Status> get() {
		LOGGER.debug("DemoService.get......");
		
		return rest
				.get()
				.uri("/intermittent")
				.retrieve()
				.bodyToMono(Status.class)
				.retryWhen(Retry.any()
								.fixedBackoff(Duration.ofMillis(100))
								.retryMax(3))
				.timeout(Duration.ofMillis(500))
				.onErrorReturn(fallback());
	}
	
	private Status fallback() {
		LOGGER.warn("DemoService.fallback......");
		Map<String, String> mapaNomes = new HashMap<String, String>();
		mapaNomes.put("status", "fallback 2");
		return new Status("DOWN","Fallback");
	}
}
