package com.mikos.examples.mikosresilience4jretry.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikos.examples.mikosresilience4jretry.model.Status;
import com.mikos.examples.mikosresilience4jretry.service.DemoService;

import reactor.core.publisher.Mono;

@RestController
public class DemoController {

	Logger LOG = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	private final DemoService demoService;

	public DemoController(DemoService demoService) {
		this.demoService = demoService;
	}

	@GetMapping("/get")
//	public Mono<Map> get() {
	public Mono<Status> get() {
		return demoService.get();
	}
}
