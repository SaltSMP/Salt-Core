package me.axecy.saltcore.ProtectHub;

import me.axecy.saltcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HubProtection implements Listener {

    private final Main plugin;

    public HubProtection(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        String hubWorld = plugin.getConfig().getString("protecthub.world");
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(hubWorld)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot break blocks in the hub world!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String hubWorld = plugin.getConfig().getString("protecthub.world");
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(hubWorld)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place blocks in the hub world!");
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        String hubWorld = plugin.getConfig().getString("protecthub.world");
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(hubWorld)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop items in the hub world!");
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        String hubWorld = plugin.getConfig().getString("protecthub.world");
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getWorld().getName().equalsIgnoreCase(hubWorld)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot pick up items in the hub world!");
            }
        }
    }


    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        String hubWorld = plugin.getConfig().getString("protecthub.world");
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getWorld().getName().equalsIgnoreCase(hubWorld)) {
                event.setCancelled(true);
                player.setFoodLevel(20);
            }
        }
    }
}
