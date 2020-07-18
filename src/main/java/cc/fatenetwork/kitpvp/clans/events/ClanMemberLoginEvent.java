package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanMemberLoginEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;

    public ClanMemberLoginEvent(Player player, Clan clan) {
        super(player);
        this.clan = clan;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Clan getClan() {
        return clan;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

}
