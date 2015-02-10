package com.main.dinedroid.models;

import java.io.Serializable;

public class Restore implements Serializable {
	
	private boolean isOccupied;
	private Order o;
	private Waiter waiter;
	
	public Restore(boolean isOccupied, Order o, Waiter waiter) {
		this.isOccupied = isOccupied;
		this.o = o;
		this.waiter = waiter;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public Order getOrder() {
		return o;
	}

	public void setOrder(Order o) {
		this.o = o;
	}

	public Waiter getWaiter() {
		return waiter;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
}
