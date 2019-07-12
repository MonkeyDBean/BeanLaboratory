package com.monkeybean.ball.component;

import java.awt.*;
import java.util.ArrayList;

public class NpcBall extends Ball implements Runnable {
    private static int k = 10;
    private double v;
    private double d;
    private double av;
    private double ef;
    private double gva;
    private int r_l;
    private int d_u;
    private double g;

    public NpcBall(int x, int y, ArrayList<Image> img, double l, double v, double d, int r_l, int d_u) {
        super(x, y, img, l, Math.random());
        this.v = v;
        this.d = d;
        double fn = 1000000;
        this.av = 1 - (l / fn);
        this.r_l = r_l;
        this.d_u = d_u;
        gva = 1;
        double g1 = 300;
        g = (l / g1) * (l / g1);
        new Thread(this).start();
        System.out.println("create new npc ball");
    }

    private void moving() {
        double gv = (v * k / Math.sin(Math.PI / 180 * 90) * Math.sin(Math.PI / 180 * (d)));
        ef = ef < (g / 2) ? 0 : ef;
        double mg = ((g > gv && y > MyFrame.HEIGHT - l / 2 - 35) ? gv : g) * gva;
        gva = mg >= g ? y > MyFrame.HEIGHT - l / 2 - 35 ? 1 : gva + 0.05 : 1;
        y = y + (d_u * (gv + ((d_u == 1 || ef != 0) ? mg : -mg)) - ef);
        x = x + (r_l * (v * k / Math.sin(Math.PI / 180 * 90) * Math.sin(Math.PI / 180 * (90 - d))));
        v *= av;
        r_l = x < l / 2 ? 1 : x > MyFrame.WIDTH - l / 2 - 15 ? -1 : r_l;
        d_u = y < l / 2 ? 1 : y > MyFrame.HEIGHT - l / 2 - 35 ? -1 : d_u;
        double kn = 1;
        ef = y > MyFrame.HEIGHT - l / 2 - 35 ? gv * kn : 0;
        nowImg = nowImg >= img.size() - 0.8 ? 0 : nowImg + (Math.random() / 100);
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.moving();
                Thread.sleep(k);
                if (v == 0)
                    return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }
}
