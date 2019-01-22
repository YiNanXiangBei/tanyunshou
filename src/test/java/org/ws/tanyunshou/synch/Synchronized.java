package org.ws.tanyunshou.synch;

import java.util.*;
/**
 * @author yinan
 * @date 19-1-1
 */
public class Synchronized {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.add("5");
        System.out.println(list.indexOf("4"));

        String[] strings = new String[] {"2", "3", "5"};

    }

}
