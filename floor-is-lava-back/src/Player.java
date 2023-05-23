import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable {

    private String nickname;
    private Socket socket;

    public Player(String nickname, Socket socket){
        this.nickname = nickname;
        this.socket = socket;
    }

    public String getNickname() { return nickname; }

    public Socket getSocket() { return socket; }


}
