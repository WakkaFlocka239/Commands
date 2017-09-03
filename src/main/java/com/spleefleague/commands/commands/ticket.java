package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import static com.spleefleague.core.listeners.ConnectionListener.sendTicket;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.SpleefLeague;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.Config;
import com.spleefleague.core.io.connections.ConnectionClient;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.StringUtil;
import org.json.simple.JSONObject;

/**
 *
 * @author Patrick F.
 */
public class ticket extends BasicCommand {

    public ticket(CorePlugin plugin, String name, String usage) {
        super(plugin, new ticketDispatcher(), name, usage);
    }

    @Endpoint(target = {PLAYER})
    public void ticket(SLPlayer slp, @StringArg String[] args) {
        String message = StringUtil.fromArgsArray(args);
        ConnectionClient cc = SpleefLeague.getInstance().getConnectionClient();
        if (cc.isEnabled()) {
            JSONObject sendObject = new JSONObject();
            sendObject.put("rankColor", slp.getRank().getColor().name());
            sendObject.put("sendUUID", slp.getUniqueId());
            sendObject.put("sendName", slp.getName());
            sendObject.put("shownName", slp.getName());
            sendObject.put("message", message);
            SpleefLeague.getInstance().getConnectionClient().send("ticket", sendObject);
        } else {
            sendTicket(slp.getName(), slp.getName(), slp.getUniqueId(), message, Config.getString("server_name"), slp.getRank().getColor());
        }
    }
}