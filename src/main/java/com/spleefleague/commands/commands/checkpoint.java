/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Checkpoint;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.gameapi.GamePlugin;

/**
 *
 * @author jonas
 */
public class checkpoint extends BasicCommand {

    public checkpoint(CorePlugin plugin, String name, String usage) {
        super(plugin, new checkpointDispatcher(), name, usage);
    }
    
    @Endpoint(target = {PLAYER})
    public void checkpoint(SLPlayer target) {
        Checkpoint checkpoint = target.getCheckpoint();
        if(checkpoint == null) {
            error(target, "You don't have a checkpoint set.");
            return;
        }
        if(GamePlugin.isIngameGlobal(target)) {
            error(target, "You cannot teleport while you're ingame.");
            return;
        }
        GamePlugin.unspectateGlobal(target);
        target.teleport(checkpoint.getTarget());
    }
}
