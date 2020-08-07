package cc.fatenetwork.kitpvp.server;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    @Getter private Map<String, Server> servers = new HashMap<>();

    public Server getServer(String name) {
        return servers.get(name);
    }
}
