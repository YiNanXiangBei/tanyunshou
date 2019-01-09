package org.ws.tanyunshou.task;

import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;

/**
 * @author yinan
 * @date 19-1-9
 */
public class MessageTask<T> {

    private DeferredResult<ResponseMessage> result;

    private T message;

    private boolean isTimeout;

    private MessageTask() {
    }

    public MessageTask(DeferredResult<ResponseMessage> result, T message, boolean isTimeout) {
        this.result = result;
        this.message = message;
        this.isTimeout = isTimeout;
    }

    public DeferredResult<ResponseMessage> getResult() {
        return result;
    }

    public void setResult(DeferredResult<ResponseMessage> result) {
        this.result = result;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageTask{");
        sb.append("result=").append(result);
        sb.append(", message=").append(message);
        sb.append(", isTimeout=").append(isTimeout);
        sb.append('}');
        return sb.toString();
    }
}
