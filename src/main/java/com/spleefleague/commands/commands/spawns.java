package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.spawn.SpawnManager.SpawnLocation;
import com.spleefleague.core.utils.TimeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;

import java.util.Date;

/**
 * Created by Josh on 05/02/2016.
 */
public class spawns extends BasicCommand {

    public spawns(CorePlugin plugin, String name, String usage) {
        super(plugin, new spawnsDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint(target = {PLAYER})
    public void spawn(SLPlayer p) {
        p.sendMessage(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "============ " + ChatColor.DARK_AQUA + "Spawns" + ChatColor.GRAY + " ============" + ChatColor.DARK_GRAY + " ]");
        int i = 1;
        for(SpawnLocation spawnLocation : SpleefLeague.getInstance().getSpawnManager().getAll()) {
            ComponentBuilder componentBuilder = new ComponentBuilder("#" + i++).color(ChatColor.RED.asBungee())
                    .append(" | ").color(ChatColor.DARK_GRAY.asBungee())
                    .append("CLICK TO TELEPORT").color(ChatColor.GRAY.asBungee()).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + spawnLocation.getLocation().getBlockX() + " " + spawnLocation.getLocation().getBlockY() + ' ' + spawnLocation.getLocation().getBlockZ()))
                    .append(" | ").color(ChatColor.DARK_GRAY.asBungee())
                    .append(spawnLocation.getPlayersInRadius() + " players").color(ChatColor.GRAY.asBungee());
            p.spigot().sendMessage(componentBuilder.create());
        }
        p.sendMessage(ChatColor.RED + "All spawns were cached " + TimeUtil.dateToString(new Date(SpleefLeague.getInstance().getSpawnManager().getLastCached()), false) + " ago.");
    }
}
