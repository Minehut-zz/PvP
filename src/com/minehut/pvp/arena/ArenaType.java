package com.minehut.pvp.arena;

public enum ArenaType {

	ranged("ranged"), melee("melee"), both("both");
	
	private String type;
	
	private ArenaType(String type) {
		this.type = type;
	}
	
}
