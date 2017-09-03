/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.CommandSource;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.plugin.GamePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class cancel extends BasicCommand {

    public cancel(CorePlugin plugin, String name, String usage) {
        super(plugin, new cancelDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint
    public void cancel(CommandSender cs, @SLPlayerArg(exact = true) SLPlayer tp) {
        if (GamePlugin.isIngameGlobal(tp)) {
            GamePlugin.cancelGlobal(tp);
            success(cs, "The battle will be cancelled.");
        } else {
            error(cs, "The player " + ChatColor.WHITE + tp.getName() + ChatColor.RED + " is not playing!");
        }
    }
}
