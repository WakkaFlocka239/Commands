/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.PlayerState;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.gameapi.GameAPI;
import com.spleefleague.gameapi.player.GamePlayer;
import com.spleefleague.gameapi.queue.Challenge;
import java.util.UUID;

/**
 *
 * @author Jonas
 */
public class challenge extends BasicCommand {

    public challenge(CorePlugin plugin, String name, String usage) {
        super(plugin, new challengeDispatcher(), name, usage);
    }
    
    @Endpoint(target = {PLAYER})
    public void accept(SLPlayer slp, @LiteralArg(value = "accept") String l, @StringArg String uuidString) {
        handleChallenge(slp, uuidString, true);
    }
    
    @Endpoint(target = {PLAYER})
    public void decline(SLPlayer slp, @LiteralArg(value = "decline") String l, @StringArg String uuidString) {
        handleChallenge(slp, uuidString, false);
    }
    
    private void handleChallenge(SLPlayer slp, String challengeId, boolean accept) {
        if(slp.getState() == PlayerState.INGAME) {
            error(slp, "You are currently ingame!");
            return;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(challengeId);
        } catch(Exception e) {
            return;
        }
        GamePlayer gp = GameAPI.getInstance().getPlayerManager().get(slp);
        Challenge<?> challenge  = gp.getActiveChallenges().get(uuid);
        if (challenge != null) {
            if(accept) {
                challenge.accept(slp);
                success(slp, "You accepted the challenge.");
            }
            else {
                challenge.decline(slp);
                error(slp, "You declined the challenge.");
            }
        } else {
            error(slp, "This challenge has timed out!");
        }
    }
}
