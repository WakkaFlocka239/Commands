/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.plugin.GamePlugin;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class leave extends BasicCommand {

    public leave(CorePlugin plugin, String name, String usage) {
        super(plugin, new leaveDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void leave(Player p) {
        if (GamePlugin.isQueuedGlobal(p)) {
            GamePlugin.dequeueGlobal(p);
            success(p, "You have successfully been removed from the queue.");
        } else {
            error(p, "You are currently not queued.");
        }
    }
}
