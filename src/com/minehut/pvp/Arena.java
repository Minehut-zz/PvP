package com.minehut.pvp;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.gson.Gson;

public class Arena {

	public transient int id = -1;
	
	public String name = "TEMPNAME";
	
	public UUID arenaUUID = UUID.randomUUID();
	
	public HutLocation team1Spawn, team2Spawn;
	
	boolean active = false;
	
	public long start = System.currentTimeMillis();
	
	public ArrayList<UUID> team1 = new ArrayList<UUID>(), team2 = new ArrayList<UUID>();
	
	public ArenaType type = ArenaType.melee;
	
	public transient Core core;
	
	public Arena(Core core) {
		this.core = core;
		team1Spawn = new HutLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
		team2Spawn = new HutLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
	}
	
	public void updateArena() {
		//this.core.api.getStatManager().getMySQL().setData("id", String.format("%d", this.id), "arena_json", arena, "arenas");
		
		this.core.api.getStatManager().getMySQL().setData("id", String.format("%d", this.id), "arena_json", new Gson().toJson(this), "arenas");
	}
	
}
