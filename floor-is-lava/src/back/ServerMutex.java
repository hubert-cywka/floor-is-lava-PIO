package back;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerMutex {
    public static Lock serverMutex = new ReentrantLock();

}
