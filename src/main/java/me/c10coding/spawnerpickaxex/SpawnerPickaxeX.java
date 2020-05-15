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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

public final class SpawnerPickaxeX extends JavaPlugin {

    @Inject private CoreAPI api = new CoreAPI();
    private final String PREFIX = api.getChatFactory().chat("&l[&b&l" + this.getName() + "&r&l]");
    private ConfigManager configManager;
    private Logger pluginLogger = this.getLogger();
    public static EmptyEnchant enchant;

    @Override
    public void onEnable() {
        // Plugin startup logic
        me.c10coding.coreapi.binder.Binder binder = new Binder(this);
        Injector injector = binder.createInjector();
        injector.injectMembers(this);
        configManager = new ConfigManager(this);

        File configFile = new File(this.getDataFolder(), "config.yml");

        if(!configFile.exists()){
            this.saveResource("config.yml", false);
        }

        enchant = new EmptyEnchant();
        registerEnchantment(enchant);

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

        itemMeta.addEnchant(enchant, 1, false);
        //Uses chat factory to translate the color codes into actual colors
        itemMeta.setDisplayName(chatFactory.chat(configManager.getPickaxeDisplayName()));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        player.getInventory().addItem(item);

    }

    public void registerEnchantment(Enchantment ench) {
        boolean registered = true;
        //Using Reflection
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(ench);
            pluginLogger.info("Registering enchant...");
        }catch(Exception e) {
            pluginLogger.info("Enchantment is already registered. Ignoring...");
        }
        /*
        if(registered) {
            Bukkit.broadcastMessage(ench.getName());
        }*/
    }



}
