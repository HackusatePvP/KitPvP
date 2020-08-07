package cc.fatenetwork.kitpvp.combat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TeleportToSpawnEvent extends PlayerEvent {
    private final static HandlerList handlers = new HandlerList();

    public TeleportToSpawnEvent(Player player) {
        super(player);
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}
