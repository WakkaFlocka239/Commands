/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.PlayerArg;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
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
    public void pingSelf(Player p) {
        showPing(p, p);
    }
    
    @Endpoint
    public void pingOther(Player p, @PlayerArg Player target) {
        showPing(p, target);
    }

    private void showPing(Player to, Player whose) {
        boolean same = (to == whose);
        int ping = getPlayerPing(whose);
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
        String pingStr = Integer.toString(ping) + " ms";
        if (same) {
            if (ping != 1337) {
                success(to, ChatColor.GRAY + "Your ping is: " + c + pingStr);
            } else {
                success(to, ChatColor.GRAY + "Your ping is: " + ChatColor.GREEN + 1 + ChatColor.RED + 3 + ChatColor.YELLOW + 3 + ChatColor.BLUE + 7);
            }
        } else {
            success(to, ChatColor.GRAY + "Showing ping for " + ChatColor.RED + whose.getName() + ChatColor.GRAY + ": " + c + pingStr);
        }
    }

    private int getPlayerPing(Player p) {
        EntityPlayer nmsp = (EntityPlayer) (((CraftPlayer) p).getHandle());
        return nmsp.ping;
    }
}
