package com.minehut.pvp.commands;

import com.minehut.api.managers.command.Command;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.pvp.arena.ArenaType;
import com.minehut.pvp.Core;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by luke on 5/16/15.
 */
public class JoinCommand extends Command {
    private Core core;

    public JoinCommand(Core core) {
        super(core, "join", Rank.regular);
        this.core = core;
    }

    @Override
    public boolean call(Player player, ArrayList<String> args) {
        if (args == null || args.size() == 0) {
        	 player.sendMessage(C.red + "/join (type)");
             player.sendMessage(C.red + "Queue types: melee, ranged, both");
             return false;
        }
        if (args.size() == 1) {
        	String type = args.get(0);
        	if (ArenaType.valueOf(type)!=null) {
        		if (!this.core.queueManager.isPlayerInQueue(player.getUniqueId())) {
        			this.core.queueManager.joinQueue(player.getUniqueId(), type);
            		player.sendMessage(C.green + "Joining the " + C.aqua + type + C.green + " queue.");
            	} else {
            		player.sendMessage(C.red + "You are already in the queue!");
            	}
        	} else {
        		player.sendMessage(C.red + "/join (type)");
                player.sendMessage(C.red + "Queue types: melee, ranged, both");
                return false;
        	}
        	return true;
        } else {
        	 player.sendMessage(C.red + "/join (type)");
             player.sendMessage(C.red + "Queue types: melee, ranged, both");
             return false;
        }
    }
}
