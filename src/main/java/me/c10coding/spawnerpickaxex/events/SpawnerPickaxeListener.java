package me.c10coding.spawnerpickaxex.events;

import me.c10coding.coreapi.chat.Chat;
import me.c10coding.spawnerpickaxex.SpawnerPickaxeX;
import me.c10coding.spawnerpickaxex.files.ConfigManager;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        String itemDisplayName = chatFactory.removeChatColor(itemInHand.getItemMeta().getDisplayName());
        String itemLoreLine = chatFactory.removeChatColor(im.getLore().get(0));
        ConfigManager cm = new ConfigManager(plugin);

        String configPickaxeName = chatFactory.removeChatColor(chatFactory.chat(cm.getPickaxeDisplayName()));
        String firstLoreLine = chatFactory.removeChatColor(chatFactory.chat(cm.getPickaxeLore().get(0)));

        Block b = e.getBlock();

        if(itemDisplayName.equalsIgnoreCase(configPickaxeName)){
            if(itemLoreLine.equalsIgnoreCase(firstLoreLine)) {
                if (b.getType().equals(Material.MOB_SPAWNER)) {
                    //Breaks the pickaxe
                    itemInHand.setDurability(Material.DIAMOND_PICKAXE.getMaxDurability());
                    CreatureSpawner cs = (CreatureSpawner) b.getState();
                    p.getInventory().addItem(getSpawner(cs.getSpawnedType()));
                } else {
                    e.setCancelled(true);
                    chatFactory.sendPlayerMessage("You cannot use this pickaxe on anything but spawners!", true, p, plugin.getPrefix());
                }
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
            String displayName = chatFactory.removeChatColor(spawner.getItemMeta().getDisplayName());
            if(displayName.equalsIgnoreCase("[Spawner]")) {
                String spawnerType = chatFactory.removeChatColor(spawner.getItemMeta().getLore().get(0)).toUpperCase();
                CreatureSpawner cs = (CreatureSpawner) spawnerBlock.getState();
                cs.setSpawnedType(EntityType.valueOf(spawnerType));
                cs.update(true);
                spawnerBlock.getWorld().spawnParticle(Particle.DRAGON_BREATH, spawnerBlock.getLocation(), 10);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void denyMobSpawnerChange(PlayerInteractEvent event) {
        if ((event.getClickedBlock() != null) && (event.getItem() != null) && (event.getClickedBlock().getType() == Material.MOB_SPAWNER) && (event.getItem().getType() == Material.MONSTER_EGG))
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
    }
    /*
    Upon mining the block, gives a spawner with a specific display name
     */
    public ItemStack getSpawner(EntityType spawnerType){
        ItemStack i = new ItemStack(Material.MOB_SPAWNER);

        List<String> lore = new ArrayList<>();
        lore.add(chatFactory.chat("&d&l" + spawnerType.getName()));

        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(chatFactory.chat("&d&o[Spawner]"));
        meta.setLore(lore);

        i.setItemMeta(meta);
        return i;
    }

}
