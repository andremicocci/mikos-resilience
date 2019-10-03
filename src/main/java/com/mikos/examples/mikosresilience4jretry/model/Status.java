package com.mikos.examples.mikosresilience4jretry.model;

public class Status {

	private String status;
	private String health;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public Status(String status, String health) {
		super();
		this.status = status;
		this.health = health;
	}

	@Override
	public String toString() {
		return "Status [status=" + status + ", health=" + health + "]";
	}

}
