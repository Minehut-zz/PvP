package com.minehut.pvp.queue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.minehut.pvp.Core;
import com.minehut.pvp.ELOManager.ELO;
import com.minehut.pvp.arena.ArenaType;

public class QueueManager {

	private Core core;
	
	public QueueManager(Core core) {
		this.core = core;
	}
	
	public Date getJoinDate(UUID uuid) {
		return new Date(this.getTestLong(uuid));
	}
	
	public ArrayList<QueuePlayer> getPlayersInQueue(ArenaType type) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `queue` where `type` = '" + type.toString() + "'");
			ResultSet res = statement.executeQuery();
			ArrayList<QueuePlayer> players = new ArrayList<QueuePlayer>();
			while (res.next()) {
				QueuePlayer loadedPlayer = new QueuePlayer(this, UUID.fromString(res.getString("uuid")), res.getLong("join_time"), ArenaType.valueOf(res.getString("type")));
				players.add(loadedPlayer);
			}
			Collections.sort(players, new JoinTimeComparator());
			return players;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public class JoinTimeComparator implements Comparator<QueuePlayer> {
		
		@Override
		public int compare(QueuePlayer arg0, QueuePlayer arg1) {
			return arg1.getSecondsInQueue() - arg0.getSecondsInQueue();
		}
	}
	
 	
	public long getTestLong(UUID uuid) {
		
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `queue` where `uuid` = '" + uuid.toString() + "'");
			ResultSet res = statement.executeQuery();
			res.next();
			long join_time = res.getLong("join_time");
			return join_time;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0L;
	}
	
	public void joinQueue(UUID uuid, String type) {
		this.joinQueue(uuid, ArenaType.valueOf(type));
	}

	public void joinQueue(UUID uuid, ArenaType type) {
		Player player = Bukkit.getPlayer(uuid);
		if (!this.core.eloManager.hasELO(player)) {
			this.core.eloManager.createPlayerRanks(player);
		}
		if (!this.core.eloManager.hasELOInArena(player, type)) {
			this.core.eloManager.getPlayerRanks(player).addELORank(this.core.eloManager.new ELO().setArenaType(type)).updateRanks();
		} else {
			System.out.println(player.getName() + " has an elo of " + this.core.eloManager.getELOInArena(player, type).currentELO);
		}
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
					"INSERT INTO `queue` (`id`, `uuid`, `join_time`, `type`)" +
							" VALUES (NULL, '" + uuid.toString() + "', '" + System.currentTimeMillis() + "', '" + type.getType() + "')");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void joinQueue(UUID uuid) {
		this.joinQueue(uuid, "melee");
	}
	
	public void leaveQueue(UUID uuid) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
							"DELETE FROM `queue` WHERE `uuid` = '" + uuid.toString() + "'");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlayerInQueue(UUID uuid) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `queue` where `uuid` = '" + uuid.toString() + "'");
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
