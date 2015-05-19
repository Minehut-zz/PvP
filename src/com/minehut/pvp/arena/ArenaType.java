package com.minehut.pvp.arena;

import com.minehut.commons.common.chat.C;
import org.bukkit.Material;

public enum  ArenaType {

	pot("pot", C.green + C.bold + "POT", Material.POTION),
	mcsg("mcsg", C.gold + C.bold + "MCSG", Material.FISHING_ROD),
	cqb("cqb", C.red + C.bold + "CQB", Material.STONE_SWORD),
	ranged("ranged", C.yellow + C.bold + "RANGED", Material.BOW);

	private String type;
	private String displayName;
	private Material material;
	
	private ArenaType(String type, String displayName, Material material) {
		this.type = type;
		this.displayName = displayName;
		this.material = material;
	}

	public static ArenaType getArenaType(String str) {
		for (ArenaType type : ArenaType.values()) {
			if (type.name().equalsIgnoreCase(str))
				return type;
		}
		return null;
	}

	public static ArenaType getArenaType(Material material) {
		for (ArenaType type : ArenaType.values()) {
			if (type.material == material)
				return type;
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public Material getMaterial() {
		return material;
	}

	public String getDisplayName() {
		return displayName;
	}
}
