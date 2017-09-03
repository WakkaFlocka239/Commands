/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.chat.ChatChannel;
import com.spleefleague.core.chat.ChatManager;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.Config;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class staffchat extends BasicCommand {

    public staffchat(CorePlugin plugin, String name, String usage) {
        super(plugin, new staffchatDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint(target = {CONSOLE})
    public void staffchatConsole(CommandSender cs, @StringArg String[] args) {
        if (args.length > 0) {
            String message = StringUtil.fromArgsArray(args);
            String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Staff" + ChatColor.GRAY + "|" + ChatColor.DARK_PURPLE + Config.getString("server_name") + ChatColor.GRAY + "]";
            ChatManager.sendMessage(prefix, ChatColor.GRAY + "CONSOLE: " + ChatColor.GREEN + message, ChatChannel.STAFF);
        } else {
            sendUsage(cs);
        }
    }

    @Endpoint(target = {PLAYER})
    public void staffchatPlayer(SLPlayer slp, @StringArg String[] args) {
        if (args.length > 0) {
            String message = StringUtil.fromArgsArray(args);
            String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Staff" + ChatColor.GRAY + "|" + ChatColor.DARK_PURPLE + Config.getString("server_name") + ChatColor.GRAY + "]";
            ChatManager.sendMessage(prefix, slp.getRank().getColor() + slp.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + message, ChatChannel.STAFF);
        } else {
            sendUsage(slp);
        }
    }
}