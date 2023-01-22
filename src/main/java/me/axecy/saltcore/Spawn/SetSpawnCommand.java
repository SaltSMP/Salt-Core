package me.axecy.saltcore.Spawn;

import me.axecy.saltcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {
    private final Main plugin;
    public SetSpawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        Location playerLocation = player.getLocation();
        plugin.getConfig().set("spawn.location.world", playerLocation.getWorld().getName());
        plugin.getConfig().set("spawn.location.x", playerLocation.getX());
        plugin.getConfig().set("spawn.location.y", playerLocation.getY());
        plugin.getConfig().set("spawn.location.z", playerLocation.getZ());
        plugin.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Spawn location set to your current location.");
        return true;
    }
}
