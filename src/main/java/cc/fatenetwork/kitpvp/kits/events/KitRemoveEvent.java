package cc.fatenetwork.kitpvp.kits.events;

import cc.fatenetwork.kitpvp.kits.Kit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitRemoveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Kit kit;
    private boolean cancelled = false;

    public KitRemoveEvent(Kit kit){
        this.kit = kit;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Kit getKit(){
        return this.kit;
    }

    public boolean isCancelled(){
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}
