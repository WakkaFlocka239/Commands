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
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.packets.ArtificialThrottleAdapter;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import java.time.Duration;
import org.bukkit.command.CommandSender;

/**
 *
 * @author jonas
 */
public class setping extends BasicCommand {

    private final ArtificialThrottleAdapter throttleAdapter;
    
    public setping(CorePlugin plugin, String name, String usage) {
        super(plugin, new setpingDispatcher(), name, usage, Rank.SENIOR_MODERATOR);
        throttleAdapter = new ArtificialThrottleAdapter();
        SpleefLeague.getInstance().getProtocolManager().addPacketListener(throttleAdapter);
    }
    
    @Endpoint(target = {CONSOLE, PLAYER, COMMAND_BLOCK})
    public void setping(CommandSender sender, @SLPlayerArg SLPlayer target, @IntArg(min = 0) int aimed) {
        Duration toDecrease = Duration.ofMillis(target.getAimedPing() - aimed);
        throttleAdapter.decreaseDelay(target.getPlayer(), toDecrease);
        target.setAimedPing(aimed);
        success(sender, "The ping has been set.");
    }
}
