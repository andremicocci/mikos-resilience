package com.mikos.examples.resilience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mikos.examples.resilience.model.Status;
import com.mikos.examples.resilience.service.IntermittentService;

import reactor.core.publisher.Mono;

@RestController
public class IntermittentController {

	@Autowired
	private final IntermittentService intermittentService;

	public IntermittentController(IntermittentService intermittentService) {
		this.intermittentService = intermittentService;
	}

	@GetMapping("/intermittent")
	public Mono<Status> getIntermittent() {
		try {
			return intermittentService.get();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Servico Indisponivel... :(", e);
		}
	}

}
