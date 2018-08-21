package com.monkeybean.ball.component;

import javax.swing.*;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    public static final int width = 1920;
    public static final int height = 1080;

    public MyFrame(int x, int y, ArrayList<Ball> b) {
        try {
            this.setBounds(x, y, width, height);
            MyPanel panel = new MyPanel(b);
            this.add(panel);
            this.setVisible(true);
            System.out.println("Create MyFrame");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
