package me.c10coding.spawnerpickaxex;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.c10coding.coreapi.CoreAPI;
import me.c10coding.coreapi.binder.Binder;
import me.c10coding.coreapi.chat.Chat;
import me.c10coding.spawnerpickaxex.commands.Commands;
import me.c10coding.spawnerpickaxex.events.SpawnerPickaxeListener;
import me.c10coding.spawnerpickaxex.files.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SpawnerPickaxeX extends JavaPlugin {

    @Inject private CoreAPI api = new CoreAPI();
    private final String PREFIX = api.getChatFactory().chat("&l[&b&l" + this.getName() + "&r&l]");
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        me.c10coding.coreapi.binder.Binder binder = new Binder(this);
        Injector injector = binder.createInjector();
        injector.injectMembers(this);
        configManager = new ConfigManager(this);

        new Commands(this);
        new SpawnerPickaxeListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public CoreAPI getApi(){
        return api;
    }

    public String getPrefix(){
        return PREFIX;
    }

    /*
        Gives the player a Spawner Pickaxe with a custom display name and lore
     */
    public void givePlayerPickaxe(String playerName, int amount){

        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, amount);
        ItemMeta itemMeta = item.getItemMeta();
        Player player = Bukkit.getPlayer(playerName);
        Chat chatFactory = api.getChatFactory();
        List<String> lore = configManager.getPickaxeLore();

        for(int x = 0; x < lore.size(); x++){
            lore.set(x, chatFactory.chat(lore.get(x)));
        }

        //Uses chat factory to translate the color codes into actual colors
        itemMeta.setDisplayName(chatFactory.chat(configManager.getPickaxeDisplayName()));
        itemMeta.setLore(configManager.getPickaxeLore());
        item.setItemMeta(itemMeta);

        player.getInventory().addItem(item);

    }


}
