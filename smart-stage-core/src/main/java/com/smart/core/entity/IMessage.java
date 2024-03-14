package com.smart.core.entity;

/**
 * 消息基础接口
 *
 * @author Joe
 */
public interface IMessage {

    /**
     * 键
     */
    String getKey();

    default String getMessage(Object... args) {
        return Message.get(getKey(), args);
    }
}