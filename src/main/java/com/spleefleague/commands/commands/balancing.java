package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

/**
 * Created by Josh on 09/08/2016.
 */
public class balancing extends BasicCommand {

    public balancing(CorePlugin plugin, String name, String usage) {
        super(plugin, new balancingDispatcher(), name, usage, Rank.DEVELOPER);
    }

    @Endpoint
    public void refresh(CommandSender cs, @LiteralArg(value = "refresh") String literal) {
        JSONObject request = new JSONObject();
        request.put("action", "REFRESH");
        SpleefLeague.getInstance().getConnectionClient().send("rotation", request);
        success(cs, "Requested balancing refresh!");
    }
}
