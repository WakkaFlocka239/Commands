package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.DoubleArg;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.entity.Player;

/**
 * Created by Josh on 07/02/2016.
 */
public class fspeed extends BasicCommand {

    public fspeed(CorePlugin plugin, String name, String usage) {
        super(plugin, new fspeedDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
    }

    @Endpoint(target = {PLAYER})
    public void fspeed(Player p, @DoubleArg(min = 1, max = 10) double speed) {
        p.setFlySpeed((float)speed / (float)10.0);
        success(p, "Flyspeed set to " + Math.round(speed) + "!");
    }

}
