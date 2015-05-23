package com.minehut.pvp.stats;

import com.google.gson.Gson;
import com.minehut.commons.common.chat.F;
import com.minehut.pvp.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by luke on 5/22/15.
 */
public class KillsManager {
    private Core core;

    public KillsManager(Core core) {
        this.core = core;
    }

    public void setupPlayer(Player player) {
        if (!this.hasKillsInDatabase(player)) {
            this.createPlayerRanks(player);
            F.log("Creating Kills in database for " + player.getName());
        }
    }

    public void addKill(Player player) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.core, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = core.api.getStatManager().getMySQL().getConnection().prepareStatement(
                            "UPDATE player_kills SET kills = kills + 1 WHERE uuid='" + player.getUniqueId() + "';");
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addDeath(Player player) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.core, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = core.api.getStatManager().getMySQL().getConnection().prepareStatement(
                            "UPDATE player_kills SET deaths = deaths + 1 WHERE uuid='" + player.getUniqueId() + "';");
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createPlayerRanks(Player player) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.core, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = core.api.getStatManager().getMySQL().getConnection().prepareStatement(
                            "INSERT INTO `player_kills` (`id`, `uuid`, `kills`, `deaths`)" +
                                    " VALUES (NULL, '" + player.getUniqueId().toString() + "', '0', '0')");
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean hasKillsInDatabase(Player player) {
        try {
            PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `player_kills` where `uuid` = '" + player.getUniqueId().toString() + "'");
            ResultSet res = statement.executeQuery();
            if (!res.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
