package org.ws.tanyunshou.util;

import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date 19-1-5
 */
public class CommonConstant {

    /**
     * 常用状态码
     */

    public static final int SUCCESS_RESPONSE = 200;

    public static final int SUCCESS_CREATED = 201;

    public static final int BAD_REQUEST = 400;

    public static final int UNAUTHORIZED = 401;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND = 404;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * 返回消息
     */

    public static String SUCCESS_REQUEST_MESSAGE = "请求成功！";

    public static String SUCCESS_CREATED_MESSAGE = "创建成功!";

    public static String BAD_REQUEST_MESSAGE = "请求失败，请求语法可能有问题！";

    public static String NOT_FOUND_MESSAGE = "请求失败，请求的数据不存在!";

    public static String SERVICE_UNAVAILABLE_MESSAGE = "请求失败，服务端无法响应，请稍后重试！";


    /**
     * 金额不足
     */
    public static String INSUFFICIENT_AMOUNT = "金额不足，无法扣除！";

    /**
     * 金额常量
     */
    public static final Amount AMOUNT = new Amount("error",
                                    new BigDecimal(-1), Thread.currentThread().getName());

}
