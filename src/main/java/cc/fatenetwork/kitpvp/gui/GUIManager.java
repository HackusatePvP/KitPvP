package cc.fatenetwork.kitpvp.gui;

import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.gui.impl.ClanGUI;
import cc.fatenetwork.kitpvp.gui.impl.QuestGUI;
import cc.fatenetwork.kitpvp.gui.impl.SettingsGUI;
import cc.fatenetwork.kitpvp.gui.impl.ShopGUI;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Arrays;

@Getter
public class GUIManager {
    private QuestGUI questGUI;
    private SettingsGUI settingsGUI;
    private ClanGUI clanGUI;
    private ShopGUI shopGUI;
    private final KitPvP plugin;

    public GUIManager(KitPvP plugin) {
        this.plugin = plugin;
        registerGUIS();
    }

    void registerGUIS() {
        questGUI = new QuestGUI(plugin);
        settingsGUI = new SettingsGUI(plugin);
        clanGUI = new ClanGUI(plugin);
        shopGUI = new ShopGUI(plugin);
        Arrays.asList(questGUI, settingsGUI, clanGUI, shopGUI).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
    }
}
