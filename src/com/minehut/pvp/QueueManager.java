package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;

public class QueueManager {

	private Core core;
	
	public QueueManager(Core core) {
		this.core = core;
	}
	
	public Date getJoinDate(UUID uuid) {
		return new Date(this.getTestLong(uuid));
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
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
							"INSERT INTO `queue` (`id`, `uuid`, `join_time`, `type`)" +
							" VALUES (NULL, '" + uuid.toString() + "', '" + System.currentTimeMillis() + "', '" + type + "')");
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
