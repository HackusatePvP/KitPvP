package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import javax.annotation.Nullable;


public class ClanCreateEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Nullable private final Clan clan;
    private final String name;
    private final String prefix;
    private boolean cancelled = false;

    public ClanCreateEvent(Clan clan, Player player, String name, String prefix) {
        super(player);
        this.clan = clan;
        this.player = player;
        this.name = name;
        this.prefix = prefix;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Clan getClan() {
        return clan;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
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
