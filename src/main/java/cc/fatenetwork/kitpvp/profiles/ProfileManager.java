package cc.fatenetwork.kitpvp.profiles;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;

public class ProfileManager {
    @Getter public Map<UUID, Profile> profiles = new HashMap<>();
    @Getter private final Map<String, Integer> kitMap = new HashMap<>();
    @Getter private Set<Player> inKitMap = new HashSet<>();
    private final Logger log = Bukkit.getLogger();

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public boolean profileExists(UUID uuid) {
        return profiles.containsKey(uuid);
    }

}
