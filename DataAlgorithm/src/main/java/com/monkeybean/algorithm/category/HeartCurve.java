package com.monkeybean.algorithm.category;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by MonkeyBean on 2019/1/15.
 */
public class HeartCurve extends JFrame {
    /**
     * 获取屏幕窗口大小
     */
    private static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    /**
     * 定义加载窗口大小
     */
    private static final int GAME_WIDTH = 500;
    private static final int GAME_HEIGHT = 500;

    private HeartCurve() {

        // 设置窗口标题
        this.setTitle("心形曲线");

        // 设置窗口初始位置
        this.setLocation((WIDTH - GAME_WIDTH) / 2, (HEIGHT - GAME_HEIGHT) / 2);

        // 设置窗口大小
        this.setSize(GAME_WIDTH, GAME_HEIGHT);

        // 设置背景色
        this.setBackground(Color.BLACK);

        // 设置窗口关闭方式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 设置窗口显示
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new HeartCurve();
    }

    @Override
    public void paint(Graphics g) {
        double x, y, r;
        Image offScreen = createImage(GAME_WIDTH, GAME_HEIGHT);
        Graphics drawOffScreen = offScreen.getGraphics();

        //随机颜色
        Random random = new Random(System.currentTimeMillis());
        Color color;
        int randomSeq = random.nextInt(6);
        switch (randomSeq) {
            case 0:
                color = Color.BLUE;
                break;
            case 1:
                color = Color.GREEN;
                break;
            case 2:
                color = Color.RED;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.PINK;
                break;
            default:
                color = Color.WHITE;
        }
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 90; j++) {
                r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 18;
                x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i) + GAME_WIDTH / 2.0;
                y = -r * Math.sin(Math.PI / 45 * j) + GAME_HEIGHT / 4.0;

                //设置笔画颜色
                drawOffScreen.setColor(color);

                // 绘制椭圆
                drawOffScreen.fillOval((int) x, (int) y, 2, 2);
            }

            // 生成图片
            g.drawImage(offScreen, 0, 0, this);
        }
    }

}
