package me.c10coding.spawnerpickaxex.files;

import me.c10coding.spawnerpickaxex.SpawnerPickaxeX;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private SpawnerPickaxeX plugin;
    private FileConfiguration config;

    public ConfigManager(SpawnerPickaxeX plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getPickaxeDisplayName(){
        return config.getString("PickaxeName");
    }

    public List<String> getPickaxeLore(){
        return config.getStringList("Lore");
    }


}
