package com.minehut.pvp.events;

import com.minehut.pvp.events.events.SpawnPreparePlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by luke on 5/17/15.
 */
public class EventCaller {

    /* Equips a player with their spawn gear */
    public static void callSpawnPreparePlayer(Player player) {
        Bukkit.getServer().getPluginManager().callEvent(new SpawnPreparePlayerEvent(player));
    }
}
