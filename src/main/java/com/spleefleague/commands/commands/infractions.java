/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.mongodb.client.MongoCursor;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.SLPlayerArg;
import java.util.Date;
import java.util.UUID;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.infraction.Infraction;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.DatabaseConnection;
import com.spleefleague.core.utils.TimeUtil;
import com.spleefleague.entitybuilder.EntityBuilder;
import java.util.HashSet;
import java.util.Set;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class infractions extends BasicCommand {

    public infractions(CorePlugin plugin, String name, String usage) {
        super(plugin, new infractionsDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint
    public void infractions(CommandSender cs, @SLPlayerArg(offline = true) SLPlayer target) {
        infractions(cs, target, 1);
    }

    @Endpoint
    public void infractions(CommandSender cs, @SLPlayerArg(offline = true) SLPlayer target, @IntArg(min = 1) int page) {
        DatabaseConnection.find(SpleefLeague.getInstance().getPluginDB().getCollection("Infractions"), new Document("uuid", target.getUniqueId().toString()), (result) -> {
            result.sort(new Document("time", -1));
            Set<Document> dbc = new HashSet<>();
            for (Document d : result) {
                dbc.add(d);
            }
            if (dbc.isEmpty()) {
                error(cs, "The player \"" + target.getName() + "\" doesn't have any infractions yet!");
                return;
            }
            int maxPages = (dbc.size() - 1) / 10 + 1;
            if (page > maxPages) {
                error(cs, page + " is not a valid page." + (maxPages == 1 ? " There is only one page!" : " Please choose a number between 1 and " + maxPages + "!"));
                return;
            }
            cs.sendMessage(ChatColor.DARK_GRAY + "[========== " + ChatColor.GRAY + target.getName() + "'s infractions (" + ChatColor.RED + page + ChatColor.GRAY + "/" + ChatColor.RED + maxPages + ChatColor.GRAY + ") " + ChatColor.DARK_GRAY + "==========]");
            result.skip((page - 1) * 10);
            MongoCursor<Document> mc = result.iterator();
            for (int i = 0; i < 10 && mc.hasNext(); i++) {
                Infraction inf = EntityBuilder.load(mc.next(), Infraction.class);
                cs.sendMessage(ChatColor.RED + String.valueOf(page * 10 - 9 + i) + ". " + ChatColor.DARK_GRAY + "| " + inf.getType().getColor() + inf.getType() + ChatColor.DARK_GRAY + " | " + ChatColor.RED + (inf.getPunisher().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")) ? "CONSOLE" : DatabaseConnection.getUsername(inf.getPunisher())) + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + inf.getMessage() + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + TimeUtil.dateToString(new Date(inf.getTime()), false) + " ago");
            }
        });
    }
}
