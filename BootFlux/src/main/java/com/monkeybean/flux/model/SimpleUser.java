package com.monkeybean.flux.model;

/**
 * 用户模型, 存储于内存中
 * <p>
 * Created by MonkeyBean on 2018/8/24.
 */
public class SimpleUser {

    /**
     * 自增Id
     */
    private int id;

    /**
     * 昵称
     */
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
