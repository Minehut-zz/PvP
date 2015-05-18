package com.minehut.pvp.events.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Created by Luke on 2/6/15.
 */
public class SpawnPreparePlayerEvent extends Event {
	private Player player;

	public SpawnPreparePlayerEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
