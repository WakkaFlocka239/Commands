/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.SLPlayerArg;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;

/**
 *
 * @author JoBa
 */
public class ping extends BasicCommand {

    public ping(CorePlugin plugin, String name, String usage) {
        super(plugin, new pingDispatcher(), name, usage, Rank.DEFAULT);
    }

    @Endpoint
    public void pingSelf(SLPlayer p) {
        showPing(p, p);
    }
    
    @Endpoint
    public void pingOther(SLPlayer p, @SLPlayerArg SLPlayer target) {
        showPing(p, target);
    }

    private void showPing(SLPlayer to, SLPlayer whose) {
        int ping = whose.getPing();
        String realPing;
        if(ping < whose.getAimedPing()) {
            realPing = ChatColor.GRAY + " (Throttled to " + ChatColor.RED + formatPing(whose.getAimedPing()) + ChatColor.GRAY + ")";
        }
        else {
            realPing = "";
        }
        if (to == whose) {
            success(to, ChatColor.GRAY + "Your ping is: " + formatPing(ping) + realPing);
        } else {
            String who = whose.getName() + "'";
            if(!whose.getName().endsWith("s")) {
                who += "s";
            }
            success(to, ChatColor.RED + who + " ping" + ChatColor.GRAY + ": " + formatPing(ping) + realPing);
        }
    }
    
    private String formatPing(int ping) {
        ChatColor c;
        if (ping < 30) {
            c = ChatColor.DARK_GREEN;
        } else if (ping < 60) {
            c = ChatColor.GREEN;
        } else if (ping < 120) {
            c = ChatColor.YELLOW;
        } else if (ping < 250) {
            c = ChatColor.GOLD;
        } else if (ping < 500) {
            c = ChatColor.RED;
        } else {
            c = ChatColor.DARK_RED;
        }
        String pingStr;
        if(ping != 1337) {
            pingStr = "" + c + Integer.toString(ping);
        }
        else { 
            pingStr = "" + ChatColor.GREEN + 1 + ChatColor.RED + 3 + ChatColor.YELLOW + 3 + ChatColor.BLUE + 7;
        }
        pingStr += " ms";
        return pingStr;
    }
}
