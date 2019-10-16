package com.monkeybean.algorithm.pattern.behavior.template;

/**
 * 球类游戏
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class Ball extends Game {

    @Override
    void initialize() {
        System.out.println("Ball Game Initialized! Start Playing!");
    }

    @Override
    void startPlay() {
        System.out.println("Ball Game Started. Enjoy The Game!");
    }

    @Override
    void endPlay() {
        System.out.println("Ball Game Finished!");
    }
}
