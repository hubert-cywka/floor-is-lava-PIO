package front.main.java.com.pio.floorislavafront.ClientSocket;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMutex {

    public static Lock clientMutex = new ReentrantLock();

}
