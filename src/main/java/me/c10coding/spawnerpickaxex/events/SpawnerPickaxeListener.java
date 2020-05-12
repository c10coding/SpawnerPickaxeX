package me.c10coding.spawnerpickaxex.events;

import me.c10coding.coreapi.chat.Chat;
import me.c10coding.spawnerpickaxex.SpawnerPickaxeX;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerPickaxeListener implements Listener {

    private SpawnerPickaxeX plugin;
    private Chat chatFactory;

    public SpawnerPickaxeListener(SpawnerPickaxeX plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getApi().getChatFactory();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        ItemStack itemInHand = p.getItemInHand();
        ItemMeta im = itemInHand.getItemMeta();
        Block b = e.getBlock();
        if(im.getDisplayName().equalsIgnoreCase("&bSpawner Pickaxe")){
            if(im.getLore().get(0).equalsIgnoreCase("&6&lOne use")){
                if(b.getType().equals(Material.MOB_SPAWNER)){
                    e.setCancelled(true);
                    //Breaks the pickaxe
                    itemInHand.setDurability(Material.DIAMOND_PICKAXE.getMaxDurability());
                    CreatureSpawner cs = (CreatureSpawner) b.getState();
                    p.getInventory().addItem(getSpawner(cs.getSpawnedType()));
                }
            }else{
                chatFactory.sendPlayerMessage("You cannot use this pickaxe on anything but spawners!", true, p, plugin.getPrefix());
            }
        }
    }

    @EventHandler
    public void onPlaceSpawner(BlockPlaceEvent e){
        /*
            Sets the spawner type to whatever was in the display name
         */
        if(e.getBlock().getType().equals(Material.MOB_SPAWNER)){

            Block spawnerBlock = e.getBlock();
            ItemStack spawner = e.getItemInHand();
            String displayName = spawner.getItemMeta().getDisplayName();
            if(chatFactory.removeChatColor(displayName).equalsIgnoreCase("SpawnerPickaxeX Spawner")) {
                String spawnerType = getSpawnerType(spawner.getItemMeta().getDisplayName());
                CreatureSpawner cs = (CreatureSpawner) spawnerBlock;
                cs.setSpawnedType(EntityType.valueOf(spawnerType));
                spawnerBlock.getWorld().spawnParticle(Particle.SPELL_INSTANT, spawnerBlock.getLocation(), 10);
            }

        }
    }

    /*
    Upon mining the block, gives a spawner with a specific display name
     */
    public ItemStack getSpawner(EntityType spawnerType){
        ItemStack i = new ItemStack(Material.MOB_SPAWNER);

        List<String> lore = new ArrayList<>();
        lore.add(chatFactory.chat("&d&l" + spawnerType.getName()));

        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(chatFactory.chat("&o" + "SpawnerPickaxeX Spawner"));
        meta.setLore(lore);

        i.setItemMeta(meta);
        return i;
    }

    public String getSpawnerType(String displayName){
        return ChatColor.stripColor(displayName).replace("[", "").replace("]", "");
    }

}
