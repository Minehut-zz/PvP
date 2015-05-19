package com.minehut.pvp.commands;

import com.minehut.api.managers.command.Command;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.commons.common.chat.F;
import com.minehut.pvp.Core;
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
        super(core, "ranks", Rank.regular);
        this.core = core;
    }

    @Override
    public boolean call(Player player, ArrayList<String> arrayList) {

        player.sendMessage("");
        player.sendMessage(C.gray + "Displaying your current ELOs");

        for (ArenaType arenaType : ArenaType.values()) {
            F.log("searching for elo of type: " + arenaType.getType());
            int elo = core.getEloManager().getELO(player, arenaType);
            player.sendMessage(C.gray + arenaType.getType() + ": " + C.yellow + ((elo > 500)?elo:"Unranked"));
        }

        player.sendMessage("");

        return false;
    }
}
