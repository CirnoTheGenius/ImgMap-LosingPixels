package com.tenko.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;

public class SlideshowRenderEvent extends Event {
	
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
    private String[] urls;
    
    /**
     * Constructs a new Slideshow render event.
     * @param url - The URL that is being used.
     * @param viewport - The MapView being used.
     */
    public SlideshowRenderEvent(String[] urls, MapView viewport){
    	this.viewport = viewport;
    	this.urls = urls;
    }
    
    /**
     * Gets the URLs that are being used.
     * @return An array of URLs (Note: These are strings, not URL objects.)
     */
    public String[] getURLs(){
    	return urls;
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