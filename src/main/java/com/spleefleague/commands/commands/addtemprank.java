package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.player.TemporaryRank;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class addtemprank extends BasicCommand {

    public addtemprank(CorePlugin plugin, String name, String usage) {
        super(plugin, new addtemprankDispatcher(), name, usage, Rank.DEVELOPER);
    }
    
    @Endpoint(priority = 1)
    public void addTempRankOnline(CommandSender cs, @SLPlayerArg(exact = true) SLPlayer target, @StringArg String rankName, @IntArg(min = 1) int hours) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        if (rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        long endingTime = System.currentTimeMillis() + hours * 60 * 60 * 1000;
        TemporaryRank tempRank = new TemporaryRank(rank, endingTime);
        target.addTemporaryRank(tempRank);
        success(cs, "Temporary rank has been added.");
    }

    @Endpoint(priority = 0)
    public void addTempRankOffline(CommandSender cs, @SLPlayerArg(offline = true, exact = true) SLPlayer target, @StringArg String rankName, @IntArg(min = 1) int hours) {
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        if (rank == null) {
            error(cs, "The rank " + rankName + " does not exist!");
            return;
        }
        long endingTime = System.currentTimeMillis() + hours * 60 * 60 * 1000;
        TemporaryRank tempRank = new TemporaryRank(rank, endingTime);
        addTempRankOffline(target, tempRank);
        success(cs, "Temporary rank has been added.");
    }

    private void addTempRankOffline(SLPlayer player, TemporaryRank tempRank) {
        player.addTemporaryRank(tempRank);
        SpleefLeague.getInstance().getPlayerManager().save(player);
    }
}
