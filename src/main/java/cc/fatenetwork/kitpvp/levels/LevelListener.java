package cc.fatenetwork.kitpvp.levels;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kbase.utils.StringUtil;
import cc.fatenetwork.kitpvp.levels.events.LevelUpEvent;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {

    @EventHandler
    @SneakyThrows
    public void onLevelUp(LevelUpEvent event) {
        Player player = event.getPlayer();
        if (ClientAPI.isClient(player)) {
            ClientAPI.sendNotification(player, "You have leveled up!", 3);
        }
        player.sendMessage(StringUtil.format("&7You have leveled up to &c" + event.getNewLevel()));
    }
}
