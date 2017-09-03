package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.core.chat.Theme;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.cosmetics.CItem;
import com.spleefleague.core.cosmetics.CosmeticsManager;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.UtilChat;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class collectibles extends BasicCommand {

    public collectibles(CorePlugin plugin, String name, String usage) {
        super(plugin, new collectiblesDispatcher(), name, usage, Rank.DEVELOPER);
    }

    @Endpoint
    public void help(CommandSender cs, @LiteralArg(value = "help") String l) {
        help(cs);
    }

    @Endpoint
    public void help(CommandSender cs) {
        UtilChat.s(Theme.INFO, cs, "Help for &b/collectibles&e:");
        printHelp(cs, "/collectibles list <page>", "prints list of all cosmetic items with their ids");
        printHelp(cs, "/collectibles give <player> <id>", "gives cosmetic item with given id to the player");
        printHelp(cs, "/collectibles take <player> <id>", "takes cosmetic item with given id from the player");
    }

    @Endpoint
    public void list(CommandSender cs, @LiteralArg(value = "list") String l) {
        list(cs, l, 1);
    }

    @Endpoint
    public void list(CommandSender cs, @LiteralArg(value = "list") String l, @IntArg(min = 1) int page) {
        final int perPage = 10;
        int start = (page - 1) * perPage + 1, end = page * perPage;
        for (int id = start; id <= end; ++id) {
            CItem citem = CosmeticsManager.getItem(id);
            if (citem == null) {
                break;
            }
            UtilChat.s(cs, "&7%d. &r%s", id, citem.getName());
        }
    }

    @Endpoint
    public void give(CommandSender cs, @LiteralArg(value = "give") String l, @SLPlayerArg(exact = true) SLPlayer slp, @IntArg(min = 1) int id) {
        CItem citem = CosmeticsManager.getItem(id);
        if (citem == null) {
            error(cs, "Cosmetic item with given id doesn't exist.");
            return;
        }

        if (slp.getCollectibles().getItems().contains(id)) {
            error(cs, "This player already has cosmetic item with given id.");
            return;
        }
        slp.getCollectibles().addItem(id);
        UtilChat.s(Theme.SUCCESS, cs, "New cosmetic item with id &b%d &ahas been added to &b%s&a!", id, slp.getName());
    }

    @Endpoint
    public void take(CommandSender cs, @LiteralArg(value = "take") String l, @SLPlayerArg(exact = true) SLPlayer slp, @IntArg(min = 1) int id) {
        CItem citem = CosmeticsManager.getItem(id);
        if (citem == null) {
            error(cs, "Cosmetic item with given id doesn't exist.");
            return;
        }
        if (slp.getCollectibles().getItems().contains(id)) {
            error(cs, "This player already has cosmetic item with given id.");
            return;
        }
        slp.getCollectibles().removeItem(id);
        UtilChat.s(Theme.SUCCESS, cs, "Cosmetic item with id &b%d &ahas been taken from &b%s&a!", id, slp.getName());
    }
    
    private void printHelp(CommandSender cs, String cmd, String description) {
        UtilChat.s(cs, "&b%s &8- &7%s&8.", cmd, description);
    }
}
