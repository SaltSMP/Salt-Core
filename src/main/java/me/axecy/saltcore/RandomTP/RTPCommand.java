package me.axecy.saltcore.RandomTP;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RTPCommand implements CommandExecutor {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final Main plugin;
    public RTPCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        // Check if the player is on cooldown
        if (cooldowns.containsKey(playerUUID)) {
            long lastExecution = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            int timePassed = (int) (currentTime - lastExecution) / 1000;

            // Get cooldown in seconds
            int cooldownTime = plugin.getConfig().getInt("randomtp.cooldown") * 60;
            if (timePassed < cooldownTime) {
                int timeLeft = cooldownTime - timePassed;
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + timeLeft + " seconds.");
                return true;
            }
        }
        // If the player is not on cooldown, or the cooldown has expired
        World world = Bukkit.getWorld(plugin.getConfig().getString("randomtp.world"));
        ConfigurationSection range = plugin.getConfig().getConfigurationSection("randomtp.range");
        int minX = range.getInt("1.x");
        int maxX = range.getInt("2.x");
        int minZ = range.getInt("1.z");
        int maxZ = range.getInt("2.z");

        int x=0, y=257, z=0;
        while(world.getBlockAt(x, y, z).isPassable())
        {
            x = new Random().nextInt(maxX-minX) + minX;
            z = new Random().nextInt(maxZ-minZ) + minZ;
            y = world.getHighestBlockYAt(x, z);
        }
        player.teleport(new Location(world, x, y+1, z));
        sender.sendMessage(ChatColor.GREEN + "You have been teleported to: " + ChatColor.GOLD +  x + ", " + y + ", " + z + ChatColor.GREEN + " in world " + ChatColor.GOLD + world.getName());

        // Add the player to the cooldown list
        cooldowns.put(playerUUID, System.currentTimeMillis());
        return true;
    }
}
