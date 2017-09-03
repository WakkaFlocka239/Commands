/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands;

import com.spleefleague.commands.command.CommandLoader;
import com.spleefleague.commands.command.dynamic.DynamicCommandManager;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.ChatColor;

/**
 *
 * @author jonas
 */
public class Commands extends CorePlugin {
    
    private static Commands instance;
    private CommandLoader commandLoader;
    private DynamicCommandManager dynamicCommandManager;
    
    public Commands() {
        super(ChatColor.GRAY + "[" + ChatColor.GOLD + "SpleefLeague" + ChatColor.GRAY + "]" + ChatColor.RESET);
    }
    
    @Override
    public void start() {
        instance = this;
        this.commandLoader = CommandLoader.loadCommands(this, "com.spleefleague.commands.commands");
        this.dynamicCommandManager = new DynamicCommandManager(this);
    }
    
    public static Commands getInstance() {
        return instance;
    }

    /**
     * @return the commandLoader
     */
    public CommandLoader getCommandLoader() {
        return commandLoader;
    }

    /**
     * @return the dynamicCommandManager
     */
    public DynamicCommandManager getDynamicCommandManager() {
        return dynamicCommandManager;
    }
}
