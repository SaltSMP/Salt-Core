package me.axecy.saltcore.RandomTP;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RTPCommand extends JavaPlugin {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private int cooldownTime;

    private final Main plugin;
    public RTPCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cooldownTime = getConfig().getInt("randomtp.cooldown");
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
            long timePassed = currentTime - lastExecution;

            if (timePassed < (long) cooldownTime * 60 * 1000) {
                long timeLeft = ((long) cooldownTime * 60 * 1000 - timePassed) / 1000;
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + timeLeft + " seconds.");
                return true;
            }
        }
        // If the player is not on cooldown, or the cooldown has expired
        World world = Bukkit.getWorld(getConfig().getString("randomtp.world"));
        ConfigurationSection range = getConfig().getConfigurationSection("randomtp.range");
        int x = new Random().nextInt(range.getInt("2.x") - range.getInt("1.x")) + range.getInt("1.x");
        int z = new Random().nextInt(range.getInt("2.z") - range.getInt("1.z")) + range.getInt("1.z");
        int y = world.getHighestBlockYAt(x, z);
        if (!world.getBlockAt(x, y, z).isPassable()) {
            player.teleport(new Location(world, x, y, z));
            sender.sendMessage(ChatColor.GREEN + "You have been teleported to: " + ChatColor.GOLD +  x + ChatColor.GOLD + y + ChatColor.GOLD + z + ChatColor.GREEN + " in world" + ChatColor.GOLD + world);
        } else {
            player.sendMessage(ChatColor.RED + "The randomly generated location is not a solid block, please try again.");
        }

        // Add the player to the cooldown list
        cooldowns.put(playerUUID, System.currentTimeMillis());
        return true;
    }
}
