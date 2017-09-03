package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import org.bukkit.entity.Player;

import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.Warp;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class warp extends BasicCommand {

    public warp(CorePlugin plugin, String name, String usage) {
        super(plugin, new warpDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
    }
    
    @Endpoint(target = {PLAYER})
    public void warplist(Player sender) {
        List<Warp> warps = Warp.getAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        ComponentBuilder cb = new ComponentBuilder("---------------Warps list---------------").color(ChatColor.RED);
        cb.append("\n").reset();

        boolean first = true;
        for (Warp warp : warps) {
            if (!first) {
                cb.append(" , ").reset();
            }
            cb.append("[" + warp.getName() + "]")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to teleport to '" + warp.getName() + "'").create()));

            first = false;
        }
        sender.spigot().sendMessage(cb.create());
    }

    @Endpoint(target = {PLAYER})
    public void warp(Player sender, @StringArg String warpName) {
        Warp warp = Warp.byName(warpName);

        if (warp != null) {
            sender.teleport(warp.getLocation(), TeleportCause.COMMAND);
            success(sender, "You have been teleported to '" + warp.getName() + "'");
        } else {
            error(sender, "The specified warp does not exist");
        }
    }
}
