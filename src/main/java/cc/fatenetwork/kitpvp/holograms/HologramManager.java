package cc.fatenetwork.kitpvp.holograms;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.utils.Config;
import cc.fatenetwork.kitpvp.utils.GenericUtils;
import org.spigotmc.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HologramManager implements HologramInterface{
    private final Map<String, Hologram> hologramNameMap = new CaseInsensitiveMap<>();
    private Config config;
    private List<Hologram> holograms = new ArrayList<>();
    private final KitPvP plugin;

    public HologramManager(KitPvP plugin) {
        this.plugin = plugin;
        this.reloadHologramData();
    }

    @Override
    public List<Hologram> getHolograms() {
        return holograms;
    }

    @Override
    public Hologram getHologram(String var1) {
        return this.hologramNameMap.get(var1);
    }

    @Override
    public void createHologram(Hologram var1) {
        if (!holograms.add(var1)) {
            holograms.add(var1);
            hologramNameMap.put(var1.getName(), var1);
        }
    }

    @Override
    public void removeHologram(Hologram var1) {
        if (holograms.remove(var1)) {
            hologramNameMap.remove(var1.getName());
            holograms.remove(var1);
        }
    }

    @Override
    public boolean containsHologram(Hologram var1) {
        return hologramNameMap.containsKey(var1.getName());
    }

    @Override
    public void reloadHologramData() {
        this.config = new Config(this.plugin, "holograms");
        Object object = this.config.get("holograms");
        if(object instanceof List){
            this.holograms = GenericUtils.createList(object, Hologram.class);
            for(Hologram hologram : this.holograms){
                this.hologramNameMap.put(hologram.getName(), hologram);
            }
        }
    }

    @Override
    public void saveHologramData() {
        this.config.set("holograms", this.holograms);
        this.config.save();
    }
}
