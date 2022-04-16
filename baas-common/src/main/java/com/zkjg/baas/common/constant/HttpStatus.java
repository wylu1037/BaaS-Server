package com.zkjg.baas.common.constant;

/**
 * @author wylu
 * @date 2022/4/16 上午 11:44
 */
public class HttpStatus {

    private HttpStatus() {
    }

    public static final int INITIALIZE = -100;

    public static final int CONTINUE = 100;
    public static final int OK = 200;
    public static final int CREATED = 201;

    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
}
