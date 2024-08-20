package org.piglinManager.piglinTrades;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("piglinTrades.reload")) {
            ReloadConfig reloadConfig = new ReloadConfig(plugin);
            reloadConfig.reloadPluginConfig();
            sender.sendMessage("Piglin trades configuration reloaded successfully!");
            return true;
        } else {
            sender.sendMessage("You don't have permission to reload the piglin trades configuration.");
            return false;
        }
    }
}
