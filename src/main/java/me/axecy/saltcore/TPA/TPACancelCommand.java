package me.axecy.saltcore.TPA;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPACancelCommand implements CommandExecutor {

    private final TPAManager plugin;

    public TPACancelCommand(TPAManager plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target != null) {
                    if (plugin.getTpaRequests().containsKey(target.getUniqueId()) && plugin.getTpaRequests().get(target.getUniqueId()).equals(player.getUniqueId())) {
                        plugin.cancelTpaRequest(player, target);
                    } else {
                        player.sendMessage(ChatColor.RED + "You have not sent a TPA request to that player.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /tpacancel <player>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }

}
