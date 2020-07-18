package cc.fatenetwork.kitpvp.clans.events;

import cc.fatenetwork.kitpvp.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClanFriendlyFireEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;
    private final Player attacker;
    private boolean cancelled = false;

    public ClanFriendlyFireEvent(Player player, Player attacker, Clan clan) {
        super(player);
        this.attacker = attacker;
        this.clan = clan;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Clan getClan() {
        return clan;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

}
