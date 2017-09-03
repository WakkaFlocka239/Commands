/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.plugin.GamePlugin;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class cancelall extends BasicCommand {

    public cancelall(CorePlugin plugin, String name, String usage) {
        super(plugin, new cancelallDispatcher(), name, usage, Rank.SENIOR_MODERATOR);
    }
    @Endpoint
    public void cancelAll(CommandSender cs) {
        GamePlugin.cancelAllMatches();
        success(cs, "All games have been cancelled.");
    }
}
