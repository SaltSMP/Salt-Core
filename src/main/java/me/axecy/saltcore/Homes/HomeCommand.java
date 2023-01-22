package me.axecy.saltcore.Homes;

import me.axecy.saltcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class HomeCommand extends JavaPlugin {

    private final Main plugin;
    public HomeCommand(Main plugin) {
        this.plugin = plugin;
    }

    // Create a static HashMap to store player's home location
    public static HashMap<UUID, Location> homeLocations = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("sethome")) {
            // Set the player's home location
            Location playerLoc = player.getLocation();
            homeLocations.put(player.getUniqueId(), playerLoc);
            player.sendMessage(ChatColor.GREEN + "Home location set successfully!");
            return true;
        }
        if (command.getName().equalsIgnoreCase("home")) {
            if (!homeLocations.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You haven't set a home location yet! " + ChatColor.GRAY + "Use /sethome to set your home location.");
                return true;
            }
            // Teleport the player to their home location
            Location homeLoc = homeLocations.get(player.getUniqueId());
            player.teleport(homeLoc);
            player.sendMessage(ChatColor.GREEN + "Teleported to your home location!");
            return true;
        }
        return false;
    }
}
