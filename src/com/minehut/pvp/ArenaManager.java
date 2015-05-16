package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class ArenaManager {

	private Core core;
	
	public ArenaManager(Core core) {
		this.core = core;
	}
	
	public ArrayList<Arena> getEmptyArenasOfType(ArenaType type) {
		ArrayList<Arena> out = this.getArenas();
		Iterator<Arena> i = out.iterator();
		while (i.hasNext()) {
			Arena temp = i.next();
			if (temp.type != type) {
				i.remove();
			}
			if (temp.active) {
				i.remove();
			} 
		}
		return out;
	}
	
	public ArrayList<Arena> getEmptyArenas() {
		ArrayList<Arena> out = this.getArenas();
		Iterator<Arena> i = out.iterator();
		while (i.hasNext()) {
			Arena temp = i.next();
			if (temp.active) {
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
				arenas.add(loadedArena);
			}
			return arenas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}