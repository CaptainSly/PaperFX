package io.azraein.paperfx.system.inventory.items;

import java.io.Serializable;

public abstract class Item implements Serializable {

	private static final long serialVersionUID = -8314166625712196193L;

	private String itemId;
	private String itemName;
	private String itemDescription;

	private double itemWeight;

	public String getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public double getItemWeight() {
		return itemWeight;
	}

}
