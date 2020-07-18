package cc.fatenetwork.kitpvp.quests.events;

import cc.fatenetwork.kitpvp.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class QuestCompleteEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Quest quest;

    public QuestCompleteEvent(Player player, Quest quest) {
        super(player);
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HandlerList getHandlers(){
        return handlers;
    }
}
