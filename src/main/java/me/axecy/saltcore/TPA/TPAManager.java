package me.axecy.saltcore.TPA;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TPAManager extends JavaPlugin {

    private Map<UUID, UUID> tpaRequests;
    private Map<UUID, Long> cooldowns;
    private FileConfiguration config;

    public void onEnable() {
        tpaRequests = new HashMap<>();
        cooldowns = new HashMap<>();
        config = getConfig();
        saveDefaultConfig();

        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("tpacancel").setExecutor(new TPACancelCommand(this));
        getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
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
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (config.getInt("cooldown") * 1000L));
    }

    public Map<UUID, UUID> getTpaRequests() {
        return tpaRequests;
    }

    public String getWorld() {
        return config.getString("world");
    }

    public int getAcceptTime() {
        return config.getInt("accept-time");
    }

    public void sendTpaRequest(Player sender, Player target) {
        tpaRequests.put(target.getUniqueId(), sender.getUniqueId());
        for (String s : config.getStringList("messages.request.sender")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
        }
        for (String s : config.getStringList("messages.request.target")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
        }
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (tpaRequests.containsKey(target.getUniqueId())) {
                tpaRequests.remove(target.getUniqueId());
                sender.sendMessage("TPA request timed out.");
                target.sendMessage("TPA request timed out.");
            }
        }, getAcceptTime() * 20L);
    }



    public void cancelTpaRequest(Player sender, Player target) {
        tpaRequests.remove(target.getUniqueId());
        for (String s : config.getStringList("messages.cancelled.sender")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
        }
        for (String s : config.getStringList("messages.cancelled.target")) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
        }
    }


    public void acceptTpaRequest(Player sender, Player target) {
        if (tpaRequests.containsKey(sender.getUniqueId()) && tpaRequests.get(sender.getUniqueId()).equals(target.getUniqueId())) {
            tpaRequests.remove(sender.getUniqueId());
            for (String s : config.getStringList("messages.accepted.sender")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<target>", target.getName())));
            }
            for (String s : config.getStringList("messages.accepted.target")) {
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("<sender>", sender.getName())));
            }
            target.teleport(sender);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "There is no pending TPA request from that player."));
        }
    }


}