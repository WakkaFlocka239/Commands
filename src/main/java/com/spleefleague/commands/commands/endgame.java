/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.PlayerState;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.plugin.GamePlugin;

/**
 *
 * @author Jonas
 */
public class endgame extends BasicCommand {

    public endgame(CorePlugin plugin, String name, String usage) {
        super(SpleefLeague.getInstance(), new endgameDispatcher(), name, usage);
    }
    
    

    @Endpoint(target = {PLAYER})
    public void delete(SLPlayer slp) {
        if (slp.getState() == PlayerState.INGAME) {
            GamePlugin.requestEndgameGlobal(slp.getPlayer());
        } else {
            error(slp, "You are not ingame!");
        }
    }
}
