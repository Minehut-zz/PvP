package com.minehut.pvp.commands;

import com.minehut.api.managers.command.Command;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.commons.common.chat.F;
import com.minehut.pvp.Core;
import com.minehut.pvp.ELOManager;
import com.minehut.pvp.arena.ArenaType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by luke on 5/17/15.
 */
public class EloCommand extends Command {
    private Core core;

    public EloCommand(Core core) {
        super(core, "elo", Rank.regular);
        this.core = core;
    }

    @Override
    public boolean call(Player player, ArrayList<String> arrayList) {

        player.sendMessage("");

        for (ArenaType arenaType : ArenaType.values()) {
            F.log("searching for elo of type: " + arenaType.getType());

            if(core.getEloManager().hasELO(player)) {
                ELOManager.ELO elo = core.getEloManager().getELOInArena(player, arenaType);

                if (elo.currentELO > 500) {
                    player.sendMessage(arenaType.getDisplayName() + C.gray + ":  " + C.white
                            + core.getEloManager().getPlayerDivisionForElo(elo).getDisplayName()
                            + C.gray + " [" + Integer.toString(elo.currentELO) + "]");
                } else {
                /* Unranked */
                    player.sendMessage(arenaType.getDisplayName() + C.gray + ":  " + C.white + "Unranked");
                }
            } else {
                player.sendMessage(arenaType.getDisplayName() + C.gray + ":  " + C.white
                        + "Unranked"
                        + C.gray + " [1050]");
            }
        }

        player.sendMessage("");

        return false;
    }
}
