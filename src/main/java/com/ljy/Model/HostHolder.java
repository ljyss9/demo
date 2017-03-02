package com.ljy.Model;

import org.springframework.stereotype.Component;

/**
 * Created by ljy on 2017/2/14.
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

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
