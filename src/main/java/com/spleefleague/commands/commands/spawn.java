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
import com.spleefleague.core.spawn.SpawnManager.SpawnLocation;
import org.bukkit.Bukkit;

/**
 *
 * @author Jonas
 */
public class spawn extends BasicCommand {

    public spawn(CorePlugin plugin, String name, String usage) {
        super(plugin, new spawnDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void spawn(SLPlayer slp) {
        if (slp.getState() == PlayerState.INGAME) {
            error(slp, "You are currently ingame!");
        } else if (slp.getState() == PlayerState.SPECTATING) {
            GamePlugin.unspectateGlobal(slp);
        }
        SpawnLocation spawnLocation = SpleefLeague.getInstance().getSpawnManager().getNext();
        Bukkit.getScheduler().runTask(SpleefLeague.getInstance(), () -> slp.teleport(spawnLocation.getLocation()));
    }
}
