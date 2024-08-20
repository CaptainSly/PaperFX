package io.azraein.inkfx.system.inventory.items;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Lootlist implements Serializable {

    private static final long serialVersionUID = -8509392989564469362L;

    private final String lootlistId;
    private List<String> itemIds;

    public Lootlist(String lootlistId, Item... items) {
        this.lootlistId = lootlistId;
        itemIds = new ArrayList<>();

        for (Item item : items)
            itemIds.add(item.getItemId());
    }

    public void addItem(Item item) {
        itemIds.add(item.getItemId());
    }

    public String getLootlistId() {
        return lootlistId;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

}
