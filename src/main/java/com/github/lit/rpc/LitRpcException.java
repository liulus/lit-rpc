package com.github.lit.rpc;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 18:17
 */
public class LitRpcException extends RuntimeException {

    private static final long serialVersionUID = -899183767656141566L;

    public LitRpcException() {
        super();
    }

    public LitRpcException(String message) {
        super(message);
    }

    public LitRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public LitRpcException(Throwable cause) {
        super(cause);
    }

    protected LitRpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
