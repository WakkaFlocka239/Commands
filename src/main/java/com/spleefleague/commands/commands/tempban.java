/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.mongodb.client.FindIterable;
import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.commands.Commands;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.chat.ChatChannel;
import com.spleefleague.core.chat.ChatManager;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.infraction.Infraction;
import com.spleefleague.core.infraction.InfractionType;
import com.spleefleague.core.io.EntityBuilder;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.DatabaseConnection;
import com.spleefleague.core.utils.StringUtil;
import com.spleefleague.core.utils.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

/**
 *
 * @author Jonas
 */
public class tempban extends BasicCommand {

    public tempban(CorePlugin plugin, String name, String usage) {
        super(plugin, new tempbanDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint(target = {PLAYER})
    public void tempban(SLPlayer cs, @SLPlayerArg(exact = true, offline = true) SLPlayer target, @StringArg String durationString, @StringArg String[] reason) {
        String tempbanMessage = StringUtil.fromArgsArray(reason);
        Duration duration = TimeUtil.parseDurationString(durationString);
        UUID senderId = cs.getUniqueId();
        performTempban(target, senderId, cs.getName(), duration, tempbanMessage);
        if(target.isOnline()) {
            target.kickPlayer("You have been tempbanned for " + TimeUtil.durationToString(duration) + ". " + tempbanMessage);
        }
        if(target != cs) {
            success(cs, "The player has been tempbanned!");
        }
    }
    
    @Endpoint(target = {CONSOLE})
    public void tempban(CommandSender cs, @SLPlayerArg(exact = true, offline = true) SLPlayer target, @StringArg String durationString, @StringArg String[] reason) {
        String tempbanMessage = StringUtil.fromArgsArray(reason);
        Duration duration = TimeUtil.parseDurationString(durationString);
        UUID senderId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        performTempban(target, senderId, cs.getName(), duration, tempbanMessage);
        if(target.isOnline()) {
            target.kickPlayer("You have been tempbanned for " + TimeUtil.durationToString(duration) + ". " + tempbanMessage);
        }
        success(cs, "The player has been banned!");
    }
    
    private void performTempban(SLPlayer target, UUID sender, String senderName, Duration duration, String message) {
        Infraction tempban = new Infraction(target.getUniqueId(), sender, InfractionType.TEMPBAN, System.currentTimeMillis(), duration.toMillis(), message);
            Bukkit.getScheduler().runTaskAsynchronously(Commands.getInstance(), () -> {
                EntityBuilder.save(tempban, SpleefLeague.getInstance().getPluginDB().getCollection("Infractions"), false);
                EntityBuilder.save(tempban, SpleefLeague.getInstance().getPluginDB().getCollection("ActiveInfractions"), false);
            });
            ChatManager.sendMessage(new ComponentBuilder(SpleefLeague.getInstance().getChatPrefix() + " ")
                    .append(senderName + " has been tempbanned by " + senderName + "!")
                    .color(ChatColor.GRAY)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reason: " + message + "\n")
                            .color(ChatColor.GRAY)
                            .append("Expires: " + TimeUtil.durationToString(duration))
                            .color(ChatColor.GRAY)
                            .create()))
                    .create(), ChatChannel.STAFF_NOTIFICATIONS);
    }
}
