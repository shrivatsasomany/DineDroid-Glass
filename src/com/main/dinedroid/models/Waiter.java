package com.main.dinedroid.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Waiter implements Serializable {
	private int id;
	private String fname;
	private String lname;
	private ArrayList<Table> tables; 
	private ArrayList<Table> hailQueue;
	
	public Waiter(int id, String fname, String lname) {
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.tables = new ArrayList<Table>();
		this.hailQueue = new ArrayList<Table>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFName() {
		return fname;
	}

	public void setFName(String fname) {
		this.fname = fname;
	}
	
	public String getLName() {
		return lname;
	}

	public void setLName(String fname) {
		this.lname = fname;
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}
	
	public boolean addTable(Table table)
	{
		for(Table t : tables)
		{
			if(t.getId() == table.getId())
			{
				return false;
			}
		}
		tables.add(table);
		return true;
	}
	
	public boolean removeTable(int tableId)
	{
		for(int i = 0; i < tables.size(); ++i)
		{
			if(tables.get(i).getId().equals(tableId))
			{
				tables.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeAllTables()
	{
		for(int i = 0; i < tables.size(); ++i)
		{
			tables.remove(i);
		}
		return true;
	}

	public ArrayList<Table> getHailQueue() {
		return hailQueue;
	}

	public void setHailQueue(ArrayList<Table> hailQueue) {
		this.hailQueue = hailQueue;
	}
	
	public synchronized boolean addHail(Table table)
	{
		for(int i = 0; i < hailQueue.size(); ++i)
		{
			if(hailQueue.get(i).getId() == table.getId())
			{
				return false;
			}
		}
		return (this.hailQueue.add(table));
	}
	
	public synchronized boolean removeHail(int tableId)
	{
		for(int i = 0; i < hailQueue.size(); ++i)
		{
			if(hailQueue.get(i).getId() == tableId)
			{
				hailQueue.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Waiter other = (Waiter) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public String toString()
	{
		String tableInfo = "";
		if(!tables.isEmpty())
		{
			tableInfo = "[-";
			for(Table e : tables)
			{
				tableInfo+=e.getId()+"-";
			}
			tableInfo += "]";
		}
		return "ID: "+id+"\tName: "+fname+ " "+lname + " - Tables: "+tableInfo;
	}
	
}
