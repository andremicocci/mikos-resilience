package com.mikos.examples.resilience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikos.examples.resilience.model.Status;
import com.mikos.examples.resilience.service.IntermittentService;
import com.mikos.examples.resilience.service.Resilience4jService;
import com.mikos.examples.resilience.service.WebFluxService;

import reactor.core.publisher.Mono;

@RestController
public class DemoController {

	@Autowired
	private final WebFluxService webFluxService;

	@Autowired
	private final Resilience4jService resilience4jService;
	
	@Autowired
	private final IntermittentService intermittentService;
	

	public DemoController(WebFluxService webFluxService, Resilience4jService resilience4jService, IntermittentService intermittentService) {
		this.webFluxService = webFluxService;
		this.resilience4jService = resilience4jService;
		this.intermittentService = intermittentService;
	}

	@GetMapping("/get")
	public Mono<Status> get() {
		return webFluxService.get();
	}
	
	@GetMapping("/resilience4j")
	public Mono<Status> getResilience4j() {
		return resilience4jService.get();
	}
	
	@GetMapping("/intermittent")
	public Mono<Status> getIntermittent() {
		return intermittentService.get();
	}
	
}
