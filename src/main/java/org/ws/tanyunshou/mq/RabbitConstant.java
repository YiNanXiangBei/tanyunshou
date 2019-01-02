package org.ws.tanyunshou.mq;

/**
 * @author yinan
 * @date 18-12-26
 */
public class RabbitConstant {

    /**
     * 队列名称
     */
    public static final String AMOUNT_QUEUE = "tanyunshou.amount";

    /**
     * 队列名称
     */
    public static final String MONEY_QUEUE = "tanyunshou.money";

    /**
     * 队列名称
     */
    public static final String SERIAL_NO_QUEUE = "tanyunshou.serialno";

    /**
     * 交换机名称
     */
    public static final String EXCHANGE = "exchange";

    /**
     * 路由名称
     * 金额基本信息
     */
    public static final String AMOUNT_ROUTING_KEY = "tanyunshou.amount";

    /**
     * 路由名称
     * 金额
     */
    public static final String MONEY_ROUTING_KEY = "tanyunshou.money";

    /**
     * 路由名称
     * 序列号
     */
    public static final String SERIAL_ROUTING_KEY = "tanyunshou.serialno";

}
