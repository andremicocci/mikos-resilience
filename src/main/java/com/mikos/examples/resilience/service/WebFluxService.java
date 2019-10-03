package com.mikos.examples.resilience.service;

import java.time.Duration;
import java.time.LocalTime;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mikos.examples.resilience.model.Status;

import reactor.core.publisher.Mono;
import reactor.retry.Retry;

@Service
public class WebFluxService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxService.class);

	private WebClient webClient;
	
	@Autowired
	private WebClient.Builder builderInjected;
	
	@Value("${api.intermittent.endpoint}")
	private String endpoint;

	@Value("${api.intermittent.timeout}")
	private Integer timeout;
	
	@Value("${api.intermittent.retry.max}")
	private int retryMax;
	
	@Value("${api.intermittent.retry.interval}")
	private Integer retryInterval;
	
	@PostConstruct
	private void init() {
		webClient = builderInjected
				.baseUrl(endpoint)
				.build();
	}
	
	public Mono<Status> get() {
		LOGGER.debug("WebFluxService.get......");
		
		return webClient
				.get()
				.uri("/intermittent")
				.retrieve()
				.bodyToMono(Status.class)
//				.doOnError(err -> LOGGER.error(err.getMessage()))
//				.retryBackoff(retryMax, Duration.ofMillis(retryInterval))
				.retryWhen(Retry.any()
								.fixedBackoff(Duration.ofMillis(retryInterval))
								.retryMax(retryMax)
								.doOnRetry(s -> LOGGER.warn("Retry at " + LocalTime.now())))
				.timeout(Duration.ofMillis(timeout))
				.onErrorReturn(fallback());
	}
	
	private Status fallback() {
		return new Status("DOWN","Fallback");
	}
}
