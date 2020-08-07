package cc.fatenetwork.kitpvp.clans;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClanManager {

    List<Clan> getClans();

    Map<Clan, Clan> rivalMap();

    Clan getClan(String var1);

    Clan getClan(UUID var1);

    boolean containClan(Clan var1);

    boolean inClan(Player var1);

    void createClan(Clan var1);

    void removeClan(Clan var1);

    void sendRivalRequest(Clan var1, Clan var2);

    void acceptRivalRequest(Clan var1, Clan var2);

    void addRival(Clan var1, Clan var2);

    void removeRival(Clan var1, Clan var2);

    void addToClan(Player var1, Clan var2);

    void removeFromClan(Player var1, Clan var2);

    void invitePlayer(Player var1, Player var2, Clan var3);

    void joinClan(Player var1, Clan var2);
}
