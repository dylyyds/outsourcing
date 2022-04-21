package com.weichuang.outsourcing.common.api;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(2200, "操作成功"),
    NOVALUE(2201,"没有符合条件的数据"),
    FAILED(2500, "操作失败"),
    VALIDATE_FAILED(1400, "参数检验失败"),
    UNAUTHORIZED(1401, "您尚未登录，请重新登录"),
    FORBIDDEN(1403, "没有相关权限"),
    ERROR_MSG(1404,"用户名或密码错误");
    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
