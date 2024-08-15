package io.azraein.paperfx.system.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.azraein.paperfx.system.inventory.items.Item;

public class Inventory implements Serializable {

	private static final long serialVersionUID = -5957408433436832903L;

	private List<ItemSlot> itemSlots;
	private double inventoryWeight = 0.0;

	public Inventory() {
		itemSlots = new ArrayList<>();
	}

	public void addItem(Item item, int amount) {
		ItemSlot slot = getItemSlotFromId(item.getItemId());
		if (slot == null) {
			slot = getFreeItemSlot();
			slot.addItem(item, amount);
			inventoryWeight += (item.getItemWeight() * amount);
			return;
		}

		slot.addItem(amount);
		inventoryWeight += (item.getItemWeight() * amount);
	}

	public void removeItem(Item item, int amount) {
		ItemSlot slot = getItemSlotFromId(item.getItemId());
		if (slot == null)
			return;

		slot.removeItem(amount);
		inventoryWeight -= (item.getItemWeight() * amount);
	}

	public ItemSlot getFreeItemSlot() {
		ItemSlot slot = null;

		for (ItemSlot s : itemSlots) {
			if (s.isEmpty()) {
				slot = s;
				break;
			}
		}

		if (slot == null) {
			itemSlots.add(new ItemSlot());
			return getFreeItemSlot();
		}

		return slot;
	}

	public ItemSlot getItemSlotFromId(String itemId) {
		ItemSlot slot = null;

		for (ItemSlot s : itemSlots) {
			if (s.getItem().getItemId().equals(itemId)) {
				slot = s;
				break;
			}
		}

		return slot;
	}

	public double getInventoryWeight() {
		return inventoryWeight;
	}

	public List<ItemSlot> getItemSlots() {
		return itemSlots;
	}

}
