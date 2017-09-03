/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.command;

import com.google.common.collect.Sets;
import com.spleefleague.annotations.CommandSource;
import com.spleefleague.annotations.DispatchResult;
import com.spleefleague.annotations.DispatchResultType;
import com.spleefleague.annotations.DispatchableCommand;
import com.spleefleague.annotations.Dispatcher;
import java.util.regex.Pattern;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.chat.Theme;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.ServerType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jonas
 */
public abstract class BasicCommand extends DispatchableCommand implements CommandExecutor {

    protected CorePlugin plugin;
    protected String name;
    protected Rank requiredRank;
    protected final Set<Rank> additionalRanks;
    protected final Map<ServerType, Set<Rank>> additionalRanksPerServerTypes = new HashMap<>();
    protected boolean hasCommandBlockExecutor = false;
    private String[] usages = null;
    public static final String NO_COMMAND_PERMISSION_MESSAGE = "You don't have permission to use this command!";
    public static final String PLAYERDATA_ERROR_MESSAGE = "Your player data hasn't yet been loaded. Please try again.";
    public static final String NO_PLAYER_INSTANCE = Theme.WARNING.buildTheme(false) + "This command can only be run by an instance of a player.";

    public BasicCommand(CorePlugin plugin, Dispatcher dispatcher, String name, String usage) {
        this(plugin, dispatcher, name, usage, Rank.DEFAULT);
    }

    public BasicCommand(CorePlugin plugin, Dispatcher dispatcher, String name, String usage, Rank requiredRank, Rank... additionalRanks) {
        super(dispatcher);
        this.plugin = plugin;
        this.name = name;
        this.requiredRank = requiredRank;
        this.additionalRanks = Sets.newHashSet(additionalRanks);
        usage = usage.replaceAll(Pattern.quote("<command>"), name);
        this.usages = StringUtils.split(usage, "\n");
        plugin.getCommand(name).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        try {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                SLPlayer slp = SpleefLeague.getInstance().getPlayerManager().get(p);
                if (slp != null) {
                    boolean hasPermissions = false;
                    Rank rank = slp.getRank();
                    if(rank != null) {
                        hasPermissions = rank.hasPermission(requiredRank);
                        hasPermissions |= additionalRanks.contains(rank);
                        Set<Rank> additionalRanksForCurrentServerType = additionalRanksPerServerTypes.get(SpleefLeague.getInstance().getServerType());
                        if(additionalRanksForCurrentServerType != null)
                            hasPermissions |= additionalRanksForCurrentServerType.contains(rank);
                    }
                    if (hasPermissions) {
                        performRun(CommandSource.PLAYER, slp, args);
                    } else {
                        error(sender, NO_COMMAND_PERMISSION_MESSAGE);
                    }
                } else {
                    error(sender, PLAYERDATA_ERROR_MESSAGE);
                }
            } else if (sender instanceof ConsoleCommandSender) {
                performRun(CommandSource.CONSOLE, sender, args);
            } else if (sender instanceof BlockCommandSender) {
                if (hasCommandBlockExecutor) {
                    performRun(CommandSource.COMMAND_BLOCK, sender, args);
                } else {
                    performRun(CommandSource.CONSOLE, sender, args);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public String getName() {
        return name;
    }
    
    protected void setAdditionalRanksDependingOnServerType(ServerType type, Rank... ranks) {
        additionalRanksPerServerTypes.put(type, Sets.newHashSet(ranks));
    }

    protected void error(CommandSender cs, String message) {
        cs.sendMessage(plugin.getChatPrefix() + " " + Theme.ERROR.buildTheme(false) + message);
    }

    protected void success(CommandSender cs, String message) {
        cs.sendMessage(plugin.getChatPrefix() + " " + Theme.SUCCESS.buildTheme(false) + message);
    }

    protected void sendUsage(CommandSender cs) {
        cs.sendMessage(plugin.getChatPrefix() + " " + Theme.ERROR.buildTheme(false) + "Correct Usage: ");
        for (String m : usages) {
            cs.sendMessage(plugin.getChatPrefix() + " " + Theme.INCOGNITO.buildTheme(false) + m);
        }
    }
    
    private void performRun(CommandSource source, CommandSender cs, String[] args) {
        DispatchResult result = super.run(cs, source, args);
        if(null != result.getType()) switch (result.getType()) {
            case NO_ROUTE:
                cs.sendMessage(plugin.getChatPrefix() + " " + NO_PLAYER_INSTANCE);
                break;
            case NO_VALID_ROUTE:
                sendUsage(cs);
                break;
            case OTHER:
                error(cs, result.getMessage().orElse("An error has occured."));
                break;
        }
    }
}
