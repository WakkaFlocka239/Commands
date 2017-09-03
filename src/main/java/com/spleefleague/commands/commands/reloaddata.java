/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.Settings;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class reloaddata extends BasicCommand {

    public reloaddata(CorePlugin plugin, String name, String usage) {
        super(plugin, new reloaddataDispatcher(), name, usage, Rank.DEVELOPER);
    }

    @Endpoint
    public void reloadRanks(CommandSender cs, @LiteralArg(value = "ranks") String l) {
        Rank.init();
        for (SLPlayer slp : SpleefLeague.getInstance().getPlayerManager().getAll()) {
            slp.setRank(Rank.valueOf(slp.getRank().getName()));
        }
        success(cs, "Reloaded " + Rank.values().length + " ranks!");
    }

    @Endpoint
    public void reloadSettings(CommandSender cs, @LiteralArg(value = "settings") String l) {
        Settings.loadSettings();
        Settings.getList("debugger_hosts").ifPresent(SpleefLeague.getInstance().getDebuggerHostManager()::reloadAll);
        success(cs, "Reloaded settings from the database!");
    }
}
