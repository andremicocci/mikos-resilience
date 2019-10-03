package com.mikos.examples.resilience.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.mikos.examples.resilience.model.Status;

import reactor.core.publisher.Mono;

@Service
public class IntermittentService {

	private Status status;
	
	public Mono<Status> get() {
		int timee = (int) (System.currentTimeMillis() % 3);
		if (timee == 2) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
		} else if(timee == 1 ) {
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
			status = new Status("UP", "SLOW");
		} else {
			status = new Status("UP", "NORMAL");
		}
		return Mono.just(status);
	}
}
