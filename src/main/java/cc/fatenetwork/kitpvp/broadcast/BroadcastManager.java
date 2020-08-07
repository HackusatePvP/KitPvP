package cc.fatenetwork.kitpvp.broadcast;

import cc.fatenetwork.kitpvp.KitPvP;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class BroadcastManager {
    @Getter private Map<String, Broadcast> broadcastMap = new HashMap<>();
    private final KitPvP plugin;

    public BroadcastManager(KitPvP plugin) {
        this.plugin = plugin;
        loadBroadcast();
    }

    public Broadcast getBroadcast(String name) {
        return broadcastMap.get(name);
    }

    void loadBroadcast() {
        FileConfiguration config = plugin.getConfig();
        if (config.getConfigurationSection("broadcast").getKeys(false).size() != 0) {
            for (String s : config.getConfigurationSection("broadcast").getKeys(false)) {
                String path = "broadcast." + s + ".";
                String name = config.getString(path + "name");
                String message = config.getString(path + "message");
                boolean enable = config.getBoolean(path + "enable");
                Broadcast broadcast = new Broadcast(name, message, enable);
                broadcastMap.put(name, broadcast);
            }
        }
    }

}
