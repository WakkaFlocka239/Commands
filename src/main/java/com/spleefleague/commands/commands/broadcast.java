package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.COMMAND_BLOCK;
import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

/**
 * Created by Josh on 11/08/2016.
 */
public class broadcast extends BasicCommand {

    public broadcast(CorePlugin plugin, String name, String usage) {
        super(plugin, new broadcastDispatcher(), name, usage, Rank.DEVELOPER);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE})
    public void broadcast(@StringArg String[] messageArray) {
        String message = ChatColor.translateAlternateColorCodes('&', StringUtil.fromArgsArray(messageArray));
        if(SpleefLeague.getInstance().getConnectionClient().isEnabled()) {
            JSONObject send = new JSONObject();
            send.put("message", message);
            SpleefLeague.getInstance().getConnectionClient().send("broadcast", send);
        }
        else {
            Bukkit.broadcastMessage(String.format(SpleefLeague.BROADCAST_FORMAT, message));
        }
    }
    
    @Endpoint(priority = 1, target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void broadcastLocal(@LiteralArg(value = "local") String l, @StringArg String[] messageArray) {
        String message = ChatColor.translateAlternateColorCodes('&', StringUtil.fromArgsArray(messageArray));
        Bukkit.broadcastMessage(String.format(SpleefLeague.BROADCAST_FORMAT, message));
    }
}
