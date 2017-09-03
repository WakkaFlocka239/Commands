package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.commands.Commands;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class dcmd extends BasicCommand {

    private final int commandsPerPage = 8;

    public dcmd(CorePlugin plugin, String name, String usage) {
        super(plugin, new dcmdDispatcher(), name, usage, Rank.DEVELOPER);
    }
    
    @Endpoint
    public void list(CommandSender p, @LiteralArg(value = "list") String l) {
        list(p, l, 1);
    }

    @Endpoint
    public void list(CommandSender p, @LiteralArg(value = "list") String l, @IntArg(min = 1) int page) {
        String[] commands = Commands.getInstance().getDynamicCommandManager().getRegisteredCommands();
        if (commands.length == 0) {
            error(p, "No dynamic commands are currently loaded!");
            return;
        }
        page--;
        int low = (page * commandsPerPage);
        int high = low + commandsPerPage;
        if (low < 0) {
            low = 0;
            high = low + commandsPerPage;
        }
        while (low >= commands.length) {
            low -= commandsPerPage;
        }
        if (high >= commands.length) {
            high = commands.length;
        }
        page = low / commandsPerPage;
        int totalPages = commands.length / commandsPerPage;
        page++;
        totalPages++;
        p.sendMessage(ChatColor.GRAY + "[==== " + ChatColor.GREEN + "Enabled Dynamic Commands [page " + page + "/" + totalPages + "]" + ChatColor.GRAY + " ====]");
        for (int i = low; i < high; i++) {
            p.sendMessage(ChatColor.DARK_GREEN + " - /" + ChatColor.GREEN + commands[i]);
        }
    }

    @Endpoint
    public void disabled(CommandSender p, @LiteralArg(value = "disabled") String l) {
        disabled(p, l, 1);
    }

    @Endpoint
    public void disabled(CommandSender p, @LiteralArg(value = "disabled") String l, @IntArg(min = 1) int page) {
        String[] commands = Commands.getInstance().getDynamicCommandManager().getUnloadedCommands();
        if (commands.length == 0) {
            error(p, "No dynamic commands are currently loaded!");
            return;
        }
        page--;
        int low = (page * commandsPerPage);
        int high = low + commandsPerPage;
        if (low < 0) {
            low = 0;
            high = low + commandsPerPage;
        }
        while (low >= commands.length) {
            low -= commandsPerPage;
        }
        if (high >= commands.length) {
            high = commands.length;
        }
        page = low / commandsPerPage;
        int totalPages = commands.length / commandsPerPage;
        page++;
        totalPages++;
        p.sendMessage(ChatColor.GRAY + "[==== " + ChatColor.RED + "Disabled Dynamic Commands [page " + page + "/" + totalPages + "]" + ChatColor.GRAY + " ====]");
        for (int i = low; i < high; i++) {
            p.sendMessage(ChatColor.DARK_RED + " - /" + ChatColor.RED + commands[i]);
        }
    }

    @Endpoint
    public void add(CommandSender p, @LiteralArg(value = "add") String l, @StringArg String haste) {
        Commands.getInstance().getDynamicCommandManager().register(haste, (ChatColor color, String txt) -> p.sendMessage(color + txt));
    }

    @Endpoint
    public void remove(CommandSender p, @LiteralArg(value = "remove") String l, @StringArg String name) {
        if (Commands.getInstance().getDynamicCommandManager().unregister(name)) {
            success(p, "Unregistered dynamic command: " + name);
        } else {
            error(p, "Command not found.");
        }
    }
    
    @Endpoint
    public void enable(CommandSender p, @LiteralArg(value = "enable") String l, @StringArg String name) {
        Commands.getInstance().getDynamicCommandManager().enable(
                name,
                (ChatColor color, String txt) -> {
                    p.sendMessage(color + txt);
                }
        );
    }

}
