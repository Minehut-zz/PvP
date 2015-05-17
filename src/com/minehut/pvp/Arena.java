package com.minehut.pvp;

import java.util.ArrayList;
import java.util.UUID;

import com.minehut.commons.common.chat.F;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
	
	public ArenaType type = ArenaType.melee;
	
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
				elo += this.core.eloManager.getELO(Bukkit.getPlayer(uuid));
			}
			elo = (elo / team1.size());
		} else
		if (team == Team.TEAM2) {
			for (UUID uuid : team2) {
				elo += this.core.eloManager.getELO(Bukkit.getPlayer(uuid));
			}
			elo = (elo / team1.size());
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
				player.sendMessage("You win!! Thank you for playing."); //TODO: JSON MESSAGES WITH COMMAND TO REQUEUE
				this.core.eloManager.setELO(player, this.core.eloManager.getWinnerELO(team1ELO, team2ELO));
			}
			for (UUID uuid : this.team2) {
				Player player = Bukkit.getPlayer(uuid);
				player.sendMessage("You lost! Thank you for playing.");
				this.core.eloManager.setELO(player, this.core.eloManager.getLoserELO(team1ELO, team2ELO));
			}
		} else
		if (winningTeam == Team.TEAM2) {
			int team1ELO = this.getTeamELO(Team.TEAM1);
			int team2ELO = this.getTeamELO(Team.TEAM2);
			for (UUID uuid : this.team2) {
				//TODO: reward player and calc ELO
				Player player = Bukkit.getPlayer(uuid);
				player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
				player.sendMessage("You win!! Thank you for playing."); //TODO: JSON MESSAGES WITH COMMAND TO REQUEUE
				this.core.eloManager.setELO(player, this.core.eloManager.getWinnerELO(team2ELO, team1ELO));
			}
			for (UUID uuid : this.team1) {
				Player player = Bukkit.getPlayer(uuid);
				player.sendMessage("You lost! Thank you for playing.");
				this.core.eloManager.setELO(player, this.core.eloManager.getLoserELO(team2ELO, team1ELO));
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
	
}
