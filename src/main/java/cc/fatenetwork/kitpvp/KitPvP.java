package cc.fatenetwork.kitpvp;

import cc.fatenetwork.kitpvp.clans.ClanCommand;
import cc.fatenetwork.kitpvp.clans.ClanListener;
import cc.fatenetwork.kitpvp.clans.ClanManager;
import cc.fatenetwork.kitpvp.clans.FlatFileClanManager;
import cc.fatenetwork.kitpvp.combat.CombatListener;
import cc.fatenetwork.kitpvp.combat.CombatManager;
import cc.fatenetwork.kitpvp.commands.CombatCommand;
import cc.fatenetwork.kitpvp.commands.TeammateCommand;
import cc.fatenetwork.kitpvp.commands.TestCommand;
import cc.fatenetwork.kitpvp.database.MongoManager;
import cc.fatenetwork.kitpvp.events.DeathEvent;
import cc.fatenetwork.kitpvp.events.PlayerListener;
import cc.fatenetwork.kitpvp.kits.*;
import cc.fatenetwork.kitpvp.levels.LevelInterface;
import cc.fatenetwork.kitpvp.levels.LevelManager;
import cc.fatenetwork.kitpvp.profiles.ProfileManager;
import cc.fatenetwork.kitpvp.quests.QuestInterface;
import cc.fatenetwork.kitpvp.quests.QuestListener;
import cc.fatenetwork.kitpvp.quests.QuestManager;
import cc.fatenetwork.kitpvp.tasks.Task;
import cc.fatenetwork.kitpvp.utils.SignHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

@Getter
public final class KitPvP extends JavaPlugin {
    private final Logger log = Bukkit.getLogger();
    private ProfileManager profileManager;
    private MongoManager mongoManager;
    private KitsManager kitsManager;
    private SignHandler signHandler;
    private ClanManager clanManager;
    private CombatManager combatManager;
    private LevelInterface levelManager;
    private KitExecutor kitExecutor;
    private QuestInterface questInterface;
    private Task task;
    @Getter private static KitPvP plugin;

    @Override
    public void onEnable() {
        plugin = this;
        log.info("[KitPvP] initializing...");
        log.info("[KitPvP] loading config.yml...");
        File file = new File(getDataFolder() + "config.yml");
        if (!file.exists()) {
            saveConfig();
        }
        ConfigurationSerialization.registerClass(Kit.class);
        log.info("[KitPvP] registering commands...");
        registerCommands();
        registerEvents();
        registerManagers();
        mongoManager.loadClans();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        kitsManager.saveKitData();
        signHandler.cancelTasks(null);
        saveConfig();
        plugin = null;
    }

    private void registerManagers() {
        profileManager = new ProfileManager();
        mongoManager = new MongoManager();
        levelManager = new LevelManager(this);
        kitsManager = new FlatFileKitManager(this);
        signHandler = new SignHandler(this);
        clanManager = new FlatFileClanManager(this);
        combatManager = new CombatManager();
        questInterface = new QuestManager(this);
        task = new Task(this);
        task.runTaskTimer(this, 0, 20);
    }

    private void registerEvents() {
        Arrays.asList(new KitListener(this), new ClanListener(this), new PlayerListener(this), new DeathEvent(this),
        new QuestListener(this), new CombatListener(this)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        kitExecutor = new KitExecutor(this);
        getCommand("teammate").setExecutor(new TeammateCommand());
        getCommand("kit").setExecutor(kitExecutor);
        getCommand("clan").setExecutor(new ClanCommand(this));
        getCommand("combat").setExecutor(new CombatCommand(this));
        //getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kits").setExecutor(new KitsCommand(this));
        getCommand("test").setExecutor(new TestCommand(this));
    }
}
