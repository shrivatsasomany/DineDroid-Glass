package com.main.dinedroid.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;

public class Table implements Serializable {
	
	private Integer id;
	private Waiter waiter;
	Order order;
	private String customerName;
	private boolean isOccupied;
	
	/*
	 * 1 -> OK
	 * 2 -> Delayed
	 * 3 -> Problem
	 */
	private Integer orderStatus;
	
	public Table(Integer id)
	{
		this.id = id;
		waiter = null;
		order = null;
		customerName = null;
		isOccupied = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Waiter getWaiter() {
		return waiter;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString()
	{
		String occupied = "Inactive";
		if(isOccupied)
		{
			occupied = "Active";
		}
		return "Table Number: " + id + " - " + occupied;
	}
	
}
