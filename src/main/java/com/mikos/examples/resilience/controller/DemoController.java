package com.mikos.examples.resilience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikos.examples.resilience.model.Status;
import com.mikos.examples.resilience.service.DemoService;

import reactor.core.publisher.Mono;

@RestController
public class DemoController {

	@Autowired
	private final DemoService demoService;

	public DemoController(DemoService demoService) {
		this.demoService = demoService;
	}

	@GetMapping("/get")
	public Mono<Status> get() {
		return demoService.get();
	}
}
