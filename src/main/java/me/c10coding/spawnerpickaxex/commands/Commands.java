package me.c10coding.spawnerpickaxex.commands;

import me.c10coding.coreapi.CoreAPI;
import me.c10coding.coreapi.chat.Chat;
import me.c10coding.spawnerpickaxex.SpawnerPickaxeX;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private SpawnerPickaxeX plugin;
    private CoreAPI api;
    private Chat chatFactory;

    public Commands(SpawnerPickaxeX plugin){
        this.plugin = plugin;
        api = plugin.getApi();
        chatFactory = api.getChatFactory();
        plugin.getCommand("spx").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args[0].equalsIgnoreCase("give") && args.length == 3){
                int amount = 1;
                String playerName = args[1];
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    chatFactory.sendPlayerMessage("The amount you gave was not a number!", true, p, plugin.getPrefix());
                    e.printStackTrace();
                    return false;
                }

                if(Bukkit.getPlayer(playerName) != null){
                    plugin.givePlayerPickaxe(playerName, amount);
                }else{
                    chatFactory.sendPlayerMessage("This is not a valid player!", true, p, plugin.getPrefix());
                }

            }
        }

        return false;
    }
}
