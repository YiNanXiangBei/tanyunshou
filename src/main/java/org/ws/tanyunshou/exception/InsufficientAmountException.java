package org.ws.tanyunshou.exception;

/**
 * @author yinan
 * @date 19-1-5
 */
public class InsufficientAmountException extends Exception {

    public InsufficientAmountException(String message) {
        super(message);
    }

    public InsufficientAmountException(String message, Throwable cause) {
        super(message, cause);
    }


}
