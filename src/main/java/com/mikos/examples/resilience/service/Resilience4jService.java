package com.mikos.examples.resilience.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mikos.examples.resilience.model.Status;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
	
	@Retry(name = "backendA", fallbackMethod = "fallback_Retry")
	@CircuitBreaker(name = "backendA", fallbackMethod = "fallback_CB")
//	@RateLimiter(name = "backendA")
//	@Bulkhead(name = "backendA")
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

//	public Mono<Status> get() {
//		io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = io.github.resilience4j.circuitbreaker.CircuitBreaker.ofDefaults("backendA");
//		io.github.resilience4j.retry.Retry retry = io.github.resilience4j.retry.Retry.ofDefaults("backendA");
//		
//		return Mono.fromCallable(this::get2)
////			.compose(CircuitBreakerOperator.of(circuitBreaker))
//			.compose(RetryOperator.of(retry))
//			.block();
//	}

	private Mono<Status> fallback_Retry(RuntimeException e) {
		return Mono.just(new Status("DOWN","Fallback Retry"));
	}

	private Mono<Status> fallback_CB(org.springframework.web.reactive.function.client.WebClientResponseException e) {
		throw new RuntimeException("banana");
		//return Mono.just(new Status("DOWN","Fallback Circuit Breaker"));
	}
	
}
