/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.gameapi.GamePlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class spectate extends BasicCommand {

    public spectate(CorePlugin plugin, String name, String usage) {
        super(plugin, new spectateDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void unspectate(Player p) {
        if (GamePlugin.isSpectatingGlobal(p)) {
            GamePlugin.unspectateGlobal(p);
            success(p, "You are no longer spectating a match");
            if (p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        } else {
            error(p, "You are currently not spectating anyone!");
        }
    }

    @Endpoint(target = {PLAYER})
    public void spectate(Player p, @PlayerArg Player target) {
        if (!GamePlugin.isIngameGlobal(p)) {
            for (GamePlugin gp : GamePlugin.getGamePlugins()) {
                if (gp.isIngame(target)) {
                    if (GamePlugin.isSpectatingGlobal(p)) {
                        GamePlugin.unspectateGlobal(p);
                    }
                    if (gp.spectateGracefully(target, p)) {
                        success(p, "You are now spectating " + target.getName());
                        return;
                    }
                }
            }
            error(p, target.getName() + " is not ingame.");
        } else {
            error(p, "You are currently ingame!");
        }
    }
}
