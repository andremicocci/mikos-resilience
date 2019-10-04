package com.mikos.examples.resilience.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mikos.examples.resilience.model.Status;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

@Service
public class Resilience4jService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Resilience4jService.class);

	private WebClient webClient;
	
	@Autowired
	private WebClient.Builder builderInjected;
	
	@Value("${api.intermittent.endpoint}")
	private String endpoint;
	
	@PostConstruct
	private void init() {
		webClient = builderInjected
				.baseUrl(endpoint)
				.build();
	}	
	
	@CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
	@RateLimiter(name = "backendA")
	@Bulkhead(name = "backendA")
	@Retry(name = "backendA", fallbackMethod = "fallback")
	public Mono<Status> get() {
		LOGGER.debug("Resilience4jService.get......");
		
		return webClient
				.get()
				.uri("/intermittent")
				.retrieve()
				.bodyToMono(Status.class);
//				.doOnError(err -> LOGGER.error(err.getMessage()))
//				.retryBackoff(retryMax, Duration.ofMillis(retryInterval))

	}

	private Mono<Status> fallback(org.springframework.web.client.HttpServerErrorException e) {
		return Mono.just(new Status("DOWN","Fallback - HttpServerErrorException"));
	}

	private Mono<Status> fallback(org.springframework.web.reactive.function.client.WebClientResponseException e) {
		return Mono.just(new Status("DOWN","Fallback - WebClientResponseException"));
	}

	private Mono<Status> fallback(RuntimeException e) {
		return Mono.just(new Status("DOWN","Fallback"));
	}
}
