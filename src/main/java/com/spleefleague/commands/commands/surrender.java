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
import com.spleefleague.gameapi.GamePlugin;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public class surrender extends BasicCommand {

    public surrender(CorePlugin plugin, String name, String usage) {
        super(plugin, new surrenderDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void surrender(Player p) {
        if (GamePlugin.isIngameGlobal(p)) {
            GamePlugin.surrenderGlobal(p);
        } else {
            error(p, "You are currently not ingame!");
        }
    }
}
