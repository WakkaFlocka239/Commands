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
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class points extends BasicCommand {

    public points(CorePlugin plugin, String name, String usage) {
        super(plugin, new pointsDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void pointsSelf(Player p) {
        pointsOther(p, p);
    }
    
    @Endpoint(target = {PLAYER})
    public void pointsOther(Player p, @PlayerArg Player target) {
        GamePlugin.printAllStats(p, target);
    }
}
