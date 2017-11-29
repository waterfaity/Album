package com.waterfairy.http.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 作者： qingyun on 16/12/8.
 * 邮箱：419254872@qq.com
 * 版本：v1.0
 */
public class HttpExceptionUtils {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable handleException(Throwable throwable) {
        ResponseThrowable throwableTemp = null;
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.HTTP_ERROR);
            throwableTemp.message = "网络错误";
            return throwableTemp;
        } else if (throwable instanceof ServerException) {
            ServerException resultException = (ServerException) throwable;
            throwableTemp = new ResponseThrowable(resultException, resultException.code);
            throwableTemp.message = resultException.message;
            return throwableTemp;
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.PARSE_ERROR);
            throwableTemp.message = "解析错误";
            return throwableTemp;
        } else if (throwable instanceof ConnectException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.NETWORD_ERROR);
            throwableTemp.message = "连接失败";
            return throwableTemp;
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.SSL_ERROR);
            throwableTemp.message = "证书验证失败";
            return throwableTemp;
        } else if (throwable instanceof java.net.UnknownHostException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.HTTP_UNKNOWN_HOSTNAME);
            throwableTemp.message = "网络异常,稍候再试吧";
            return throwableTemp;
        } else if (throwable instanceof SocketTimeoutException) {
            throwableTemp = new ResponseThrowable(throwable, ERROR.HTTP_TIME_OUT);
            throwableTemp.message = "连接超时";
            return throwableTemp;
        } else {
            throwableTemp = new ResponseThrowable(throwable, ERROR.UNKNOWN);
            throwableTemp.message = "未知错误";
            return throwableTemp;
        }
    }

    public static class ResponseThrowable extends Exception {
        public int code = -1;
        public String message = "";

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }

    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }

    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;
        /**
         * 地址出错
         */
        public static final int HTTP_UNKNOWN_HOSTNAME = 1006;
        /**
         * 地址出错
         */
        public static final int HTTP_TIME_OUT = 1007;
    }

}
