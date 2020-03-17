package webspider.http;

/**
 * @author JIUN·LIU
 * @data 2020/2/12 13:59
 */
public enum Method {
    GET("get"),
    POST("post"),
    DELETE("delete"),
    PUT("put"),
    PATCH("patch"),
    HEAD("head"),
    OPTIONS("options"),
    TRACE("trace");;

    private String method;

    Method(String method) {
        this.method = method;
    }
}
