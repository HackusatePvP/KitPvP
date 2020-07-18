package cc.fatenetwork.kitpvp.profiles;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {
    @Getter public Map<UUID, Profile> profiles = new HashMap<>();

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public boolean profileExists(UUID uuid) {
        return profiles.containsKey(uuid);
    }

}
