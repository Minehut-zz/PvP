package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

public class ArenaManager {

	private Core core;
	
	public ArenaManager(Core core) {
		this.core = core;
	}
	
	public Arena getPlayerArena(Player player) {
		ArrayList<Arena> arenas = this.getArenas();
		
		for (Arena arena : arenas) {
			if (arena.team1.contains(player.getUniqueId())||arena.team2.contains(player.getUniqueId())) {
				return arena;
			}
		}
		
		return null;
	}
	
	public boolean isPlayerInArena(Player player) {
		ArrayList<Arena> arenas = this.getArenas();
		
		for (Arena arena : arenas) {
			if (arena.team1.contains(player.getUniqueId())||arena.team2.contains(player.getUniqueId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Arena> getEmptyArenasOfType(ArenaType type) {
		ArrayList<Arena> out = this.getEmptyArenas();
		Iterator<Arena> i = out.iterator();
		while (i.hasNext()) {
			if (!i.next().type.equals(type)) {
				i.remove();
			}
		}
		return out;
	}
	
	public ArrayList<Arena> getEmptyArenas() {
		ArrayList<Arena> out = this.getArenas();
		Iterator<Arena> i = out.iterator();
		while (i.hasNext()) {
			if (i.next().active) {
				i.remove();
			}
		}
		return out;
	}
    
	public ArrayList<Arena> getArenas() {
		try {
			PreparedStatement statement = this.core.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `arenas`");
			ResultSet res = statement.executeQuery();
			ArrayList<Arena> arenas = new ArrayList<Arena>();
			while (res.next()) {
				Arena loadedArena = this.core.gson.fromJson(res.getString("arena_json"), Arena.class);
				loadedArena.id = res.getInt("id");
				loadedArena.core = this.core;
				arenas.add(loadedArena);
			}
			return arenas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Arena getArena(String name) {
		for (Arena arena : this.getArenas()) {
			if (arena.name.equalsIgnoreCase(name)) {
				return arena;
			}
		}
		return null;
	}
}
