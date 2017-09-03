/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class join extends BasicCommand {

    public join(CorePlugin plugin, String name, String usage) {
        super(plugin, new joinDispatcher(), name, usage);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SpleefLeague.getInstance(), "BungeeCord");
    }

    @Endpoint(target = {PLAYER})
    public void run(Player p, @StringArg String server) {
        PlayerUtil.sendToServer(p, server);
    }
}
