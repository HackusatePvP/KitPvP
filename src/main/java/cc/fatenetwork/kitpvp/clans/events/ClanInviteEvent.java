package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanInviteEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Player invited;
    private boolean cancelled;

    public ClanInviteEvent(Clan clan, Player player, Player invited) {
        super(player);
        this.clan = clan;
        this.player = player;
        this.invited = invited;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Clan getClan() {
        return clan;
    }

    public Player getInvited() {
        return invited;
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
