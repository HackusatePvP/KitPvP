package cc.fatenetwork.kitpvp.database;

import cc.fatenetwork.kbase.utils.ClientAPI;
import cc.fatenetwork.kitpvp.KitPvP;
import cc.fatenetwork.kitpvp.clans.Clan;
import cc.fatenetwork.kitpvp.clans.events.ClanDisbandEvent;
import cc.fatenetwork.kitpvp.profiles.Profile;
import cc.fatenetwork.kitpvp.utils.StringUtil;
import com.mongodb.*;
import com.mongodb.util.JSON;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MongoManager {
    private final Logger log = Bukkit.getLogger();
    private MongoClient mongoClient;
    private DB db;

    public MongoManager() {
        log.info("[KitPvP] starting database...");
        try {
            MongoClientURI uri;
            uri = new MongoClientURI("mongodb://" + "admin" + ":" + "fate74" + "@" + "localhost" + ":" + 27017 + "/?authSource=" + "admin");            mongoClient = new MongoClient(uri);
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
        Profile profile = KitPvP.getPlugin().getProfileManager().getProfile(uuid);
        Player player = Bukkit.getPlayer(uuid);

        DBCollection coll = db.getCollection("profiles");

        BasicDBObject doc = new BasicDBObject("uuid", uuid)
                .append("name", player.getName())
                .append("firstPlayed", player.getFirstPlayed())
                .append("activeQuest", "Starter Quest")
                .append("level", String.valueOf(1))
                .append("xp", 0.0)
                .append("kills", String.valueOf(1))
                .append("deaths", String.valueOf(1))
                .append("killstreak", String.valueOf(1))
                .append("balance", 0.0)
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
        coll.insert(doc);

        log.info("[KitPvP] " + player.getName() + " has been created.");
    }

    public void insertClan(UUID uuid, Player leader) {
        Clan clan = KitPvP.getPlugin().getClanManager().getClan(uuid);
        DBCollection collection = db.getCollection("clans");

        ArrayList<String> members = clan.getMembers();
        ArrayList<String> invited = clan.getInvited();
        BasicDBObject object = new BasicDBObject("uuid", uuid)
                .append("name", clan.getName())
                .append("prefix", clan.getPrefix())
                .append("leader", leader.getUniqueId().toString())
                .append("invited", invited)
                .append("members", members);
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
                Profile pro = KitPvP.getPlugin().getProfileManager().getProfile(online.getUniqueId());
                pro.setClan(false);
                pro.setClanName("null");
                pro.setClanUUID(null);
                online.sendMessage(StringUtil.format("&cThe clan you were in has been disbanded."));
                if (ClientAPI.isClient(online)) {
                    ClientAPI.sendNotification(online, "The clan you were in has been disbanded.", 5, TimeUnit.SECONDS);
                }
            }
            KitPvP.getPlugin().getClanManager().removeClan(clan);
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
        edit.put("xp", String.valueOf(profile.getXp()));

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

    public void updateClan(Clan clan) {
        DBCollection coll = db.getCollection("profiles");
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

        edit.remove("invited");
        edit.put("invited", clan.getInvited());

        edit.remove("leader");
        edit.put("leader", clan.getLeader().toString());

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
            Clan clan = new Clan(uuid);
            KitPvP.getPlugin().getClanManager().createClan(clan);
            return (BasicDBObject) cursor.next();
        }
        return null;
    }

    public void loadClans() {
        DBCollection collection = db.getCollection("clans");
        DBCursor cursor = collection.find();
        if (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            UUID uuid = (UUID) object.get("uuid");
            parseClan(uuid);
            Clan clan = new Clan(uuid);
            KitPvP.getPlugin().getClanManager().createClan(clan);
        } else {
            log.info("[Clans not found]");
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
            clan = KitPvP.getPlugin().getClanManager().getClan(uuid);
        }

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(js);

        String name = (String) object.get("name");
        String prefix = (String) object.get("prefix");
        String leader = (String) object.get("leader");

        clan.setName(name);
        clan.setPrefix(prefix);
        clan.setLeader(UUID.fromString(leader));

        JSONArray members = (JSONArray) object.get("members");
        JSONArray invited = (JSONArray) object.get("invites");
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
        String killstreak = (String) object.get("killstreak");
        Boolean chat = (Boolean) object.get("chat");
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
        profile.setChat(chat);
        profile.setJoinmsg(joinmsg);
        profile.setScoreboard(scoreboard);
        profile.setClan(clan);
        if (profile.isClan()) {
            profile.setClanName(clanName);
            profile.setClanUUID(UUID.fromString(clanUUID));
        }
    }
}
