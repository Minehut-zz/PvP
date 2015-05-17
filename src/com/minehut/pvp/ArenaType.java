package com.minehut.pvp;

public enum ArenaType {

	ranged("ranged"), melee("melee"), both("both");
	
	private String type;
	
	private ArenaType(String type) {
		this.type = type;
	}
	
}
