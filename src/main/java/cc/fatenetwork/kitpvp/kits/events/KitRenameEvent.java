package cc.fatenetwork.kitpvp.kits.events;

import cc.fatenetwork.kitpvp.kits.Kit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitRenameEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
    private final Kit kit;
    private final String oldName;
    private boolean cancelled = false;
    private String newName;

    public KitRenameEvent(Kit kit, String oldName, String newName){
        this.kit = kit;
        this.oldName = oldName;
        this.newName = newName;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Kit getKit(){
        return this.kit;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
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
