package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanRenameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final String oldName;
    private final String newName;
    private boolean cancelled = false;

    public ClanRenameEvent(Clan clan, String newName, String oldName) {
        this.clan = clan;
        this.newName = newName;
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public String getOldName() {
        return oldName;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Clan getClan() {
        return clan;
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
