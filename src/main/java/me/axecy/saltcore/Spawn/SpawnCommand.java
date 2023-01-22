package me.axecy.saltcore.Spawn;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    private final Main plugin;
    public SpawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        Location spawn = new Location(
                Bukkit.getWorld(plugin.getConfig().getString("spawn.location.world")),
                plugin.getConfig().getDouble("spawn.location.x"),
                plugin.getConfig().getDouble("spawn.location.y"),
                plugin.getConfig().getDouble("spawn.location.z")
        );
        player.teleport(spawn);
        sender.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.GOLD + "spawn" + ChatColor.GREEN + "!");
        return true;
    }
}
