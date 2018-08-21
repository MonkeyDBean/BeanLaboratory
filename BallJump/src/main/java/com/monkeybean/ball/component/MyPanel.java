package com.monkeybean.ball.component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyPanel extends JPanel {
    private ArrayList<Ball> b;

    MyPanel(ArrayList<Ball> b) {
        this.b = b;
        System.out.println("panel start");
    }

    public void paint(Graphics g) {
        try {
            super.paint(g);

            //不可替换为forEach, 列表元素在运行时增多,foreach和Iterator都会发生java.util.ConcurrentModificationException
            for (int i = 0; i < b.size(); i++) {
                int x = (int) (b.get(i).x - (b.get(i).l / 2));
                int y = (int) (b.get(i).y - (b.get(i).l / 2));
                int xr = (int) b.get(i).l;
                int yr = (int) b.get(i).l;
                g.drawImage(b.get(i).img.get((int) b.get(i).nowImg), x, y, xr, yr, null);
            }
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
