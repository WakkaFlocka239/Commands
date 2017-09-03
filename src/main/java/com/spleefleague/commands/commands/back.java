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
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class back extends BasicCommand {

//    private HashMap<UUID, Location> lastLocations = new HashMap<>();

    public back(CorePlugin plugin, String name, String usage) {
        super(plugin, new backDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint(target = {PLAYER})
    public void disabled(CommandSender cs) {
        error(cs, "This command is currently disabled.");
    }

//    @Override
//    protected void run(Player p, SLPlayer slp, Command cmd, String[] args) {
//        if (!lastLocations.containsKey(slp.getUniqueId())) {
//            error(p, "There is no place to go back to.");
//        } else {
//            p.teleport(lastLocations.get(slp.getUniqueId()));
//        }
//    }
//
//    public void setLastTeleport(Player player, Location location) {
//        lastLocations.put(player.getUniqueId(), location);
//    }
}
