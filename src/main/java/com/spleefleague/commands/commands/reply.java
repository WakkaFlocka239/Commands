/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import java.util.UUID;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.DatabaseConnection;
import com.spleefleague.core.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class reply extends BasicCommand {

    public reply(CorePlugin plugin, String name, String usage) {
        super(plugin, new replyDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void reply(SLPlayer slp, @StringArg String[] messageArray) {
        UUID lastChatpartner = slp.getLastChatPartner();
        if (lastChatpartner != null) {
            SLPlayer target = SpleefLeague.getInstance().getPlayerManager().get(lastChatpartner);
            if (target != null) {
                String prefix1 = ChatColor.GRAY + "[me -> " + target.getRank().getColor() + target.getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
                String prefix2 = ChatColor.GRAY + "[" + slp.getRank().getColor() + slp.getName() + ChatColor.GRAY + " -> me] " + ChatColor.RESET;
                String message = StringUtil.fromArgsArray(messageArray);
                slp.sendMessage(prefix1 + message);
                target.sendMessage(prefix2 + message);
                target.setLastChatPartner(slp.getUniqueId());
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(SpleefLeague.getInstance(), () -> {
                    String username = DatabaseConnection.getUsername(lastChatpartner);
                    error(slp, username + " is not online!");
                });
            }
        } else {
            error(slp, "You don't have anyone to reply to!");
        }
    }
}
