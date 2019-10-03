package com.mikos.examples.resilience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikos.examples.resilience.model.Status;
import com.mikos.examples.resilience.service.IntermittentService;
import com.mikos.examples.resilience.service.WebFluxService;

import reactor.core.publisher.Mono;

@RestController
public class DemoController {

	@Autowired
	private final WebFluxService webFluxService;
	
	@Autowired
	private final IntermittentService intermittentService;
	

	public DemoController(WebFluxService webFluxService, IntermittentService intermittentService) {
		this.webFluxService = webFluxService;
		this.intermittentService = intermittentService;
	}

	@GetMapping("/get")
	public Mono<Status> get() {
		return webFluxService.get();
	}
	
	@GetMapping("/intermittent")
	public Mono<Status> getIntermittent() {
		return intermittentService.get();
	}
	
}
