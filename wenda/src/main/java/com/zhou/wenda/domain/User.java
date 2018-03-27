package com.zhou.wenda.domain;


import lombok.Data;

/**
 *
 * user实体
 */

@Data
public class User {

    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;

    public User() {
    }

    public User(int id, String name, String password, String salt, String headUrl) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
