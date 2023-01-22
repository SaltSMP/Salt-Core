package me.axecy.saltcore;

import me.axecy.saltcore.RandomTP.RTPCommand;
import me.axecy.saltcore.Spawn.SetSpawnCommand;
import me.axecy.saltcore.Spawn.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        this.getCommand("saltcore setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("saltcore reload").setExecutor(new ReloadConfigCommand(this));
        this.getCommand("rtp").setExecutor(new RTPCommand(this));
    }
}
