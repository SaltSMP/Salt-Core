package me.axecy.saltcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {
    private final Main plugin;
    public ReloadConfigCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GOLD + "SaltCore " + ChatColor.GREEN + "configuration reloaded successfully!");
        return true;
    }
}
