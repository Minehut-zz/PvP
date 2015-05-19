package com.minehut.pvp.commands;

import com.minehut.api.managers.command.Command;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.pvp.arena.Arena;
import com.minehut.pvp.arena.ArenaType;
import com.minehut.pvp.Core;
import com.minehut.pvp.HutLocation;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by luke on 5/16/15.
 */
public class CreateArenaCommand extends Command {
    private Core core;

    public CreateArenaCommand(Core core) {
        super(core, "arena", Rank.Admin);
        this.core = core;
    }

    @Override
    public boolean call(Player player, ArrayList<String> args) {

        if (args == null || args.size() == 0) {
            player.sendMessage(C.red + "/arena new (type) (type)");
            player.sendMessage(C.red + "/arena edit (name) (loc) (1)");
            return false;
        }

        /* New */
        if (args.get(0).equalsIgnoreCase("new")) {
            /* Parameters check */
            if (args.size() != 3) {
                player.sendMessage(C.red + "/arena new (name) (type)");
                return false;
            }

            String name = args.get(1);
            String type = args.get(2);

            Arena arena = new Arena(core);
            arena.name = name;
            arena.type = ArenaType.valueOf(type);
            arena.team1Spawn = new HutLocation(player.getLocation());
            arena.createArena();

            player.sendMessage(C.green + "Created arena " + C.aqua + name);
            return true;
        }

        /* Edit */
        if (args.get(0).equalsIgnoreCase("edit")) {
            /* Parameters check */
            if (args.size() != 4) {
                player.sendMessage(C.red + "/arena edit (name) (loc) (1)");
                return false;
            }

            Arena arena = core.getArenaManager().getArena(args.get(1));
            if (arena == null) {
                player.sendMessage(C.red + "Couldn'listeners find arena " + C.aqua + args.get(1));
                return false;
            }

            if (args.get(2).equalsIgnoreCase("loc")) {
                if (args.get(3).equalsIgnoreCase("1")) {
                    arena.team1Spawn = new HutLocation(player.getLocation());
                    arena.updateArena();
                    player.sendMessage(C.green + "Updated Spawn 1 for arena " + C.aqua + arena.name);
                    return true;
                } else if (args.get(3).equalsIgnoreCase("2")) {
                    arena.team2Spawn = new HutLocation(player.getLocation());
                    arena.updateArena();
                    player.sendMessage(C.green + "Updated Spawn 2 for arena " + C.aqua + arena.name);
                    return true;
                }
            }
        }


        return false;
    }
}
