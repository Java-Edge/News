package com.sss.async;

/**
 * Created by Shusheng Shi on 2017/5/17 18:59.
 */

/**
 * 发生的事件类型
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
