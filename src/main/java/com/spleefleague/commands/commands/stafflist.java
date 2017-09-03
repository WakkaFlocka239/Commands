package com.spleefleague.commands.commands;

import com.spleefleague.annotations.Endpoint;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.connections.ConnectionResponseHandler;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Josh on 04/08/2016.
 */
public class stafflist extends BasicCommand {

    public stafflist(CorePlugin plugin, String name, String usage) {
        super(plugin, new stafflistDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint()
    public void stafflist(CommandSender cs) {
        success(cs, "Requesting...");

        JSONObject requestObject = new JSONObject();
        requestObject.put("action", "GET_STAFF");
        new ConnectionResponseHandler("sessions", requestObject, 100) {

            @Override
            protected void response(JSONObject jsonObject) {
                if (jsonObject == null) {
                    error(cs, "Response timed out! Please try again.");
                    return;
                }
                cs.sendMessage(ChatColor.DARK_GRAY + "[========== " + ChatColor.DARK_AQUA + "Online Staff " + ChatColor.DARK_GRAY + " ==========]");

                JSONArray result = (JSONArray) jsonObject.get("staff");
                for (Object results : result) {
                    JSONObject staffMember = (JSONObject) results;
                    String name = (String) staffMember.get("username"), server = (String) staffMember.get("playerServer");
                    Rank rank = Rank.valueOf(staffMember.get("rank").toString().toUpperCase());
                    cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + (rank == null ? "Unknown" : rank.getDisplayName())
                            + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + name + ChatColor.DARK_GRAY + " | "
                            + ChatColor.GREEN + "Online on " + server + "!");
                }
            }
        };
    }
}
