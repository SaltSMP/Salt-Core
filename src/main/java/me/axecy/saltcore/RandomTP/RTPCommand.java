package me.axecy.saltcore.RandomTP;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RTPCommand extends JavaPlugin {

    private final Main plugin;
    public RTPCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        World world = Bukkit.getWorld(getConfig().getString("randomtp.world"));
        ConfigurationSection range = getConfig().getConfigurationSection("randomtp.range");
        int x = new Random().nextInt(range.getInt("2.x") - range.getInt("1.x")) + range.getInt("1.x");
        int z = new Random().nextInt(range.getInt("2.z") - range.getInt("1.z")) + range.getInt("1.z");
        int y = world.getHighestBlockYAt(x, z);
        if (!world.getBlockAt(x, y, z).isPassable()) {
            player.teleport(new Location(world, x, y, z));
        } else {
            player.sendMessage("The randomly generated location is not a solid block, please try again.");
        }

        return true;
    }
}
