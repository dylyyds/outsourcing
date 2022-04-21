package com.weichuang.outsourcing.common.api;

/**
 * 封装API的错误码
 */
public interface IErrorCode {
    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    long getCode();

    /**
     * 获取错误代码对应的文本消息
     *
     * @return 错误代码对应的文本消息
     */
    String getMessage();
}
