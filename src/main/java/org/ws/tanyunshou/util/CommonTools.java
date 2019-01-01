package org.ws.tanyunshou.util;

import java.util.UUID;

/**
 * @author yinan
 * @date 19-1-1
 */
public class CommonTools {

    private CommonTools() {

    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
