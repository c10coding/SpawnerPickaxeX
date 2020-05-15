package me.c10coding.spawnerpickaxex.commands;

import me.c10coding.coreapi.CoreAPI;
import me.c10coding.coreapi.chat.Chat;
import me.c10coding.spawnerpickaxex.SpawnerPickaxeX;
import me.c10coding.spawnerpickaxex.files.ConfigManager;
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
        Bukkit.getPluginCommand("spawnerpickaxex").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("give") && args.length == 3){
            if(sender.hasPermission("spx.give")){

                int amount;
                String playerName = args[1];

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    chatFactory.sendPlayerMessage("The amount you gave was not a number!", true, sender, plugin.getPrefix());
                    return false;
                }

                if(Bukkit.getPlayer(playerName) != null){
                    Player player = Bukkit.getPlayer(playerName);
                    plugin.givePlayerPickaxe(playerName, amount);
                    /*
                    If the player isn't themselves, then give them feedback on if the command worked
                     */
                    if(sender instanceof Player){
                        Player senderP = (Player) sender;
                        if(!senderP.equals(player)){
                            chatFactory.sendPlayerMessage("You have given &a&l" + player.getName() + " &ra SpawnerPickaxe", true, sender, plugin.getPrefix());
                        }
                    }
                    chatFactory.sendPlayerMessage("You have been given a SpawnerPickaxe!", true, player, plugin.getPrefix());
                }else{
                    chatFactory.sendPlayerMessage("This is not a valid player!", true, sender, plugin.getPrefix());
                }

            }else{
                chatFactory.sendPlayerMessage("You aren't allowed to do that!", false, sender, null);
            }
        }
        return false;
    }
}
