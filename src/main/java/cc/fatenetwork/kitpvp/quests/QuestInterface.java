package cc.fatenetwork.kitpvp.quests;

import org.bukkit.entity.Player;

import java.util.List;

public interface QuestInterface {

    List<String> quests(Player var1);

    String getActiveQuest(Player var1);

    int getQuestGoal(Player var1);

    int getQuestCompletion(Player var1);

    double getQuestProgress(Player var1);

    void onQuestComplete(Player var1, Quest var2);

}
