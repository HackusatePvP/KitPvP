package cc.fatenetwork.kitpvp;

import cc.fatenetwork.kitpvp.clans.ClanCommand;
import cc.fatenetwork.kitpvp.clans.ClanListener;
import cc.fatenetwork.kitpvp.clans.ClanManager;
import cc.fatenetwork.kitpvp.clans.FlatFileClanManager;
import cc.fatenetwork.kitpvp.combat.CombatListener;
import cc.fatenetwork.kitpvp.combat.CombatManager;
import cc.fatenetwork.kitpvp.commands.*;
import cc.fatenetwork.kitpvp.config.DataConfig;
import cc.fatenetwork.kitpvp.database.MongoManager;
import cc.fatenetwork.kitpvp.economy.EconomyCommand;
import cc.fatenetwork.kitpvp.economy.EconomyManager;
import cc.fatenetwork.kitpvp.events.DeathEvent;
import cc.fatenetwork.kitpvp.events.PlayerListener;
import cc.fatenetwork.kitpvp.gui.GUIManager;
import cc.fatenetwork.kitpvp.holograms.Hologram;
import cc.fatenetwork.kitpvp.holograms.HologramInterface;
import cc.fatenetwork.kitpvp.holograms.HologramManager;
import cc.fatenetwork.kitpvp.broadcast.BroadcastManager;
import cc.fatenetwork.kitpvp.kits.KitsManager;
import cc.fatenetwork.kitpvp.levels.LevelInterface;
import cc.fatenetwork.kitpvp.levels.LevelManager;
import cc.fatenetwork.kitpvp.profiles.ProfileManager;
import cc.fatenetwork.kitpvp.quests.QuestCommand;
import cc.fatenetwork.kitpvp.quests.QuestInterface;
import cc.fatenetwork.kitpvp.quests.QuestListener;
import cc.fatenetwork.kitpvp.quests.QuestManager;
import cc.fatenetwork.kitpvp.rank.RankManager;
import cc.fatenetwork.kitpvp.server.ServerManager;
import cc.fatenetwork.kitpvp.stats.StatsInterface;
import cc.fatenetwork.kitpvp.stats.StatsManager;
import cc.fatenetwork.kitpvp.tasks.Task;
import cc.fatenetwork.kitpvp.utils.SignHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

@Getter
public final class KitPvP extends JavaPlugin {
    private final Logger log = Bukkit.getLogger();
    private ProfileManager profileManager;
    private MongoManager mongoManager;
    private SignHandler signHandler;
    private ClanManager clanManager;
    private CombatManager combatManager;
    private LevelInterface levelManager;
    private QuestInterface questInterface;
    private GUIManager guiManager;
    private HologramInterface hologramInterface;
    private EconomyManager economyManager;
    private StatsInterface statsInterface;
    private ServerManager serverManager;
    private KitsManager kitsManager;
    private Task task;
    private DataConfig dataConfig;
    private BroadcastManager broadcastManager;
    private RankManager rankManager;
    private Permission perms = null;
    @Getter private static KitPvP plugin;

    @Override
    public void onEnable() {
        plugin = this;
        log.info("[KitPvP] initializing...");
        if (!setupPermissions()) {
            log.severe("[KitPvP] No vault supported permissions system found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        log.info("[KitPvP] loading config.yml...");
        File file = new File(getDataFolder() + "config.yml");
        if (!file.exists()) {
            saveConfig();
        }
        if (getWorldEdit() == null) {
            log.info("[KitPvP] failed to find worldedit plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (getWorldGuard() == null) {
            log.info("[KitPvP] failed to find worldguard plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        log.info("[KitPvP] loading data.yml ...");
        dataConfig = new DataConfig(this, "data.yml");
        dataConfig.getConfig().options().copyDefaults(true);
        dataConfig.saveConfig();
        dataConfig.saveDefaultConfig();
        dataConfig.reloadConfig();
        ConfigurationSerialization.registerClass(Hologram.class);
        log.info("[KitPvP] registering commands...");
        registerCommands();
        registerEvents();
        registerManagers();
        log.info("[KitPvP] getting database entries...");
        if (mongoManager.getServer("KitPvP") == null) {
            mongoManager.insertServer();
        }
        mongoManager.parseServer("KitPvP");
        mongoManager.loadClans();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        signHandler.cancelTasks(null);
        dataConfig.saveConfig();
        saveConfig();
        plugin = null;
    }

    private void registerManagers() {
        profileManager = new ProfileManager();
        statsInterface = new StatsManager(this);
        serverManager = new ServerManager();
        mongoManager = new MongoManager(this);
        levelManager = new LevelManager(this);
        hologramInterface = new HologramManager(this);
        signHandler = new SignHandler(this);
        clanManager = new FlatFileClanManager(this);
        combatManager = new CombatManager();
        questInterface = new QuestManager(this);
        kitsManager = new KitsManager(this);
        rankManager = new RankManager(this);
        task = new Task(this);
        task.runTaskTimer(this, 0, 20);
        guiManager = new GUIManager(this);
        economyManager = new EconomyManager();
        log.info("[KitPvP] loading broadcaster...");
        broadcastManager = new BroadcastManager(this);
    }

    private void registerEvents() {
        Arrays.asList(new ClanListener(this), new PlayerListener(this), new DeathEvent(this),
        new QuestListener(this), new CombatListener(this)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        getCommand("applykit").setExecutor(new ApplyKitCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("quest").setExecutor(new QuestCommand(this));
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getCommand("teammate").setExecutor(new TeammateCommand());
        getCommand("clan").setExecutor(new ClanCommand(this));
        getCommand("combat").setExecutor(new CombatCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("lunarclient").setExecutor(new LunarCommand());
        getCommand("test").setExecutor(new TestCommand(this));
    }

    public WorldGuardPlugin getWorldGuard() {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        //WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {

            System.out.print("WorldGuard cannot be found with Pineapple!");

            return null; //May put out an error.
        }

        return (WorldGuardPlugin) plugin;

    }

    public WorldEditPlugin getWorldEdit() {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        //WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {

            System.out.print("WorldEdit cannot be found with Pineapple!");

            return null; //May put out an error.
        }

        return (WorldEditPlugin) plugin;

    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
