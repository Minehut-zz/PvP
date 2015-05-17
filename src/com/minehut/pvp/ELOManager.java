package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

public class ELOManager {

	private Core core;
	
	public ELOManager(Core core) {
		this.core = core;
	}
	
	public void setELO(Player player, int ELO) {
		this.core.api.getStatManager().getMySQL().setData("uuid", player.getUniqueId().toString(), "elo", Integer.toString(ELO), "player_elo");
	}
	
	public void createELO(Player player) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
							"INSERT INTO `player_elo` (`id`, `uuid`, `elo`)" +
							" VALUES (NULL, '" + player.getUniqueId().toString() + "', 1200)");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getWinnerELO(int winner, int loser) {
		
		int k = 32;
		if (winner>2400) {
			k = 16;
		} else
		if (winner>2101&&winner<2400) {
			k = 24;
		} else
		if (winner<=2100) {
			k = 32;
		}
		double killerDouble = winner - loser;
		killerDouble = killerDouble / 400.0D;
		killerDouble = Math.pow(10.0D, killerDouble);
		killerDouble = 1.0D + killerDouble;
		killerDouble = 1.0D / killerDouble;
		killerDouble = 1.0D - killerDouble;
		killerDouble = k * killerDouble;
		
		return winner + (int)killerDouble;
	}
	
	public int getLoserELO(int winner, int loser) {
		int k = 32;
		k = 32;
		if (loser>2400) {
			k = 16;
		} else
		if (loser>2101&&loser<2400) {
			k = 24;
		} else
		if (loser<=2100) {
			k = 32;
		}
		
		double killedDouble = winner - loser;
		killedDouble = killedDouble / 400.0D;
		killedDouble = Math.pow(10.0D, killedDouble);
		killedDouble = 1.0D + killedDouble;
		killedDouble = 1.0D / killedDouble;
		killedDouble = 0.0D - killedDouble;
		killedDouble = k * killedDouble;
		
		return loser + (int)killedDouble;
	}
	
	public boolean hasELO(Player player) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `player_elo` where `uuid` = '" + player.getUniqueId().toString() + "'");
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
	
	public int getELO(Player player) {
		
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `player_elo` where `uuid` = '" + player.getUniqueId().toString() + "'");
			ResultSet res = statement.executeQuery();
			res.next();
			int elo = res.getInt("elo");
			return elo;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
