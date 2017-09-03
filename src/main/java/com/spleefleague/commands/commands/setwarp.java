package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import org.bukkit.entity.Player;

import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.Warp;

public class setwarp extends BasicCommand {

    public setwarp(CorePlugin plugin, String name, String usage) {
        super(plugin, new setwarpDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
    }

    @Endpoint(target = {PLAYER})
    protected void run(Player p, @StringArg String name) {
        Warp warp = new Warp(name, p.getLocation());
        Warp.addWarp(warp);
        success(p, "Warp '" + warp.getName() + "' saved");
    }
}
