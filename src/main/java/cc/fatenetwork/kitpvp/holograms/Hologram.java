package cc.fatenetwork.kitpvp.holograms;

import cc.fatenetwork.kbase.utils.PersistableLocation;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Hologram implements ConfigurationSerializable {
    private String name;
    private Location location;
    private String[] lines;
    private boolean enabled;

    public Hologram(String name, Location location, boolean enabled, String... lines) {
        this.name = name;
        this.location = location;
        this.enabled = enabled;
        this.lines = lines;
    }

    public Hologram(final Map<String, Object> map) {
        this.setName((String) map.get("name"));
        Object object = map.get("backLocation");
        if ((object = map.get("backLocation")) instanceof PersistableLocation) {
            final PersistableLocation persistableLocation = (PersistableLocation) object;
            if (persistableLocation.getWorld() != null) {
                this.location = ((PersistableLocation) object).getLocation();
            }
        }
        this.setLines((String[]) map.get("lines"));
        this.setEnabled((boolean) map.get("enabled"));
    }

    public Map<String, Object> serialize(){
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", name);
        if (this.location != null && this.location.getWorld() != null){
            map.put("location", new PersistableLocation(this.location));
        }
        map.put("lines", this.lines);
        map.put("enabled", this.enabled);
        return map;
    }
}
