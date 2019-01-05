package org.ws.tanyunshou.message;

import java.io.Serializable;

/**
 * @author yinan
 * @date 19-1-5
 */
public class ResponseMessage implements Serializable {
    private static final long serialVersionUID = 5083135623966094843L;

    private int code;

    private Object data;

    private String message;

    public ResponseMessage(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResponseMessage() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ResponseMessage{");
        sb.append("code=").append(code);
        sb.append(", data=").append(data);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
