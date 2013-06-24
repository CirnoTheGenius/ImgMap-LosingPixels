package com.tenko.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;

public class ImageRenderEvent extends Event {

	/**
	 * List of handlers.
	 */
	private static final HandlerList handlers = new HandlerList();

	/**
	 * The MapView associated.
	 */
	private MapView viewport;

	/**
	 * The URL associated.
	 */
	private String url;

	/**
	 * Constructs a new Image render event.
	 * @param url - The URL that is being used.
	 * @param viewport - The MapView being used.
	 */
	public ImageRenderEvent(String urlz, MapView view){
		this.viewport = view;
		this.url = urlz;
	}

	/**
	 * Gets the URL that is being used.
	 * @return A string URL.
	 */
	public String getURL(){
		return url;
	}

	/**
	 * Gets the MapView being used.
	 * @return A MapView.
	 */
	public MapView getView(){
		return viewport;
	}

	/**
	 * Gets the HandlerList.
	 * @return A HandlerList
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the HandlerList.
	 * @return A HandlerList
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

}