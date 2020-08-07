package cc.fatenetwork.kitpvp.holograms;
import java.util.List;

public interface HologramInterface {

    List<Hologram> getHolograms();

    Hologram getHologram(String var1);

    void createHologram(Hologram var1);

    void removeHologram(Hologram var1);

    boolean containsHologram(Hologram var1);

    void reloadHologramData();

    void saveHologramData();
}
