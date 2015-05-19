package com.minehut.pvp.arena;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import com.minehut.api.util.kit.Kit;
import com.minehut.commons.common.chat.F;
import com.minehut.commons.common.player.PlayerUtil;

import com.minehut.pvp.Core;
import com.minehut.pvp.HutLocation;
import com.minehut.pvp.events.EventCaller;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

public class Arena {

	public transient int id = -1;
	
	public String name = "TEMPNAME";
	
	public UUID arenaUUID = UUID.randomUUID();
	
	public HutLocation team1Spawn = new HutLocation(Bukkit.getWorlds().get(0).getSpawnLocation()), team2Spawn = new HutLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
	
	boolean active = false, playing = false;
	
	public long start = System.currentTimeMillis();
	
	public ArrayList<UUID> team1 = new ArrayList<UUID>(), team2 = new ArrayList<UUID>();
	
	public ArenaType type = ArenaType.pot;
	
	public int maxWait = 180, startDelay = 20; //Max wait = time until arena auto resets from no joins, startDelay = delay before starting after everyone is ready.
	
	public transient Core core;
	
	public transient ArrayList<UUID> team1Deaths, team2Deaths;
	
	public Arena(Core core) {
		this.core = core;
	}
	
	public int getTeamELO(Team team) {
		int elo = 0;
		if (team == Team.TEAM1) {
			for (UUID uuid : team1) {
				elo += this.core.eloManager.getELO(Bukkit.getPlayer(uuid), this.type);
			}
			elo = (elo / team1.size());
		} else
		if (team == Team.TEAM2) {
			for (UUID uuid : team2) {
				elo += this.core.eloManager.getELO(Bukkit.getPlayer(uuid), this.type);
			}
			elo = (elo / team2.size());
		}
		
		return elo;
	}
	
	public void end(Team winningTeam) {
		this.active = false;
		if (winningTeam == Team.TEAM1) {
			int team1ELO = this.getTeamELO(Team.TEAM1);
			int team2ELO = this.getTeamELO(Team.TEAM2);
			for (UUID uuid : this.team1) {
				//TODO: reward player and calc ELO

				Player player = Bukkit.getPlayer(uuid);
				player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
				EventCaller.callSpawnPreparePlayer(player);

				//TODO: JSON MESSAGES WITH COMMAND TO REQUEUE
				this.core.eloManager.setELO(player, this.type, this.core.eloManager.getWinnerELO(team1ELO, team2ELO));
			}
			for (UUID uuid : this.team2) {
				Player player = Bukkit.getPlayer(uuid);
				player.sendMessage("You lost! Thank you for playing.");
				this.core.eloManager.setELO(player, this.type, this.core.eloManager.getLoserELO(team1ELO, team2ELO));
			}
		} else
		if (winningTeam == Team.TEAM2) {
			int team1ELO = this.getTeamELO(Team.TEAM1);
			int team2ELO = this.getTeamELO(Team.TEAM2);
			for (UUID uuid : this.team2) {
				//TODO: reward player and calc ELO

				Player player = Bukkit.getPlayer(uuid);
				player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
				EventCaller.callSpawnPreparePlayer(player);

				//TODO: JSON MESSAGES WITH COMMAND TO REQUEUE
				this.core.eloManager.setELO(player, this.type, this.core.eloManager.getWinnerELO(team2ELO, team1ELO));
			}
			for (UUID uuid : this.team1) {
				Player player = Bukkit.getPlayer(uuid);
				player.sendMessage("You lost! Thank you for playing.");
				this.core.eloManager.setELO(player, this.type, this.core.eloManager.getLoserELO(team2ELO, team1ELO));
			}
		}
		this.team1.clear();
		this.team2.clear();
		//TODO: Anything else
		this.updateArena(); // push to sql
	}
	
	public void joinArena(Player player) {
		Team team = this.getPlayerTeam(player);
		if (team.equals(Team.TEAM1)) {
			player.teleport(this.team1Spawn.getLocation());
			player.sendMessage("Sending you to Arena on Team 1");
		} else
		if (team.equals(Team.TEAM2)) {
			player.teleport(this.team2Spawn.getLocation());
			player.sendMessage("Sending you to Arena on Team 2");
		}
		Kit kit = core.getKit(this.type.getType());
		F.log("applying kit " + kit.getName());
		kit.equip(core.api.getGamePlayer(player));
	}
	
	public Team getPlayerTeam(Player player) {
		if (team1.contains(player.getUniqueId())) {
			System.out.println(player.getName() + " is on team 1");
			return Team.TEAM1;
		} else 
		if (team2.contains(player.getUniqueId())) {
			System.out.println(player.getName() + " is on team 2");
			return Team.TEAM2;
		} else {
			return Team.SPEC_OR_UNKNOWN;
		}
	}

	public Team getEnemyTeam(Player player) {
		Team playerTeam = getPlayerTeam(player);

		if (playerTeam == Team.TEAM1) {
			return Team.TEAM2;
		} else if (playerTeam == Team.TEAM2) {
			return Team.TEAM1;
		} else {
			F.log("tried to find opposite of spectator or null team.");
			return null; //spectator call
		}
	}
	
	public enum Team {
		TEAM1, TEAM2, SPEC_OR_UNKNOWN;
	}
	/*
	public boolean team1Ready() {
		ArrayList<UUID> playersOnline = new ArrayList<UUID>();
		for (UUID uuid : this.team1) {
			OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(uuid);
			if (offPlayer.isOnline()) {
				Player player = offPlayer.getPlayer();
				
			}
		}
		
		return false;
	}*/
	
	public World getWorld() {
		return this.team1Spawn.getWorld();
	}
	
	public void updateArena() {
		//this.core.api.getStatManager().getMySQL().setData("id", String.format("%d", this.id), "arena_json", arena, "arenas");
		this.core.api.getStatManager().getMySQL().setData("id", String.format("%d", this.id), "arena_json", new Gson().toJson(this), "arenas");
	}

	public void createArena() {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement(
					"INSERT INTO `arenas` (`id`, `arena_json`)" +
							" VALUES (NULL, '" + new Gson().toJson(this) + "')");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public UUID getArenaUUID() {
		return arenaUUID;
	}

	public HutLocation getTeam1Spawn() {
		return team1Spawn;
	}

	public HutLocation getTeam2Spawn() {
		return team2Spawn;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isPlaying() {
		return playing;
	}

	public long getStart() {
		return start;
	}

	public ArrayList<UUID> getTeam1() {
		return team1;
	}

	public ArrayList<UUID> getTeam2() {
		return team2;
	}

	public ArenaType getType() {
		return type;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public int getStartDelay() {
		return startDelay;
	}

	public Core getCore() {
		return core;
	}

	public ArrayList<UUID> getTeam1Deaths() {
		return team1Deaths;
	}

	public ArrayList<UUID> getTeam2Deaths() {
		return team2Deaths;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setArenaUUID(UUID arenaUUID) {
		this.arenaUUID = arenaUUID;
	}

	public void setTeam1Spawn(HutLocation team1Spawn) {
		this.team1Spawn = team1Spawn;
	}

	public void setTeam2Spawn(HutLocation team2Spawn) {
		this.team2Spawn = team2Spawn;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setTeam1(ArrayList<UUID> team1) {
		this.team1 = team1;
	}

	public void setTeam2(ArrayList<UUID> team2) {
		this.team2 = team2;
	}

	public void setType(ArenaType type) {
		this.type = type;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public void setStartDelay(int startDelay) {
		this.startDelay = startDelay;
	}

	public void setCore(Core core) {
		this.core = core;
	}

	public void setTeam1Deaths(ArrayList<UUID> team1Deaths) {
		this.team1Deaths = team1Deaths;
	}

	public void setTeam2Deaths(ArrayList<UUID> team2Deaths) {
		this.team2Deaths = team2Deaths;
	}
}
