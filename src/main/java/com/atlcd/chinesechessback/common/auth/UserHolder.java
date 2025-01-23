package com.atlcd.chinesechessback.common.auth;


import com.atlcd.chinesechessback.pojo.entity.User;

public class UserHolder {

    public static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setCurrentIUser(User user) {
        threadLocal.set(user);
    }

    public static User getCurrentUser() {
        return threadLocal.get();
    }

}
