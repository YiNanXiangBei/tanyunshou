package org.ws.tanyunshou.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yinan
 * @date created in 上午11:01 18-12-26
 */
public class Amount implements Serializable {

    private static final long serialVersionUID = 8358328135545873783L;
    /**
     * 自动生成的序号
     */
    private int id;

    /**
     * 序列号
     */
    private String serialNo;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 线程名称
     */
    private String threadName;

    public Amount(String serialNo, BigDecimal money, String threadName) {
        this.serialNo = serialNo;
        this.money = money;
        this.threadName = threadName;
    }

    /**
     * 添加默认构造函数，用于GenericJackson2JsonRedisSerializer反序列化
     */
    private Amount() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Amount{");
        sb.append("id=").append(id);
        sb.append(", serialNo='").append(serialNo).append('\'');
        sb.append(", money=").append(money);
        sb.append(", threadName='").append(threadName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
