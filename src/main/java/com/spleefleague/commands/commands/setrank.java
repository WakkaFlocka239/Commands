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
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.DatabaseConnection;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class setrank extends BasicCommand {

    public setrank(CorePlugin plugin, String name, String usage) {
        super(plugin, new setrankDispatcher(), name, usage, Rank.DEVELOPER);
    }

    
    @Endpoint(priority = 0)
    public void setrankOffline(CommandSender cs, @SLPlayerArg(offline = true) SLPlayer target, @StringArg String rankName) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        if(rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        setRankOffline(target, rank);
        success(cs, "Rank has been set.");
    }
    
    @Endpoint(priority = 1)
    public void setrankOnline(CommandSender cs, @SLPlayerArg(exact = true) SLPlayer target, @StringArg String rankName) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        
        if(rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        target.setRank(rank);
        target.setEternalRank(rank);
        target.setRankExpirationTime(0);
        success(cs, "Rank has been set.");
    }

    private void setRankOffline(SLPlayer player, Rank rank) {
       DatabaseConnection.updateFields(SpleefLeague.getInstance().getPluginDB().getCollection("Players"),
            new Document("uuid", player.getUniqueId()),
            Pair.<String, Object>of("eternalRank", rank.getName()),
            Pair.<String, Object>of("rank", rank.getName()),
            Pair.<String, Object>of("rankExpirationTime", 0)
        );
    }
}
