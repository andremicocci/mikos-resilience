package com.mikos.examples.mikosresilience4jretry.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mikos.examples.mikosresilience4jretry.model.Status;

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

	//@Retry(name = "backendA", fallbackMethod = "fallback")
//	public Mono<Map> get() {
	public Mono<Status> get() {
		LOGGER.debug("DemoService.get......");
		
//		String requestEnquadramentoODM = "xxx";
//		rest.post()
//			.uri("")
//			.contentType(MediaType.APPLICATION_JSON)
//			.accept(MediaType.APPLICATION_JSON)
//	        .body(Mono.just(requestEnquadramentoODM), String.class)
//	        .retrieve()
//	        .bodyToFlux(String.class)
//	        .retryWhen(Retry.any())
//	        .collectList()
//	        .block();
		
		
		return rest
				.get()
				.uri("/crazy")
				.retrieve()
//				.bodyToMono(Map.class)
				.bodyToMono(Status.class)
				.retryWhen(Retry.any()
								.fixedBackoff(Duration.ofMillis(100))
								.retryMax(3))
				.timeout(Duration.ofMillis(500))
				.onErrorReturn(fallback())
				.log("xxxxxxxxxxxxxxxxxxxxx");
	}
	
//	private Map<String, String> fallback() {
	private Status fallback() {
		LOGGER.warn("DemoService.fallback......");
		Map<String, String> mapaNomes = new HashMap<String, String>();
		mapaNomes.put("status", "fallback 2");
//		return mapaNomes;
		return new Status("DOWN","Fallback");
	}
}
