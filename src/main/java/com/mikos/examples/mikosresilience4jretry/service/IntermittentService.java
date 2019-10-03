package com.mikos.examples.mikosresilience4jretry.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.mikos.examples.mikosresilience4jretry.model.Status;

import reactor.core.publisher.Mono;

@Service
public class IntermittentService {

	private Map<String, String> mapaNomes;
	private Status status;
	
//	public Mono<Map<String, String>> get() throws Exception {
	public Mono<Status> get() throws Exception {
		mapaNomes = new HashMap<String, String>();
		int timee = (int) (System.currentTimeMillis() % 3);
		if (timee == 2) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
		} else if(timee == 1 ) {
			Thread.sleep(200);
			
			mapaNomes.put("status", "Lentidão");
			status = new Status("UP", "SLOW");
		} else {
			mapaNomes.put("status", "OK");
			status = new Status("UP", "NORMAL");
		}
//		return Mono.just(mapaNomes);
		return Mono.just(status);
	}
	

}
