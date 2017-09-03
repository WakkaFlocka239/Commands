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
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Jonas
 */
public class tphere extends BasicCommand {

    public tphere(CorePlugin plugin, String name, String usage) {
        super(plugin, new tphereDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint(target = {PLAYER})
    protected void run(Player p, @PlayerArg Player target) {
        target.teleport(p, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
