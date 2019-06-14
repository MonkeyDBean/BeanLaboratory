package com.monkeybean.algorithm.other;

import java.io.*;
import java.util.UUID;

/**
 * 在对象序列化的过程中，标记为transient的变量不会被序列化
 * <p>
 * Created by MonkeyBean on 2019/6/8.
 */
public class TransientTest implements java.io.Serializable {
    /**
     * 成员标识
     */
    private String uid;
    /**
     * 密码, 若作为临时对象, 若为明文, 安全起见, 不允许持久化
     */
    private transient String pwd;
    /**
     * 输入流, 仅用于验证流对象若不加transient, 反序列化时抛java.io.NotSerializableException异常
     * 而且流对象序列化无意义
     */
    private transient InputStream is;

    public TransientTest(String uid, String pwd, InputStream is) {
        this.uid = uid;
        this.pwd = pwd;
        this.is = is;
    }

    public static void main(String[] args) {

        //write to file
        try {
            TransientTest testObject = new TransientTest(UUID.randomUUID().toString(), "testPwd", new FileInputStream("DataAlgorithm/src/main/resource/test_file.txt"));
            System.out.println("serialize before: " + testObject);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("DataAlgorithm/src/main/resource/test_file.out"));
            out.writeObject(testObject);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //read and write back
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("DataAlgorithm/src/main/resource/test_file.out"));
            TransientTest testObject = (TransientTest) in.readObject();
            System.out.println("serialize after: " + testObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String pwd;
        if (this.pwd == null) {
            pwd = "NOT SET";
        } else {
            pwd = this.pwd;
        }
        return "TransientTest{"
                + "uid='" + uid + '\''
                + ", pwd='" + pwd + '\''
                + '}';
    }
}
