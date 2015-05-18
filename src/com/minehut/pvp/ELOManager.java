package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.minehut.pvp.arena.ArenaType;
import com.minehut.pvp.queue.QueuePlayer;

public class ELOManager {

	private Core core;
	
	public ELOManager(Core core) {
		this.core = core;
	}

	
	public class PlayerRanks {
		public ArrayList<ELO> ranks = new ArrayList<ELO>();
		public transient UUID playerUUID;
		public transient Core core;
		public ELO getRankInQueue(ArenaType type) {
			for (ELO elo : ranks ){
				if (elo.eloType == type) {
					return elo;
				}
			}
			return new ELO();
		}
		public PlayerRanks addELORank(ELO elo) {
			ranks.add(elo);
			return this;
		}
		public void updateELO(ELO newelo) {
			boolean foundELO = false;
			for (ELO elo : ranks) {
				if (elo.eloType == newelo.eloType) {
					elo.currentELO = newelo.currentELO;
					foundELO = true;
				} 
			}
			if (!foundELO) {
				ranks.add(newelo);
			}
			this.updateRanks();
		}
		public void updateRanks() {
			this.core.api.getStatManager().getMySQL().setData("uuid", this.playerUUID.toString(), "ranks_json", new Gson().toJson(this), "player_elo");
		}
	}
	
	public class ELO {
		public ArenaType eloType = ArenaType.melee;
		public int currentELO = 1200;
		public ELO setArenaType(ArenaType type) {
			this.eloType = type;
			return this;
		}
	}
	
	//public void setELO(Player player, ELO elo) {
		
	//}
	
	public int getHighestELO(Player player) {
		if (this.hasELO(player)) {
			PlayerRanks playerRanks = this.getPlayerRanks(player);
			Collections.sort(playerRanks.ranks, new ELOComparator());
			return (playerRanks.ranks.size()>=1)?playerRanks.ranks.get(0).currentELO:-1;
		} else {
			return -1;
		}
	}
	
	public class ELOComparator implements Comparator<ELO> {
		
		@Override
		public int compare(ELO arg0, ELO arg1) {
			return arg1.currentELO - arg0.currentELO;
		}
	}
	
	public int getELO(Player player, ArenaType type) {
		for (ELO elo : this.getPlayerRanks(player).ranks) {
			if (elo.eloType == type) {
				return elo.currentELO;
			}
		}
		return -1;
	}
	
	public void setELO(Player player, ArenaType type, int newELO) {
		PlayerRanks playerRanks = this.getPlayerRanks(player);
		for (ELO elo : playerRanks.ranks) {
			if (elo.eloType == type) {
				elo.currentELO = newELO;
			}
		}
		playerRanks.updateRanks();
	}
	
	public ELO getELOInArena(Player player, ArenaType type) {
		for (ELO elo : this.getPlayerRanks(player).ranks) {
			if (elo.eloType == type) {
				return elo;
			}
		}
		
		ELO newELO = new ELO();
		newELO.eloType = type;
		return newELO;
	}
	
	public boolean hasELOInArena(Player player, ArenaType type) {
		for (ELO elo : this.getPlayerRanks(player).ranks) {
			if (elo.eloType == type) {
				return true;
			}
		}
		return false;
	}
	
	public void addNewELO(Player player, ELO elo) {
		PlayerRanks playerRanks = this.getPlayerRanks(player);
		playerRanks.addELORank(elo);
		playerRanks.updateRanks();
	}
	
	public void createPlayerRanks(Player player) { //Creates template PlayerRanks json object with melee arena rank.
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
							"INSERT INTO `player_elo` (`id`, `uuid`, `ranks_json`)" +
							" VALUES (NULL, '" + player.getUniqueId().toString() + "', '" + new Gson().toJson(new PlayerRanks().addELORank(new ELO())) +"')");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PlayerRanks getPlayerRanks(Player player) {
		PlayerRanks playerRanks = new PlayerRanks();
		
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `player_elo` where `uuid` = '" + player.getUniqueId().toString() + "'");
			ResultSet res = statement.executeQuery();
			res.next();
			playerRanks = new Gson().fromJson(res.getString("ranks_json"), PlayerRanks.class);
			playerRanks.playerUUID = UUID.fromString(res.getString("uuid"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		playerRanks.core = this.core;
		return playerRanks;
	}
	
	//public void setELO(Player player, int ELO) {
		//listeners//his.core.api.getStatManager().getMySQL().setData("uuid", player.getUniqueId().toString(), "elo", Integer.toString(ELO), "player_elo");
	//}
	
	
	/*public void createELO(Player player) {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
							"INSERT INTO `player_elo` (`id`, `uuid`, `elo`)" +
							" VALUES (NULL, '" + player.getUniqueId().toString() + "', 1200)");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	
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
	
	/*public int getELO(Player player) {
		
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
	}*/
	
}
