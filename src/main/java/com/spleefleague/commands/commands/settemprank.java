package com.spleefleague.commands.commands;

import com.mongodb.client.MongoCollection;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.DatabaseConnection;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Jonas
 */
public class settemprank extends BasicCommand {

    public settemprank(CorePlugin plugin, String name, String usage) {
        super(plugin, new settemprankDispatcher(), name, usage, Rank.DEVELOPER);
    }
    
    @Endpoint
    public void setrankOnline(CommandSender cs, @SLPlayerArg(exact = true) SLPlayer target, @StringArg String rankName, @IntArg(min = 1) int hours) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        if (rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        long endingTime = System.currentTimeMillis() + hours * 60 * 60 * 1000l;
        target.setExpiringRank(rank, endingTime);
        success(cs, "Temporary rank has been set.");
    }

    @Endpoint
    public void setrankOffline(CommandSender cs, @SLPlayerArg(exact = true) SLPlayer target, @StringArg String rankName, @IntArg(min = 1) int hours) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        if (rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        long endingTime = System.currentTimeMillis() + hours * 60 * 60 * 1000;
        setRankOffline(target, rank, endingTime);
        success(cs, "Temporary rank has been set.");
    }

    private void setRankOffline(SLPlayer player, Rank rank, long endingTime) {
        MongoCollection<Document> collection = SpleefLeague.getInstance().getPluginDB().getCollection("Players");
        Document index = new Document("uuid", player.getUniqueId());
        DatabaseConnection.updateFields(collection,
                index,
                Pair.<String, Object>of("rank", rank.getName()),
                Pair.<String, Object>of("rankExpirationTime", endingTime)
        );
    }
}
