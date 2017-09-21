/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.COMMAND_BLOCK;
import static com.spleefleague.annotations.CommandSource.CONSOLE;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.PlayerArg;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class gamemode extends BasicCommand {

    public gamemode(CorePlugin plugin, String name, String usage) {
        super(plugin, new gamemodeDispatcher(), name, usage, Rank.MODERATOR, Rank.BUILDER);
        //setAdditionalRanksDependingOnServerType(ServerType.BUILD, Rank.BUILDER);
    }
    
    @Endpoint(target = {PLAYER})
    public void toggleMod(SLPlayer slp) {
        if (slp.getRank() == Rank.MODERATOR) {
            slp.setGameMode((slp.getGameMode() == GameMode.SPECTATOR ? GameMode.SURVIVAL : GameMode.SPECTATOR));
            success(slp, "Gamemode toggled!");
            success(slp, "If you wish to toggle back, please use this command again.");
        }
        else {
            sendUsage(slp);
        }
    }
    
    
    @Endpoint(target = {PLAYER})
    public void giveSelf(SLPlayer slp, @LiteralArg(value = "creative", aliases = {"survival", "adventure", "spectator"}) String mode) {
        givePlayer(slp, mode, slp);
    }

    @Endpoint(target = {PLAYER})
    public void givePlayer(SLPlayer slp, @LiteralArg(value = "creative", aliases = {"survival", "adventure", "spectator"}) String mode, @PlayerArg(exact = true) Player target) {
        if (slp.getRank() == Rank.MODERATOR) {
            error(slp, NO_COMMAND_PERMISSION_MESSAGE);
            return;
        }
        givePlayerConsole(mode, target);
    }
    
    @Endpoint(target ={CONSOLE, COMMAND_BLOCK})
    public void givePlayerConsole(@LiteralArg(value = "creative", aliases = {"survival", "adventure", "spectator"}) String mode, @PlayerArg(exact = true) Player target) {
        handle(GameMode.valueOf(mode.toUpperCase()), target);
    }
    
    @Endpoint(target = {PLAYER})
    public void giveSelf(SLPlayer slp, @IntArg(min = 0, max = 3) int mode) {
        if (slp.getRank() == Rank.MODERATOR) {
            error(slp, NO_COMMAND_PERMISSION_MESSAGE);
            return;
        }
        givePlayer(mode, slp);
    }
    
    @Endpoint(target = {PLAYER})
    public void givePlayer(@IntArg(min = 0, max = 3) int mode, @PlayerArg(exact = true) Player target) {
        handle(GameMode.getByValue(mode), target);
    }
    
    @Endpoint(target = {CONSOLE, COMMAND_BLOCK})
    public void givePlayerConsole(@IntArg(min = 0, max = 3) int mode, @PlayerArg(exact = true) Player target) {
        handle(GameMode.getByValue(mode), target);
    }
    
    private void handle(GameMode mode, Player target) {
        target.setGameMode(mode);
        success(target, "Your gamemode has been updated!");
    }
}
