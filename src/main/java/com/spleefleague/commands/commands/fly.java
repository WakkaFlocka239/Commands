/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.gameapi.GamePlugin;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class fly extends BasicCommand {

    public fly(CorePlugin plugin, String name, String usage) {
        super(plugin, new flyDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
    }

    @Endpoint(target = {PLAYER})
    public void run(Player p) {
        if (GamePlugin.isIngameGlobal(p) && !p.getAllowFlight()) {
            error(p, "You cannot fly during matches!");
            return;
        }
        p.setAllowFlight(!p.getAllowFlight());
        p.setFlying(p.getAllowFlight());
        success(p, "You are now " + (p.getAllowFlight() ? "able" : "unable") + " to fly!");
    }
}
