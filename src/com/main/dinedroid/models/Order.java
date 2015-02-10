package com.main.dinedroid.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;

public class Order implements Serializable {
	
	private int tableId;
	private ArrayList<FoodItem> order;
	private String orderNotes;
	
	public Order(ArrayList<FoodItem> order)
	{
		this.order = order;
		
	}
	
	public ArrayList<FoodItem> getOrder()
	{
		return order;
	}
	
	public void setOrder(ArrayList<FoodItem> order)
	{
		this.order = order;
	}
	
	public int getOrderTable()
	{
		return tableId;
	}
	
	public void setOrderTable(int tableId)
	{
		this.tableId = tableId;
	}
	
	public void setOrderNotes(String orderNotes)
	{
		this.orderNotes = orderNotes;
	}
	
	public String getOrderNotes()
	{
		return orderNotes;
	}
	
	public double getTotalPrice()
	{
		double totalPrice = 0;
		for(FoodItem e : order)
		{
			totalPrice += (e.getPrice());
			for(FoodItem f : e.getExtras())
			{
				totalPrice += f.getPrice();
			}
			totalPrice = totalPrice*e.getQuantity();
		}
		return totalPrice;
	}
	
	public String toString()
	{
		return "Table: "+tableId;
	}
	
	/**
	 * 
	 * @return Detailed Order information
	 */
	public String detailedString()
	{
		String temp;
		if(order.size() == 0)
		{
			temp = "Table: "+tableId+"\nNo Order";
			return temp;
		}
		else
		{
			temp = "Table: "+tableId+"\nOrder:\n";
			for(int i = 0; i < order.size(); ++i)
			{
				temp += order.get(i).displayString() + "\n";
			}
			
			return temp;
		}
	}
}
