package cc.fatenetwork.kitpvp.server;

import lombok.Data;

@Data
public class Server {
    private String name;
    private String topPlayer1,topPlayer2,topPlayer3,topPlayer4,topPlayer5;
    private int topPlayerStat1,topPlayerStat2,topPlayerStat3,topPlayerStat4,topPlayerStat5;
    private double balance;

    public Server(String name) {
        this.name = name;
    }

}
