package com.monkeybean.ball.component;

import javax.swing.*;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public MyFrame(int x, int y, ArrayList<Ball> b) {
        try {
            this.setBounds(x, y, MyFrame.WIDTH, MyFrame.HEIGHT);
            MyPanel panel = new MyPanel(b);
            this.add(panel);
            this.setVisible(true);
            System.out.println("Create MyFrame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
