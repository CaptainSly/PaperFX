package io.azraein.inkfx.system.inventory.items;

import java.io.Serializable;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;

// TODO: Items need heavy ass help

public class Item implements Serializable {

	private static final long serialVersionUID = -8314166625712196193L;

	private String itemId;
	private String itemName;
	private String itemDescription;

	private String itemImage;
	private String itemScript;

	private ItemType itemType;

	private double itemWeight;

	public Item(String itemId, ItemType itemType, String itemName, String itemDescription) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDescription = itemDescription;

		this.itemType = itemType;
	}

	public void onUse() {
		if (!itemScript.isEmpty())
			Paper.SE.runFunction(itemScript, "onUse",
					CoerceJavaToLua.coerce(Paper.PAPER_PLAYER_PROPERTY.get().getActorState()));
	}

	public String getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public String getItemScript() {
		return itemScript;
	}

	public String getItemImage() {
		return itemImage;
	}

	public double getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(double itemWeight) {
		this.itemWeight = itemWeight;
	}

	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}

	public void setItemScript(String itemScript) {
		this.itemScript = itemScript;
	}

}
