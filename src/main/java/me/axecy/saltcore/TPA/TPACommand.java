package me.axecy.saltcore.TPA;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPACommand implements CommandExecutor {

    private final TPAManager plugin;

    public TPACommand(TPAManager plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target != null) {
                    if (target.getWorld().getName().equals(plugin.getWorld())) {
                        if (!plugin.onCooldown(player)) {
                            plugin.sendTpaRequest(player, target);
                            plugin.setCooldown(player);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "That player is not in the TPA world.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /tpa <player>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }

}