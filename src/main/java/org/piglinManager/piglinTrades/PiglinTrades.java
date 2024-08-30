package org.piglinManager.piglinTrades;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class PiglinTrades extends JavaPlugin implements Listener {
    public static boolean is_enabled = true;
    public static boolean log = true;

    public static boolean override_vanilla = true;

    public static TradeItems trades = new TradeItems();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ReloadConfig reloadConfig = new ReloadConfig(this);
        reloadConfig.reloadPluginConfig();

        if (this.getCommand("piglinreload") != null) {
            this.getCommand("piglinreload").setExecutor(new ReloadCommand(this));
            getLogger().info("Piglin reload command registered successfully.");
        } else {
            getLogger().severe("Failed to register piglin reload command! Make sure it's defined in plugin.yml.");
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPiglinBarter(EntityDropItemEvent event) {
        if (!is_enabled) {
            return;
        }

        if (trades.items.isEmpty()) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Piglin piglin) {
            event.getItemDrop().setItemStack(Objects.requireNonNull(TradeItems.selectItem(trades.items)));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}