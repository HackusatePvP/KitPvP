package cc.fatenetwork.kitpvp.broadcast;

import lombok.Data;

@Data
public class Broadcast {
    private String name;
    private String message;
    private boolean enable;

    public Broadcast(String name, String message, boolean enable) {
        this.name = name;
        this.message = message;
        this.enable = enable;
    }
}
