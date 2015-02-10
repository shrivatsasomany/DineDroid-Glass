package com.main.dinedroid.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodItem implements Serializable {
	private int id;
	private String name;
	private Double price;
	private ArrayList<FoodItem> items;
	private ArrayList<FoodItem> extras;
	private String notes;
	private int quantity;
	private boolean isAvailable = true;
	private boolean isCategory;
	private FoodItem parent;

	public FoodItem(int id, String name, double price, boolean isCategory) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.isCategory = isCategory;
		this.items = new ArrayList<FoodItem>();
		this.extras = new ArrayList<FoodItem>();
	}

	public FoodItem(int id, String name, Double price,
			ArrayList<FoodItem> dishes, ArrayList<FoodItem> extras,
			String notes, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.items = dishes;
		this.extras = extras;
		this.notes = notes;
		this.quantity = quantity;
	}

	public void nullifyParent() {
		this.parent = null;
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				items.get(i).nullifyParent();
			}
		}
	}

	public void setParent(FoodItem parent) {
		this.parent = parent;
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				items.get(i).setParent(this);
			}
		}
	}

	public void populateMap(HashMap<Object, FoodItem> myMap) {
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				myMap.put(new Integer(items.get(i).getID()), items.get(i));
				items.get(i).populateMap(myMap);
			}
		}
	}

	public FoodItem getParent() {
		return parent;
	}

	public boolean isChecked() {
		return isAvailable;
	}

	public void flipChecked() {
		if (isAvailable == true) {
			isAvailable = false;
		} else {
			isAvailable = true;
		}
	}
	
	public FoodItem findItem(int itemId)
	{
		FoodItem result = null;
		for(int i = 0; i < items.size(); ++i)
		{
			if(items.get(i).isCategory)
			{
				result = items.get(i).findItem(itemId);
			}
			else
			{
				if(items.get(i).id == itemId)
				{
					result = items.get(i);
				}
			}
		}
		return result;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int q) {
		quantity = q;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public ArrayList<FoodItem> getItems() {
		return items;
	}

	public ArrayList<FoodItem> getExtras() {
		return extras;
	}

	public String getNotes() {
		return notes;
	}
	
	public boolean isAvailable()
	{
		return isAvailable;
	}

	public void addNotes(String note) {
		notes = note;
	}

	public boolean addItem(FoodItem item) {
		return items.add(item);
	}
	
	public boolean removeItem(FoodItem item) {
		return items.remove(item);
	}

	public void setItems(ArrayList<FoodItem> items) {
		this.items = items;
	}

	public void addExtra(FoodItem extra) {
		extras.add(extra);
	}
	
	public void removeExtra(FoodItem extra) {
		extras.remove(extra);
	}

	public void setExtra(ArrayList<FoodItem> extras) {
		this.extras = extras;
	}
	
	public void setCategory(boolean isCategory)
	{
		this.isCategory = isCategory;
	}
	
	public void setAvailable(boolean availability)
	{
		isAvailable = availability;
	}
	
	public boolean isCategory()
	{
		return isCategory;
	}

	public boolean equals(FoodItem compare) {
		if (this.id == compare.id) {
			return true;
		}

		else {
			return false;
		}
	}

	public String toString() {
		return name +" - "+ id;
	}

	public String toDetailedString() {
		return name + "\n" + price;
	}
	
	/**
	 * 
	 * @return Display formatted detailed information
	 */
	public String displayString()
	{
		String temp;
		temp = "\t\tDish: "+name+"\n"+"\t\tQuantity: "+quantity+"\n";
		if(extras.isEmpty() == true)
		{
			temp = temp + "\t\tExtras: None\n";
			return temp;
		}
		
		else
		{
			String chosen_extras = "\t\tExtras:\n";
			for(int i = 0; i < extras.size(); ++i)
			{
				chosen_extras+="\t\t\t\t"+extras.get(i) + "\n";
			}
			
			return temp + chosen_extras;
		}
	}
}
