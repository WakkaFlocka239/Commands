/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.PlayerState;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.queue.Challenge;

/**
 *
 * @author Jonas
 */
public class challenge extends BasicCommand {

    public challenge(CorePlugin plugin, String name, String usage) {
        super(plugin, new challengeDispatcher(), name, usage);
    }
    
    @Endpoint(target = {PLAYER})
    public void accept(SLPlayer slp, @LiteralArg(value = "accept") String l, @SLPlayerArg SLPlayer target) {
        if(slp.getState() == PlayerState.INGAME) {
            error(slp, "You are currently ingame!");
            return;
        }
        if (target.getState() == PlayerState.INGAME) {
            error(slp, "The player is already in a game");
            return;
        }
        Challenge challenge = slp.getChallenge(target);
        if (challenge != null) {
            challenge.accept(slp);
            success(slp, "You accepted the challenge!");
        } else {
            error(slp, "You have no open challenges by " + target.getName() + "!");
        }
    }
    
    @Endpoint(target = {PLAYER})
    public void decline(SLPlayer slp, @LiteralArg(value = "decline") String l, @SLPlayerArg SLPlayer target) {
        if(slp.getState() == PlayerState.INGAME) {
            error(slp, "You are currently ingame!");
            return;
        }
        Challenge challenge = slp.getChallenge(target);
        if (challenge != null) {
            challenge.decline(slp);
            error(slp, "You have declined the challenge"); //Not an actual error, just for the color
        } else {
            error(slp, "You have no open challenges by " + target.getName() + "!");
        }
    }
}
