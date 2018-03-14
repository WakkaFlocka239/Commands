/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.COMMAND_BLOCK;
import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.DoubleArg;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.ServerType;
import org.bukkit.Location;
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
    public void tpposSelf(Player target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z) {
        tpposOther(target, x, y, z);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void tpposOther(@PlayerArg Player target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z) {
        tpposOtherRotation(target, x, y, z, target.getLocation().getYaw(), target.getLocation().getPitch());
    }

    @Endpoint(target = {PLAYER})
    public void tpposSelfRotation(Player target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z, @DoubleArg double yaw, @DoubleArg double pitch) {
        tpposOtherRotation(target, x, y, z, pitch, yaw);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void tpposOtherRotation(@PlayerArg Player target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z, @DoubleArg double yaw, @DoubleArg double pitch) {
        Location loc = new Location(target.getWorld(), x, y, z, (float)yaw, (float)pitch);
        target.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
