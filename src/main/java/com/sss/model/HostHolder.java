package com.sss.model;

/**
 * Created by Shusheng Shi on 2017/5/10 22:02.
 */

import org.springframework.stereotype.Component;


/**
 * 此类表示当前用户
 */
@Component
public class HostHolder {

    //TODO
    private static ThreadLocal<User> users = new ThreadLocal<>();


    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
