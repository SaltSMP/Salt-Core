package me.axecy.saltcore.TPA;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TPAManager {

    private Map<UUID, UUID> tpaRequests;
    private Map<UUID, Long> cooldowns;

    private final Main plugin;
    public TPAManager(Main plugin) {
        this.plugin = plugin;
        tpaRequests = new HashMap<>();

        plugin.getCommand("tpa").setExecutor(new TPACommand(this));
        plugin.getCommand("tpacancel").setExecutor(new TPACancelCommand(this));
        plugin.getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
    }

    public boolean onCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        long timeLeft = (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
        if (timeLeft > 0) {
            player.sendMessage(ChatColor.RED + "You are on cooldown for " + ChatColor.GOLD + timeLeft + ChatColor.RED + " seconds.");
            return true;
        } else {
            cooldowns.remove(player.getUniqueId());
            return false;
        }
    }

    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (plugin.getConfig().getInt("cooldown") * 1000L));
    }

    public Map<UUID, UUID> getTpaRequests() {
        return tpaRequests;
    }

    public String getWorld() {
        return plugin.getConfig().getString("world");
    }

    public int getAcceptTime() {
        return plugin.getConfig().getInt("accept-time");
    }

    public void sendTpaRequest(Player sender, Player target) {
        tpaRequests.put(target.getUniqueId(), sender.getUniqueId());
        for (String s : plugin.getConfig().getStringList("messages.request.sender")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
        }
        for (String s : plugin.getConfig().getStringList("messages.request.target")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (tpaRequests.containsKey(target.getUniqueId())) {
                tpaRequests.remove(target.getUniqueId());
                sender.sendMessage("TPA request timed out.");
                target.sendMessage("TPA request timed out.");
            }
        }, getAcceptTime() * 20L);
    }



    public void cancelTpaRequest(Player sender, Player target) {
        tpaRequests.remove(target.getUniqueId());
        for (String s : plugin.getConfig().getStringList("messages.cancelled.sender")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
        }
        for (String s : plugin.getConfig().getStringList("messages.cancelled.target")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
        }
    }


    public void acceptTpaRequest(Player sender, Player target) {
        if (tpaRequests.containsKey(sender.getUniqueId()) && tpaRequests.get(sender.getUniqueId()).equals(target.getUniqueId())) {
            tpaRequests.remove(sender.getUniqueId());
            for (String s : plugin.getConfig().getStringList("messages.accepted.sender")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
            }
            for (String s : plugin.getConfig().getStringList("messages.accepted.target")) {
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
            }
            target.teleport(sender);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "There is no pending TPA request from that player."));
        }
    }


}