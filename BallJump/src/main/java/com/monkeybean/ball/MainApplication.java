package com.monkeybean.ball;

import com.monkeybean.ball.component.Ball;
import com.monkeybean.ball.component.MyFrame;
import com.monkeybean.ball.component.NpcBall;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * forked from: https://github.com/LCsion/huaji
 */
public class MainApplication {
    public static void main(String[] args) throws IOException {
        ArrayList<Ball> balls = new ArrayList<>();
        ArrayList<Image> ballImg = new ArrayList<>();
        ClassLoader classLoader = MainApplication.class.getClassLoader();
        URL url1 = classLoader.getResource("huaji1.png");
        URL url2 = classLoader.getResource("huaji2.png");
        URL url3 = classLoader.getResource("huaji3.png");
        assert url1 != null;
        ballImg.add(ImageIO.read(new File(url1.getFile())));
        assert url2 != null;
        ballImg.add(ImageIO.read(new File(url2.getFile())));
        assert url3 != null;
        ballImg.add(ImageIO.read(new File(url3.getFile())));
        MyFrame myFrame = new MyFrame(0, 0, balls);
        for (int i = 0; i < 20; i++) {
            int ballSize = (int) (Math.random() * (500 - 10) + 10);
            balls.add(new NpcBall(
                            (int) (Math.random() * (MyFrame.WIDTH - ballSize / 2 - 15 - ballSize / 2) + ballSize / 2),
                            (int) (Math.random() * (MyFrame.HEIGHT - ballSize / 2 - 35 - ballSize / 2) + ballSize / 2),
                            ballImg, ballSize,
                            (int) (Math.random() * (5 - 1) + 1),
                            Math.random() * (90),
                            (Math.random() * 1000) % 2 == 0 ? 1 : -1,
                            (Math.random() * 1000) % 2 == 0 ? 1 : -1
                    )
            );
            System.out.println("the number of ball is " + balls.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}