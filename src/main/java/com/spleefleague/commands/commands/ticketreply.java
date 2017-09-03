package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.SLPlayerArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.Config;
import com.spleefleague.core.io.connections.ConnectionClient;
import com.spleefleague.core.io.connections.ConnectionResponseHandler;
import static com.spleefleague.core.listeners.ConnectionListener.sendTicket;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.DatabaseConnection;
import com.spleefleague.core.utils.StringUtil;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

/**
 *
 * @author Patrick F.
 */
public class ticketreply extends BasicCommand {

    public ticketreply(CorePlugin plugin, String name, String usage) {
        super(plugin, new ticketreplyDispatcher(), name, usage, Rank.MODERATOR);
    }

    @Endpoint(target = {PLAYER}, priority = 1)
    public void ticketReplyOnline(SLPlayer slp, @SLPlayerArg SLPlayer target, @StringArg String[] args) {
        String message = StringUtil.fromArgsArray(args);
        ConnectionClient cc = SpleefLeague.getInstance().getConnectionClient();
        if (cc.isEnabled()) {

            sendToOtherServers(slp, target.getName(), target.getUniqueId(), message);
        } else {
            sendTicket(target.getName(), slp.getName(), target.getUniqueId(), message, Config.getString("server_name"), slp.getRank().getColor());

        }
    }

    @Endpoint(target = {PLAYER}, priority = 0)
    public void ticketReplyOffline(SLPlayer slp, @StringArg String name, @StringArg String[] args) {
        if (args.length > 1) {
            ConnectionClient cc = SpleefLeague.getInstance().getConnectionClient();
            String message = StringUtil.fromArgsArray(args);
            UUID targetId = DatabaseConnection.getUUID(name);
            if (targetId == null) {

                error(slp, name + " has never played on SpleefLeague!");
                return;
            }
            if (cc.isEnabled()) {
                sendToOtherServers(slp, name, targetId, message);
            } else {
                error(slp, name + " is not online!");
            }
        }
    }

    private void sendToOtherServers(SLPlayer slp, String targetName, UUID targetId, String message) {
        ConnectionClient cc = SpleefLeague.getInstance().getConnectionClient();
        if (cc.isEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(SpleefLeague.getInstance(), () -> {
                JSONObject request = new JSONObject();
                request.put("uuid", targetId.toString());
                request.put("action", "GET_PLAYER");
                new ConnectionResponseHandler("sessions", request, 40) {

                    @Override
                    protected void response(JSONObject jsonObject) {
                        if (jsonObject == null || jsonObject.get("playerServer").toString().equalsIgnoreCase("OFFLINE")) {
                            error(slp, targetName + " isn't online!");
                            return;
                        }
                        JSONObject sendObject = new JSONObject();
                        sendObject.put("rankColor", slp.getRank().getColor().name());
                        sendObject.put("sendUUID", targetId);
                        sendObject.put("sendName", targetName);
                        sendObject.put("shownName", slp.getName());
                        sendObject.put("overrideServer", jsonObject.get("playerServer").toString());
                        sendObject.put("message", message);
                        SpleefLeague.getInstance().getConnectionClient().send("ticket", sendObject);
                    }

                };
            });
        }
    }
}