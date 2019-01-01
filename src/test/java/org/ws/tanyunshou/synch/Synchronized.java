package org.ws.tanyunshou.synch;

/**
 * @author yinan
 * @date 19-1-1
 */
public class Synchronized {

    public static void main(String[] args) {
        synchronized (Synchronized.class) {
            System.out.println("Synchronized");
        }
    }

}
