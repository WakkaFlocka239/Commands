/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class invsee extends BasicCommand {

    public invsee(CorePlugin plugin, String name, String usage) {
        super(plugin, new invseeDispatcher(), name, usage, Rank.MODERATOR);
    }
    
    @Endpoint(target = {PLAYER})
    public void seeInventory(Player p, @PlayerArg(exact = true) Player target) {
        p.openInventory(target.getInventory());
    }
    
    @Endpoint(target = {PLAYER})
    public void seeEnderchest(Player p, @LiteralArg(value="enderchest") String l, @PlayerArg(exact = true) Player target) {
        p.openInventory(target.getEnderChest());
    }
}
