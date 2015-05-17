package com.minehut.pvp.arena;

public enum  ArenaType {

	ranged("ranged"), melee("melee"), both("both");
	
	private String type;
	
	private ArenaType(String type) {
		this.type = type;
	}

	public static ArenaType getArenaType(String str) {
		for (ArenaType type : ArenaType.values()) {
			if (type.name().equalsIgnoreCase(str))
				return type;
		}
		return null;
	}
	
}
