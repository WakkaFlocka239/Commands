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
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Checkpoint;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.TimeUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.bukkit.Location;

/**
 *
 * @author jonas
 */
public class setcheckpoint extends BasicCommand {

    public setcheckpoint(CorePlugin plugin, String name, String usage) {
        super(plugin, new setcheckpointDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void unset(@SLPlayerArg SLPlayer target, @LiteralArg(value = "unset", aliases = {"delete", "remove"}) String l) {
        target.setCheckpoint(null);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void setCurrent(@SLPlayerArg SLPlayer target) {
        setCurrentTimeout(target, null);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void setCurrentTimeout(@SLPlayerArg SLPlayer target, @StringArg String timeoutString) {
        Location l = target.getLocation();
        setTimeout(target, l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), timeoutString);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void set(@SLPlayerArg SLPlayer target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z, @DoubleArg double yaw, @DoubleArg double pitch) {
        setTimeout(target, x, y, z, yaw, pitch, null);
    }
    
    @Endpoint(target = {PLAYER, CONSOLE, COMMAND_BLOCK})
    public void setTimeout(@SLPlayerArg SLPlayer target, @DoubleArg double x, @DoubleArg double y, @DoubleArg double z, @DoubleArg double yaw, @DoubleArg double pitch, @StringArg String durationString) {
        Location targetLocation = new Location(target.getWorld(), x, y, z, (float)yaw, (float)pitch);
        Date timeoutDate = null;
        if(durationString != null) {
            Duration duration = TimeUtil.parseDurationString(durationString);
            Instant timeout = Instant.now().plus(duration);
            timeoutDate = Date.from(timeout);
        }
        target.setCheckpoint(new Checkpoint(targetLocation, timeoutDate));
    }
}
