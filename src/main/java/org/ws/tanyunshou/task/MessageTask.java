package org.ws.tanyunshou.task;

/**
 * @author yinan
 * @date 19-1-9
 */
public class MessageTask<T> {

    private String code;

    private T message;

    private MessageTask() {
    }

    public MessageTask(String code, T message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageTask{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message=").append(message);
        sb.append('}');
        return sb.toString();
    }
}
