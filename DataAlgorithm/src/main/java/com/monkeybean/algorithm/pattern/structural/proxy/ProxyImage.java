package com.monkeybean.algorithm.pattern.structural.proxy;

/**
 * 代理类
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class ProxyImage implements Image {
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 实体对象
     */
    private RealImage realImage;

    /**
     * 记录实体对象调用次数
     */
    private int count;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
        this.count++;
        System.out.println("call times is " + count);
    }
}