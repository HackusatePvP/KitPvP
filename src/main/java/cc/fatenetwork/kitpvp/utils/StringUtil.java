package cc.fatenetwork.kitpvp.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;

public class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException("Cannot initiate util class");
    }

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> messages) {
        List<String> toReturn = Lists.newArrayList();

        messages.forEach(message -> toReturn.add(format(message)));

        return toReturn;
    }
}
