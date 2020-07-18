package cc.fatenetwork.kitpvp.levels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LevelUpEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final int newLevel;
    private final int oldLevel;
    private boolean cancelled;

    public LevelUpEvent(Player player, int newLevel, int oldLevel) {
        super(player);
        this.player = player;
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}
