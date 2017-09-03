/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.annotations.StringArg;
import java.util.UUID;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.chat.ChatChannel;
import com.spleefleague.core.chat.ChatManager;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.io.EntityBuilder;
import com.spleefleague.core.infraction.Infraction;
import com.spleefleague.core.infraction.InfractionType;
import com.spleefleague.core.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class kick extends BasicCommand {

    public kick(CorePlugin plugin, String name, String usage) {
        super(plugin, new kickDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint
    public void kick(CommandSender cs, @PlayerArg(exact = true) Player target, @StringArg String[] message) {
        String kickMessage = StringUtil.fromArgsArray(message);
        target.kickPlayer("You have been kicked: " + kickMessage);
        Infraction kick = new Infraction(target.getUniqueId(), cs instanceof Player ? ((Player) cs).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000"), InfractionType.KICK, System.currentTimeMillis(), -1, kickMessage);
        Bukkit.getScheduler().runTaskAsynchronously(SpleefLeague.getInstance(), () -> {
            EntityBuilder.save(kick, SpleefLeague.getInstance().getPluginDB().getCollection("Infractions"));
        });
        ChatManager.sendMessage(new ComponentBuilder(SpleefLeague.getInstance().getChatPrefix() + " ")
                .append(target.getName() + " has been kicked by " + cs.getName() + "!")
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reason: " + kickMessage)
                        .color(ChatColor.GRAY)
                        .create()))
                .create(), ChatChannel.STAFF_NOTIFICATIONS);
        success(cs, "The player has been kicked!");
    }
}
