package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import org.bukkit.entity.Player;

import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.Warp;

public class deletewarp extends BasicCommand {

    public deletewarp(CorePlugin plugin, String name, String usage) {
        super(plugin, new deletewarpDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
    }

    @Endpoint(target = {PLAYER})
    public void delete(Player p, @StringArg String name) {
        Warp warp = Warp.byName(name);
        if (warp != null) {
            Warp.removeWarp(warp);
            success(p, "Warp '" + warp.getName() + "' deleted");
        } else {
            error(p, "The specified warp does not exist");
        }
    }
}
