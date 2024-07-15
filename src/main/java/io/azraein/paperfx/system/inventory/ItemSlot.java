package io.azraein.paperfx.system.inventory;

import java.io.Serializable;

import io.azraein.paperfx.system.inventory.items.Item;

public class ItemSlot implements Serializable {

	private static final long serialVersionUID = 4460303972359775014L;

	private Item item;
	private int itemCount;
	private boolean isEmpty;

	public ItemSlot() {
		item = null;
		itemCount = 0;
		isEmpty = true;
	}

	public ItemSlot(Item item, int itemCount) {
		this.item = item;
		this.itemCount = itemCount;
		isEmpty = false;
	}

	public void addItem(Item item, int amount) {
		this.item = item;
		this.itemCount = amount;
		isEmpty = false;
	}

	public void addItem(int amount) {
		if (this.isEmpty())
			return;

		itemCount += amount;
	}

	public void removeItem(int amount) {
		itemCount -= amount;
		if (itemCount <= 0) {
			item = null;
			itemCount = 0;
			isEmpty = true;
		}
	}

	public Item getItem() {
		return item;
	}

	public int getItemCount() {
		return itemCount;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

}
