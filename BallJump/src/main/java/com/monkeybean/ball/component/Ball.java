package com.monkeybean.ball.component;

import java.awt.*;
import java.util.ArrayList;

public class Ball {
    double x;
    double y;
    ArrayList<Image> img;
    double l;
    double nowImg;

    Ball(double x, double y, ArrayList<Image> img, double l, double nowImg) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.l = l;
        this.nowImg = nowImg;
    }
}
