package com.funnyboyroks.messenger.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event which is called each time the Messenger plugin is loaded or reloaded
 */
public class MessengerLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final boolean reload;

    public MessengerLoadEvent(boolean reload) {
        this.reload = reload;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Whether this event was trigger by a reload
     */
    public boolean reload() {
        return this.reload;
    }
}
