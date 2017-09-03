/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Jonas
 */
public class register extends BasicCommand {

    public register(CorePlugin plugin, String name, String usage) {
        super(plugin, new registerDispatcher(), name, usage);
    }
    
    @Endpoint(target = {PLAYER})
    public void register(CommandSender cs, @StringArg String[] args) {
        error(cs, "This command is currently disabled");
    }

//    @Override
//    protected void run(Player p, SLPlayer slp, Command cmd, String[] args) {
//        if (args.length == 1) {
//            if (!slp.hasForumAccount()) {
//                Bukkit.getScheduler().runTaskAsynchronously(SpleefLeague.getInstance(), () -> {
//                    Result result = XenforoAPIUtil.createForumUser(p, args[0]);
//                    switch (result) {
//                        case SUCCESS: {
//                            slp.setForumAccount(true);
//                            success(p, "Your account has been created.");
//                            break;
//                        }
//                        case INVALID_EMAIL: {
//                            error(p, "Please enter a valid email address!");
//                            break;
//                        }
//                        case INJECTION: {
//                            error(p, "Please enter a valid email address! This incident has been logged.");
//                            break;
//                        }
//                        case EMAIL_EXISTS: {
//                            error(p, "This email address already exists!");
//                            break;
//                        }
//                        case UNREACHABLE: {
//                            error(p, "The website seems to be unreachable. Please try again later.");
//                            break;
//                        }
//                    }
//                });
//            } else {
//                error(p, "You already seem to have an account. If you believe this is an error, contact the staff team.");
//            }
//        } else {
//            sendUsage(slp);
//        }
//    }
}
