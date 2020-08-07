package cc.fatenetwork.kitpvp.levels;

import cc.fatenetwork.kitpvp.profiles.Profile;
import org.bukkit.entity.Player;

public interface LevelInterface {

    int getLevel(Player var1);

    int getLevel(Profile var1);

    void setLevel(Player var1, int var2);

    void setLevel(Profile var1, int var2);

    void levelUp(Player var1);

    void levelUp(Profile var1);

    void updateLevel(Profile var1);
}
