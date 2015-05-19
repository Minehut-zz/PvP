package com.minehut.pvp.stats;

import com.google.gson.Gson;
import com.minehut.commons.common.chat.F;
import com.minehut.pvp.Core;
import com.minehut.pvp.arena.ArenaType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by luke on 5/18/15.
 */
public class DeathStat {
    /* Killer */
    private UUID killer;
    private ArrayList<Map<String, Object>> killerInv;
    private ArrayList<Map<String, Object>> killerArmor;

    /* Dead */
    private UUID dead;
    private ArrayList<Map<String, Object>> deadInv;
    private ArrayList<Map<String, Object>> deadArmor;

    /* Queue */
    private ArenaType arenaType;

    public DeathStat(Core core, Player killer, Player dead, ArenaType arenaType) {
        /* Killer Player */
        this.killer = killer.getUniqueId();
        this.killerInv = getSerializedItems(killer.getInventory().getContents());
        this.killerArmor = getSerializedItems(killer.getInventory().getArmorContents());

        /* Dead Player */
        this.dead = dead.getUniqueId();
        this.deadInv = getSerializedItems(dead.getInventory().getContents());
        this.deadArmor = getSerializedItems(dead.getInventory().getArmorContents());

        this.arenaType = arenaType;
        this.upload(core);
    }

    private void upload(Core core) {
        final String json = new Gson().toJson(this);

        /* Bukkit.getServer().getScheduler().runTaskAsynchronously(core, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = core.api.getStatManager().getMySQL().getConnection().prepareStatement(
                            "INSERT INTO `deaths` (`id`, `killer`, `dead`, `death_json`)" +
                                    " VALUES (NULL, '" + killer.toString() + "', '" + dead.toString() + "', '" + json + "');");
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    private ArrayList<Map<String, Object>> getSerializedItems(ItemStack[] contents) {
        ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        for (ItemStack itemStack : contents) {
            /* Air is null, bukkit devs ur dumb */
            if (itemStack == null) {
                continue;
            }
            Map<String, Object> ser = itemStack.serialize();
            items.add(ser);
        }

        return items;
    }

}
