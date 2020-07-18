package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanJoinEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private boolean cancelled = false;

    public ClanJoinEvent(Clan clan, Player player) {
        super(player);
        this.clan = clan;
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
