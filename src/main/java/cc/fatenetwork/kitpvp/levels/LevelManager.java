package cc.fatenetwork.kitpvp.levels;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.levels.events.LevelUpEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LevelManager implements LevelInterface {
    private final KitPvP plugin;

    public LevelManager(KitPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getLevel(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        return profile.getLevel();
    }

    @Override
    public int getLevel(Profile var1) {
        return var1.getLevel();
    }

    @Override
    public void setLevel(Player var1, int var2) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        profile.setLevel(var2);
    }

    @Override
    public void setLevel(Profile var1, int var2) {
        var1.setLevel(var2);
    }


    @Override
    public void levelUp(Player var1) {
        Profile profile = plugin.getProfileManager().getProfile(var1.getUniqueId());
        int oldLevel = profile.getLevel();
        int newLevel = profile.getLevel() + 1;
        double xp = profile.getXp();
        LevelUpEvent event = new LevelUpEvent(var1, newLevel, oldLevel);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if ((xp - 100) > 0) {
            profile.setXp(xp - 100);
        }
        profile.setLevel(newLevel);
    }

    @Override
    public void levelUp(Profile var1) {
        int oldLevel = var1.getLevel();
        int newLevel = var1.getLevel() + 1;
        double xp = var1.getXp();
        LevelUpEvent event = new LevelUpEvent(var1.getPlayer(), newLevel, oldLevel);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if ((xp - 100) > 0) {
            var1.setXp(xp - 100);
        }
        var1.setLevel(newLevel);
    }
}
