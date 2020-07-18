package cc.fatenetwork.kitpvp.combat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CombatTagEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;

    public CombatTagEvent(Player player, Player attacker) {
        super(player);
        this.attacker = attacker;
    }

    public Player getAttacker() {
        return attacker;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}
