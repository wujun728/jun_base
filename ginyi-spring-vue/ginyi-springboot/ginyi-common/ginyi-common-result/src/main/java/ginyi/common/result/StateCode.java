package ginyi.common.result;


public enum StateCode {

    SUCCESS(200, "操作成功"),

    ERROR_SYSTEM(5000, "系统异常"),
    ERROR_UNAUTHENTICATION(5001, "登陆授权异常"),
    ERROR_TIMEOUT_REQUEST(5002, "请求超时"),
    ERROR_TIMEOUT_TOKEN(5003, "无效令牌"),
    ERROR_INVALID_SIGN(5004, "无效签名"),
    ERROR_AUTHENTICATION_VALID(5005, "认证失败或令牌已过期"),
    ERROR_NOT_PERMISSION(5006, "权限不足"),
    ERROR_LIMITED(5007, "操作受限"),

    ERROR_BUSINESS(6000,"业务逻辑异常"),
    ERROR_PARAMS(6001,"参数校验错误"),
    ERROR_REQUEST_PARAMS(6002,"请求参数异常"),
    ERROR_PARAMS_SERVICE(6003,"参数逻辑校验错误"),
    ERROR_DATA_FORMAT(6004, "数据格式错误"),
    ERROR_EXIST(6005, "数据已存在"),
    ERROR_NOT_EXIST(6006, "数据不存在"),

    ERROR_NOT_SUPPORT(7000, "不支持该文件类型"),
    ERROR_LIMIT_EXCEEDED(7001, "超出请求次数上限"),
    ERROR_MULTIPART(7002, "文件上传异常"),
    ;

    private final int code;
    private final String message;

    StateCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
