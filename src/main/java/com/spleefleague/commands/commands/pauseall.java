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
import com.spleefleague.gameapi.GamePlugin;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class pauseall extends BasicCommand {

    public pauseall(CorePlugin plugin, String name, String usage) {
        super(plugin, new pauseallDispatcher(), name, usage, Rank.SENIOR_MODERATOR);
    }
    
    @Endpoint
    public void pauseall(CommandSender cs) {
        GamePlugin.setQueueStatusGlobal(false);
        success(cs, "All queues have been paused!");
    }
}
