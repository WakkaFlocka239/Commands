/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.StringUtil;
import org.bukkit.ChatColor;

/**
 *
 * @author Jonas
 */
public class tell extends BasicCommand {

    public tell(CorePlugin plugin, String name, String usage) {
        super(plugin, new tellDispatcher(), name, usage);
    }

    @Endpoint
    public void tell(SLPlayer slp, @SLPlayerArg SLPlayer target, @StringArg String[] msg) {
        String prefix1 = ChatColor.GRAY + "[me -> " + target.getRank().getColor() + target.getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
        String prefix2 = ChatColor.GRAY + "[" + slp.getRank().getColor() + slp.getName() + ChatColor.GRAY + " -> me] " + ChatColor.RESET;
        String message = StringUtil.fromArgsArray(msg);
        slp.sendMessage(prefix1 + message);
        target.sendMessage(prefix2 + message);
        slp.setLastChatPartner(target.getUniqueId());
        target.setLastChatPartner(slp.getUniqueId());
    }
}
