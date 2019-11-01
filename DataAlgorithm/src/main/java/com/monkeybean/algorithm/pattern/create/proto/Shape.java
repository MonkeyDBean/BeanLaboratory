package com.monkeybean.algorithm.pattern.create.proto;

/**
 * 形状抽象类
 * <p>
 * Created by MonkeyBean on 2019/11/1.
 */
public abstract class Shape implements Cloneable {
    protected String type;
    private String id;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    abstract void draw();

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
