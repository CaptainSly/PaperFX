package io.azraein.inkfx.system.inventory;

import java.io.Serializable;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.inventory.items.Item;

public class ItemSlot implements Serializable {

	private static final long serialVersionUID = 4460303972359775014L;

	private String itemId;
	private int itemCount;
	private boolean isEmpty;

	public ItemSlot() {
		itemId = "";
		itemCount = 0;
		isEmpty = true;
	}

	public ItemSlot(Item item, int itemCount) {
		this.itemId = item.getItemId();
		this.itemCount = itemCount;
		isEmpty = false;
	}

	public void addItem(Item item, int amount) {
		this.itemId = item.getItemId();
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
			itemId = "";
			itemCount = 0;
			isEmpty = true;
		}
	}

	public Item getItem() {
		return Paper.DATABASE.getItem(itemId);
	}

	public int getItemCount() {
		return itemCount;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

}
