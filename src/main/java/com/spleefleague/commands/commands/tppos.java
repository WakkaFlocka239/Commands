/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.COMMAND_BLOCK;
import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Jonas
 */
public class tppos extends BasicCommand {

    public tppos(CorePlugin plugin, String name, String usage) {
        super(plugin, new tpposDispatcher(), name, usage, Rank.MODERATOR);
        setAdditionalRanksDependingOnServerType(ServerType.BUILD, Rank.BUILDER);
    }

    @Endpoint(target = {PLAYER})
    public void tpposSelf(Player target, @IntArg int x, @IntArg int y, @IntArg int z) {
        tpposOther(target, x, y, z);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void tpposOther(@PlayerArg Player target, @IntArg int x, @IntArg int y, @IntArg int z) {
        Location loc = new Location(target.getWorld(), x, y, z);
        target.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
