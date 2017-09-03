/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.io.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class setmax extends BasicCommand {

    public setmax(CorePlugin plugin, String name, String usage) {
        super(plugin, new setmaxDispatcher(), name, usage, Rank.DEVELOPER);
    }

    @Endpoint
    public void setMax(CommandSender cs, @IntArg(min = 1, max = 1000) int slots) {
        SpleefLeague.getInstance().setSlotSize(slots);
                Settings.set("max_players", slots);
                success(cs, "Slot size has been changed to " + slots + "!");
    }
}
