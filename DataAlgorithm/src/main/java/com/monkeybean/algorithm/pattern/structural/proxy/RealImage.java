package com.monkeybean.algorithm.pattern.structural.proxy;

/**
 * 实体类
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class RealImage implements Image {
    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk(fileName);
    }

    @Override
    public void display() {
        System.out.println("Simulate Displaying " + fileName);
    }

    private void loadFromDisk(String fileName) {
        System.out.println("Simulate Loading " + fileName);
    }
}
