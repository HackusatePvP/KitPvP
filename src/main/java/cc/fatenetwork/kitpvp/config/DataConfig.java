package cc.fatenetwork.kitpvp.config;

import cc.fatenetwork.kitpvp.KitPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class DataConfig {
    private final String filename;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public DataConfig(KitPvP plugin, String filename) {
        if (plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null");
        if (!plugin.isInitialized()) {
            throw new IllegalArgumentException("Plugin ust be initialized");
        }
        this.plugin = plugin;
        this.filename = filename;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        this.configFile = new File(plugin.getDataFolder(), filename);
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream deConfigStream = plugin.getResource(filename);
        if (deConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(deConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || plugin == null) {
            return;
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig() {
        if(!configFile.exists()) {
            this.plugin.saveResource(filename, false);
        }
    }
}