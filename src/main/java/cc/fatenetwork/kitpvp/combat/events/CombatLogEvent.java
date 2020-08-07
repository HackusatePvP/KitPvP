package cc.fatenetwork.kitpvp.combat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CombatLogEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public CombatLogEvent(Player player, Player attacker) {
        super(player);
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}