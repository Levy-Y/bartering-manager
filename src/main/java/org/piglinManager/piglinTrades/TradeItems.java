package org.piglinManager.piglinTrades;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TradeItems {
    private final Random random = new Random();

    public HashMap<ItemStack, Integer> items = new HashMap<>();

    public static ItemStack selectItem(HashMap<ItemStack, Integer> items) {
        int totalWeight = 0;
        for (int weight : items.values()) {
            totalWeight += weight;
        }

        Random random = new Random();
        int randomWeight = random.nextInt(totalWeight);

        for (Map.Entry<ItemStack, Integer> entry : items.entrySet()) {
            randomWeight -= entry.getValue();
            if (randomWeight < 0) {
                return entry.getKey();
            }

            //if (randomWeight < 0 && entry.getKey() != null) {
            //    return entry.getKey();
            //}
        }

        return null;
    }

    public void emptyTradeItemCache() {
        items = new HashMap<>();
    }
}
