/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.Debugger;
import com.spleefleague.core.utils.debugger.DebuggerStartResult;
import com.spleefleague.core.utils.debugger.RuntimeCompiler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonas
 */
public class debug extends BasicCommand {

    public debug(CorePlugin plugin, String name, String usage) {
        super(plugin, new debugDispatcher(), name, usage, Rank.DEVELOPER);
    }

    @Endpoint
    public void list(CommandSender cs, @LiteralArg(value = "list") String l) {
        Map<String, Debugger> running = RuntimeCompiler.getRunningDebuggers();
        if (running.isEmpty()) {
            cs.sendMessage(ChatColor.RED + "No debuggers are running");
            return;
        }
        cs.sendMessage(ChatColor.GRAY + "Running debuggers:");
        for (String s : running.keySet()) {
            cs.sendMessage(ChatColor.GRAY + " - " + ChatColor.GREEN + s);
        }
    }

    @Endpoint(priority = 1)
    public void stop(CommandSender cs, @LiteralArg(value = "stop") String l, @StringArg String name) {
        String n = RuntimeCompiler.stopDebugger(name);
        if (n == null) {
            error(cs, "Debugger not found!");
        } else {
            success(cs, ChatColor.GRAY + "Successfully stopped debugger: " + ChatColor.GREEN + n);
        }
    }

    @Endpoint(priority = 0)
    public void explicitHost(CommandSender cs, @StringArg String hostArgument, @StringArg String key) {
        if (hostArgument.toLowerCase().startsWith("-host=") && hostArgument.length() > "-host=".length()) {
            String host = hostArgument.substring("-host=".length());
            Bukkit.getScheduler().runTaskAsynchronously(SpleefLeague.getInstance(), () -> {
                DebuggerStartResult result = RuntimeCompiler.debugFromHastebin(host, key, cs);
                if (result == null) {
                    error(cs, "Failed starting debugger!");
                    return;
                }
                result.informDebuggerStarted(cs);
            });
        } else {
            sendUsage(cs);
        }
    }

    @Endpoint
    public void cmd(CommandSender cs, @LiteralArg(value = "command", aliases = {"cmd"}) String l, @StringArg String name, @StringArg String[] args) {
        try {
            if (!RuntimeCompiler.runDebuggerCommand(name, cs, args)) {
                error(cs, "Debugger not found!");
            }
        } catch (Exception ex) {
            Logger.getLogger(debug.class.getName()).log(Level.SEVERE, null, ex);
            error(cs, "An error occurred!");
        }
    }
}
