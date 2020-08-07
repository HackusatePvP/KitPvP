package cc.fatenetwork.kitpvp.kits;

import cc.fatenetwork.kitpvp.KitPvP;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

import java.util.Map;

public class KitsManager {
    private final KitPvP plugin;

    public KitsManager(KitPvP plugin) {
        this.plugin = plugin;
    }

    public boolean inRegion(Location loc, String s) {
        Vector vec = new Vector(loc.getX(), loc.getY(), loc.getZ());
        Map<String, ProtectedRegion> regions = plugin.getWorldGuard().getGlobalRegionManager().get(loc.getWorld()).getRegions();
        boolean trueOrFalse = false;
        for (String key : regions.keySet()) {
            ProtectedRegion region = regions.get(key);
            if (region.contains(vec)) {
                if (key.contains(s)) {
                    trueOrFalse = true;
                    return true;
                }
            }
        }
        return trueOrFalse;
    }
}
