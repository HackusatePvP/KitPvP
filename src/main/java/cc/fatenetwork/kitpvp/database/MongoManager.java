package cc.fatenetwork.kitpvp.database;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
import cc.fatenetwork.kitpvp.clans.events.ClanDisbandEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.server.Server;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import com.mongodb.*;
import com.mongodb.util.JSON;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MongoManager {
    private final KitPvP plugin;
    private final Logger log = Bukkit.getLogger();
    private MongoClient mongoClient;
    private DB db;

    public MongoManager(KitPvP plugin) {
        this.plugin = plugin;
        log.info("[KitPvP] starting database...");
        try {
            MongoClientURI uri;
            uri = new MongoClientURI("mongodb://" + "admin" + ":" + "" + "@" + "localhost" + ":" + 27017 + "/?authSource=" + "admin");            mongoClient = new MongoClient(uri);
            log.info("[KitPvP] connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mongoClient != null) {
            db = mongoClient.getDB("kitpvp");
        }
    }

    public void close() {
        mongoClient.close();
    }

    public void insertPlayer(UUID uuid) {
        Profile profile = plugin.getProfileManager().getProfile(uuid);
        Player player = Bukkit.getPlayer(uuid);

        DBCollection coll = db.getCollection("profiles");

        BasicDBObject doc = new BasicDBObject("uuid", uuid)
                .append("name", player.getName())
                .append("firstPlayed", player.getFirstPlayed())
                .append("activeQuest", "Starter Quest")
                .append("level", String.valueOf(1))
                .append("xp", 0d)
                .append("kit", "Master")
                .append("kills", String.valueOf(1))
                .append("deaths", String.valueOf(1))
                .append("killstreak", String.valueOf(1))
                .append("balance", 0d)
                .append("objective", String.valueOf(1))
                .append("chat", true)
                .append("joinmsg", true)
                .append("scoreboard", true)
                .append("clan", false)
                .append("clanUUID", "null")
                .append("clanName", null);
        profile.setName(player.getName());
        profile.setKills(0);
        profile.setDeaths(0);
        profile.setKillstreak(0);
        profile.setFirstPlayed(player.getFirstPlayed());
        profile.setActiveQuest("Starter Quest");
        profile.setLevel(1);
        profile.setBalance(0.0);
        profile.setXp(0.0);
        profile.setClan(false);
        profile.setKit("Master");
        plugin.getStatsInterface().getKillsMap().put(player.getName(), profile.getKills());
        coll.insert(doc);

        log.info("[KitPvP] " + player.getName() + " has been created.");
    }

    public void insertServer() {
        DBCollection coll = db.getCollection("server");

        BasicDBObject doc = new BasicDBObject("server", "KitPvP")
                .append("balance", 0d)
                .append("topkillplayer1", "null")
                .append("topkillplayer2", "null")
                .append("topkillplayer3", "null")
                .append("topkillplayer4", "null")
                .append("topkillplayer5", "null")
                .append("topkillstat1", String.valueOf(0))
                .append("topkillstat2", String.valueOf(0))
                .append("topkillstat3", String.valueOf(0))
                .append("topkillstat4", String.valueOf(0))
                .append("topkillstat5", String.valueOf(0));
        Server server = new Server("KitPvP");
        plugin.getServerManager().getServers().put("KitPvP", server);
        coll.insert(doc);

    }

    public void getTopPlayer(int pos) {

    }


    public void insertClan(UUID uuid, Player leader) {
        Clan clan = plugin.getClanManager().getClan(uuid);
        DBCollection collection = db.getCollection("clans");

        ArrayList<String> members = clan.getMembers();
        ArrayList<String> invited = clan.getInvited();
        ArrayList<String> elites = clan.getElites();
        ArrayList<String> rivals = clan.getRivals();
        BasicDBObject object = new BasicDBObject("uuid", uuid)
                .append("name", clan.getName())
                .append("prefix", clan.getPrefix())
                .append("leader", leader.getUniqueId().toString())
                .append("invited", invited)
                .append("members", members)
                .append("elites", elites)
                .append("rivals", rivals)
                .append("teamDamage", false)
                .append("open", false);
        collection.insert(object);
        log.info("[KitPvP] created clan " + uuid);
    }

    public void getPlayer(UUID uuid) {
        DBCollection coll = db.getCollection("profiles");
        BasicDBObject query = new BasicDBObject("uuid", uuid);
        DBCursor cursor = coll.find(query);
        if (cursor.hasNext()) {
            JSON json = new JSON();
            String s = JSON.serialize(cursor.next());
            try {
                prase(s, uuid);
                log.info("[KitPvP] found " + uuid);
            } catch (ParseException | org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            log.info("[KitPvP] Player not found.");
        }
        cursor.close();
    }

    public void parseServer(String server) {
        DBCollection coll = db.getCollection("server");
        BasicDBObject query = new BasicDBObject("server", server);
        DBCursor cursor = coll.find(query);
        Server server1 = plugin.getServerManager().getServer(server);
        if (cursor.hasNext()) {
            JSON json = new JSON();
            String s = JSON.serialize(cursor.next());
            try {
                parse(s, server1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("[KitPvP] Player not found.");
        }
        cursor.close();
    }

    public void parseClan(UUID uuid) {
        //Clan clan = KitPvP.getPlugin().getClanManager().getClan(uuid);
        DBCollection collection = db.getCollection("clans");
        BasicDBObject query = new BasicDBObject("uuid", uuid);
        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            JSON json = new JSON();
            String s = json.serialize(cursor.next());
            try {
                parse(s, uuid);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
     }

     @SneakyThrows
     public void delete(Profile profile, Clan clan) {
        ClanDisbandEvent event = new ClanDisbandEvent(clan, profile.getPlayer());
        DBCollection collection = db.getCollection("clans");
        BasicDBObject query = new BasicDBObject("uuid", clan.getUuid());
        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            Bukkit.getPluginManager().callEvent(event);
            profile.setClan(false);
            profile.setClanName("null");
            profile.setClanUUID(null);
            for (Player online : clan.getOnline()) {
                Profile pro = plugin.getProfileManager().getProfile(online.getUniqueId());
                pro.setClan(false);
                pro.setClanName("null");
                pro.setClanUUID(null);
                online.sendMessage(StringUtil.format("&cThe clan you were in has been disbanded."));
                if (ClientAPI.isClient(online)) {
                    ClientAPI.sendNotification(online, "The clan you were in has been disbanded.", 5);
                }
            }
            plugin.getClanManager().removeClan(clan);
            collection.remove(query);
        }
     }

    public void update(UUID uuid) {
        Profile profile = KitPvP.getPlugin().getProfileManager().getProfile(uuid);
        Player player = Bukkit.getPlayer(uuid);
        DBCollection coll = db.getCollection("profiles");
        BasicDBObject object = getUUID(uuid);

        BasicDBObject edit = getUUID(uuid);
        edit.remove("name");
        edit.put("name", player.getName());

        edit.remove("kills");
        edit.put("kills", String.valueOf(profile.getKills()));

        edit.remove("deaths");
        edit.put("deaths", String.valueOf(profile.getDeaths()));

        edit.remove("killstreak");
        edit.put("killstreak", String.valueOf(profile.getKillstreak()));

        edit.remove("level");
        edit.put("level", String.valueOf(profile.getLevel()));

        edit.remove("balance");
        edit.put("balance", profile.getBalance());

        edit.remove("xp");
        edit.put("xp", profile.getXp());

        edit.remove("kit");
        edit.put("kit", profile.getKit());

        edit.remove("objective");
        edit.put("objective", profile.getObjective());

        edit.remove("chat");
        edit.put("chat", profile.isChat());

        edit.remove("joinmsg");
        edit.put("joinmsg", profile.isJoinmsg());

        edit.remove("scoreboard");
        edit.put("scoreboard", profile.isScoreboard());

        edit.remove("clan");
        edit.put("clan", profile.isClan());

        edit.remove("clanUUID");
        if (profile.getClanUUID() != null) {
            edit.put("clanUUID", profile.getClanUUID().toString());
        } else {
            edit.put("clanUUID", null);
        }

        edit.remove("clanName");
        edit.put("clanName", profile.getClanName());

        coll.update(object, edit);

    }

    public void update(String server) {
        DBCollection coll = db.getCollection("server");
        BasicDBObject object = getServer(server);


        BasicDBObject edit = getServer(server);

        edit.remove("balance");
        edit.put("balance", plugin.getServerManager().getServer("KitPvP").getBalance());

        Iterator iterator = plugin.getStatsInterface().getKillsMap().keySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            if (count == 1) {
                String name = (String) iterator.next();
                edit.remove("topkillplayer1");
                log.info("[STATS] iterator value: " + name);
                edit.put("topkillplayer1", name);

                edit.remove("topkillstat1");
                edit.put("topkillstat1", plugin.getStatsInterface().getKillsMap().get(name));
            }
            if (count == 2) {
                String name = (String) iterator.next();
                edit.remove("topkillplayer2");
                log.info("[STATS] iterator value: " + name);
                edit.put("topkillplayer2", name);

                edit.remove("topkillstat2");
                edit.put("topkillstat2", plugin.getStatsInterface().getKillsMap().get(name));
            }
            if (count == 3) {
                String name = (String) iterator.next();
                edit.remove("topkillplayer3");
                log.info("[STATS] iterator value: " + name);
                edit.put("topkillplayer3", name);

                edit.remove("topkillstat3");
                edit.put("topkillstat3", plugin.getStatsInterface().getKillsMap().get(name));
            }
            if (count == 4) {
                String name = (String) iterator.next();
                edit.remove("topkillplayer4");
                log.info("[STATS] iterator value: " + name);
                edit.put("topkillplayer4", name);

                edit.remove("topkillstat4");
                edit.put("topkillstat4", plugin.getStatsInterface().getKillsMap().get(name));
            }
            if (count == 5) {
                String name = (String) iterator.next();
                edit.remove("topkillplayer5");
                log.info("[STATS] iterator value: " + name);
                edit.put("topkillplayer5", name);

                edit.remove("topkillstat5");
                edit.put("topkillstat5", plugin.getStatsInterface().getKillsMap().get(name));
                return;
            }
        }
    }

    public void updateClan(Clan clan) {
        DBCollection coll = db.getCollection("clans");
        BasicDBObject object = getClan(clan.getUuid());

        BasicDBObject edit = getClan(clan.getUuid());
        edit.remove("name");
        edit.put("name", clan.getName());

        edit.remove("prefix");
        edit.put("prefix", clan.getPrefix());

        edit.remove("leader");
        edit.put("clan", clan.getLeader());

        edit.remove("members");
        edit.put("members", clan.getMembers());

        edit.remove("elites");
        edit.put("elites", clan.getElites());

        edit.remove("invited");
        edit.put("invited", clan.getInvited());

        edit.remove("rivals");
        edit.put("rivals", clan.getRivals());

        edit.remove("leader");
        edit.put("leader", clan.getLeader().toString());

        edit.remove("open");
        edit.put("open", clan.isOpen());

        edit.remove("teamDamage");
        edit.put("teamDamage", clan.isTeamDamage());

        coll.update(object, edit);
    }

    public BasicDBObject getUUID(UUID uuid) {
        DBCollection coll = db.getCollection("profiles");
        BasicDBObject query = new BasicDBObject("uuid", uuid);
        DBCursor cursor = coll.find(query);
        if (cursor.hasNext()) {
            return (BasicDBObject) cursor.next();
        } else {
            log.info("[KitPvP] uuid not found.");
        }

        return null;
    }

    public BasicDBObject getClan(UUID uuid) {
        DBCollection collection = db.getCollection("clans");
        BasicDBObject query = new BasicDBObject("uuid", uuid);
        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            return (BasicDBObject) cursor.next();
        }
        return null;
    }

    public BasicDBObject getServer(String server) {
        DBCollection coll = db.getCollection("server");
        BasicDBObject query = new BasicDBObject("uuid", server);
        DBCursor cursor = coll.find(query);
        if (cursor.hasNext()) {
            return (BasicDBObject) cursor.next();
        } else {
            log.info("[KitPvP] uuid not found.");
        }

        return null;
    }

    public void loadClans() {
        DBCollection collection = db.getCollection("clans");
        DBCursor cursor = collection.find();
        if (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            UUID uuid = (UUID) object.get("uuid");
            if (getClan(uuid) != null) {
                parseClan(uuid);
            }
        } else {
            log.info("[Clans not found]");
        }
    }

    @SneakyThrows
    public void parse(String js, Server server) {

        if (server == null) {
            return;
        }

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(js);

        Double money = (Double) object.get("balance");
        String topkillplayer1 = (String) object.get("topkillplayer1");
        String topkillplayer2 = (String) object.get("topkillplayer2");
        String topkillplayer3 = (String) object.get("topkillplayer3");
        String topkillplayer4 = (String) object.get("topkillplayer4");
        String topkillplayer5 = (String) object.get("topkillplayer5");

        String topkillplayerstat1 = (String) object.get("topkillstat1");
        String topkillplayerstat2 = (String) object.get("topkillstat2");
        String topkillplayerstat3 = (String) object.get("topkillstat3");
        String topkillplayerstat4 = (String) object.get("topkillstat4");
        String topkillplayerstat5 = (String) object.get("topkillstat5");

        if (topkillplayer1 != null || topkillplayer2 != null || topkillplayer3 != null || topkillplayer4 != null || topkillplayer5 != null) {
            server.setTopPlayer1(topkillplayer1);
            server.setTopPlayer2(topkillplayer2);
            server.setTopPlayer3(topkillplayer3);
            server.setTopPlayer4(topkillplayer4);
            server.setTopPlayer5(topkillplayer5);
            server.setTopPlayerStat1(Integer.parseInt(topkillplayerstat1));
            server.setTopPlayerStat2(Integer.parseInt(topkillplayerstat2));
            server.setTopPlayerStat3(Integer.parseInt(topkillplayerstat3));
            server.setTopPlayerStat4(Integer.parseInt(topkillplayerstat4));
            server.setTopPlayerStat5(Integer.parseInt(topkillplayerstat5));


            plugin.getStatsInterface().getKillsMap().put(topkillplayer1, Integer.parseInt(topkillplayerstat1));
            plugin.getStatsInterface().getKillsMap().put(topkillplayer2, Integer.parseInt(topkillplayerstat2));
            plugin.getStatsInterface().getKillsMap().put(topkillplayer3, Integer.parseInt(topkillplayerstat3));
            plugin.getStatsInterface().getKillsMap().put(topkillplayer4, Integer.parseInt(topkillplayerstat4));
            plugin.getStatsInterface().getKillsMap().put(topkillplayer5, Integer.parseInt(topkillplayerstat5));
            plugin.getEconomyManager().setServerMoney(money);
            server.setBalance(money);
        }
    }

    public void parse(String js, UUID uuid) throws org.json.simple.parser.ParseException {

        if (getClan(uuid) == null) {
            return;
        }
        Clan clan;
        if (KitPvP.getPlugin().getClanManager().getClan(uuid) == null) {
            clan = new Clan(uuid);
            clan.setOnline(new ArrayList<>());
        } else {
            clan = plugin.getClanManager().getClan(uuid);
        }

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(js);

        String name = (String) object.get("name");
        String prefix = (String) object.get("prefix");
        String leader = (String) object.get("leader");
        Boolean open = (Boolean) object.get("open");
        Boolean teamDamage = (Boolean) object.get("teamDamage");

        clan.setName(name);
        clan.setPrefix(prefix);
        clan.setLeader(UUID.fromString(leader));
        clan.setOpen(open);
        clan.setTeamDamage(teamDamage);

        JSONArray members = (JSONArray) object.get("members");
        JSONArray invited = (JSONArray) object.get("invites");
        JSONArray elites = (JSONArray) object.get("elites");
        JSONArray rivals = (JSONArray) object.get("rivals");
        if (members != null) {
            for (Object member : members) {
                if (clan.getMembers() == null) {
                    ArrayList<String> arrays = new ArrayList<>();
                    arrays.add((String) member);
                    clan.setMembers(arrays);
                } else {
                    if (!(clan.getMembers().contains((String) member))) {
                        clan.getMembers().add((String) member);
                    }
                }
            }
        }
        if (elites != null) {
            for (Object elite : elites) {
                if (clan.getElites() == null) {
                    ArrayList<String> arrays = new ArrayList<>();
                    arrays.add((String) elite);
                    clan.setElites(arrays);
                } else {
                    if (!(clan.getElites().contains((String) elite))) {
                        clan.getElites().add((String) elite);
                    }
                }
            }
        }
        if (rivals != null) {
            for (Object rival : rivals) {
                if (clan.getRivals() == null) {
                    ArrayList<String> arrays = new ArrayList<>();
                    arrays.add((String) rival);
                    clan.setRivals(arrays);
                } else {
                    if (!(clan.getRivals().contains((String) rival))) {
                        clan.getRivals().add((String) rival);
                    }
                }
            }
        }
        if (invited != null) {
            for (Object invite : invited) {
                if (clan.getInvited() == null) {
                    ArrayList<String> arrays = new ArrayList<>();
                    arrays.add((String) invite);
                    clan.setInvited(arrays);
                } else {
                    if (!(clan.getInvited().contains((String) invite))) {
                        clan.getInvited().add((String) invite);
                    }
                }
            }
        }
        plugin.getClanManager().createClan(clan);
    }

    public void prase(String js, UUID uuid) throws ParseException, org.json.simple.parser.ParseException {
        if (getUUID(uuid) == null) {
            return;
        }
        Profile profile;
        if (KitPvP.getPlugin().getProfileManager().getProfile(uuid) == null) {
            profile = new Profile(uuid);
            KitPvP.getPlugin().getProfileManager().getProfiles().put(uuid, profile);
        } else {
            profile = KitPvP.getPlugin().getProfileManager().getProfile(uuid);
        }
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(js);

        String name = (String) object.get("name");
        Long firstPlayed = (Long) object.get("firstPlayed");
        String kills = (String) object.get("kills");
        String deaths = (String) object.get("deaths");
        String level = (String) object.get("level");
        Double balance = (Double) object.get("balance");
        Double xp = (Double) object.get("xp");
        String kit = (String) object.get("kit");
        String killstreak = (String) object.get("killstreak");
        boolean chat = (boolean) object.get("chat");
        Boolean joinmsg = (Boolean) object.get("joinmsg");
        Boolean scoreboard = (Boolean) object.get("scoreboard");
        String clanName = (String) object.get("clanName");
        String clanUUID = (String) object.get("clanUUID");
        Boolean clan = (Boolean) object.get("clan");

        profile.setName(name);
        profile.setFirstPlayed(firstPlayed);
        profile.setKills(Integer.parseInt(kills));
        profile.setDeaths(Integer.parseInt(deaths));
        profile.setKillstreak(Integer.parseInt(killstreak));
        profile.setLevel(Integer.parseInt(level));
        profile.setXp(xp);
        profile.setKit(kit);
        profile.setChat(chat);
        profile.setBalance(balance);
        profile.setJoinmsg(joinmsg);
        profile.setScoreboard(scoreboard);
        profile.setClan(clan);
        if (profile.isClan()) {
            profile.setClanName(clanName);
            profile.setClanUUID(UUID.fromString(clanUUID));
        }
    }
}
